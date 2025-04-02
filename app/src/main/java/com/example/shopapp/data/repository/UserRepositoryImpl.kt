package com.example.shopapp.data.repository

import com.example.shopapp.data.local.dao.UserDao
import com.example.shopapp.data.local.dao.AddressDao
import com.example.shopapp.data.local.entity.UserEntity
import com.example.shopapp.data.local.entity.AddressEntity
import com.example.shopapp.domain.model.User
import com.example.shopapp.domain.model.Address
import com.example.shopapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val addressDao: AddressDao
) : UserRepository {

    override fun getCurrentUser(): User? {
        // В реальном приложении здесь должна быть логика получения текущего пользователя
        // Например, из SharedPreferences или токена
        return null
    }

    override fun getUserById(id: String): Flow<User?> {
        return userDao.getUserById(id).map { userEntity ->
            userEntity?.let { user ->
                addressDao.getUserAddresses(user.id).map { addresses ->
                    user.toUser(addresses)
                }.value
            }
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(UserEntity.fromUser(user))
    }

    override suspend fun updateUserAddresses(userId: String, addresses: List<Address>) {
        addresses.forEach { address ->
            addressDao.insertAddress(AddressEntity.fromAddress(address))
        }
    }

    override suspend fun addUserAddress(userId: String, address: Address) {
        addressDao.insertAddress(AddressEntity.fromAddress(address))
    }

    override suspend fun updateDefaultAddress(userId: String, addressId: String) {
        addressDao.clearDefaultAddress(userId)
        addressDao.setDefaultAddress(addressId)
    }

    override suspend fun deleteUserAddress(userId: String, addressId: String) {
        addressDao.getUserAddresses(userId).map { addresses ->
            addresses.find { it.id == addressId }?.let { address ->
                addressDao.deleteAddress(address)
            }
        }
    }
} 