package com.example.shopapp.domain.repository

import com.example.shopapp.domain.model.Address
import com.example.shopapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): User?
    fun getUserById(id: String): Flow<User?>
    suspend fun updateUser(user: User)
    suspend fun updateUserAddresses(userId: String, addresses: List<Address>)
    suspend fun addUserAddress(userId: String, address: Address)
    suspend fun updateDefaultAddress(userId: String, addressId: String)
    suspend fun deleteUserAddress(userId: String, addressId: String)
} 