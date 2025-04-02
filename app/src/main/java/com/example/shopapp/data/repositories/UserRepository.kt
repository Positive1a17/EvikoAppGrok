package com.example.shopapp.data.repositories

import com.example.shopapp.data.database.UserDao
import com.example.shopapp.data.models.User
import com.example.shopapp.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun createUser(email: String, password: String, role: String = "user"): Result<User>
    suspend fun authenticateUser(email: String, password: String): Result<User>
    suspend fun updateSecurityCode(userId: String, code: String): Result<Boolean>
    suspend fun verifySecurityCode(userId: String, code: String): Result<Boolean>
    suspend fun getUserById(id: String): User?
    fun getAllUsers(): Flow<List<User>>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val securityUtils: SecurityUtils
) : UserRepository {
    
    override suspend fun createUser(email: String, password: String, role: String): Result<User> = 
        withContext(Dispatchers.IO) {
            try {
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser != null) {
                    return@withContext Result.failure(Exception("Пользователь с таким email уже существует"))
                }
                
                val user = User(
                    id = securityUtils.generateUUID(),
                    email = email,
                    passwordHash = securityUtils.hashPassword(password),
                    role = role
                )
                userDao.insertUser(user)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun authenticateUser(email: String, password: String): Result<User> = 
        withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByEmail(email)
                if (user != null && securityUtils.verifyPassword(password, user.passwordHash)) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Неверный email или пароль"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateSecurityCode(userId: String, code: String): Result<Boolean> = 
        withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserById(userId)
                if (user != null) {
                    userDao.updateUser(user.copy(securityCode = securityUtils.hashPassword(code)))
                    Result.success(true)
                } else {
                    Result.failure(Exception("Пользователь не найден"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun verifySecurityCode(userId: String, code: String): Result<Boolean> = 
        withContext(Dispatchers.IO) {
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

    override suspend fun getUserById(id: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(id)
    }

    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
} 