package com.example.shopapp.data.repositories

import com.example.shopapp.data.database.UserDao
import com.example.shopapp.data.models.User
import com.example.shopapp.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String): Result<User>
    suspend fun verifySecurityCode(userId: String, code: String): Result<Boolean>
    suspend fun generateSecurityCode(userId: String): Result<String>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun logout()
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val securityUtils: SecurityUtils
) : AuthRepository {
    
    private var currentUser: User? = null
    
    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserByEmail(email)
            if (user != null && securityUtils.verifyPassword(password, user.passwordHash)) {
                currentUser = user
                Result.success(user)
            } else {
                Result.failure(Exception("Неверный email или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return@withContext Result.failure(Exception("Пользователь с таким email уже существует"))
            }
            
            val user = User(
                id = securityUtils.generateUUID(),
                email = email,
                passwordHash = securityUtils.hashPassword(password),
                role = "user"
            )
            userDao.insertUser(user)
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun verifySecurityCode(userId: String, code: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val user = userDao.getUserById(userId)
            val result = user?.securityCode?.let { hashedCode ->
                securityUtils.verifyPassword(code, hashedCode)
            } ?: false
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun generateSecurityCode(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val code = securityUtils.generateSecurityCode()
            val user = userDao.getUserById(userId)
            user?.let {
                val updatedUser = it.copy(securityCode = securityUtils.hashPassword(code))
                userDao.updateUser(updatedUser)
                Result.success(code)
            } ?: Result.failure(Exception("Пользователь не найден"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isUserLoggedIn(): Boolean = currentUser != null
    
    override suspend fun getCurrentUser(): User? = currentUser
    
    override suspend fun logout() {
        currentUser = null
    }
} 