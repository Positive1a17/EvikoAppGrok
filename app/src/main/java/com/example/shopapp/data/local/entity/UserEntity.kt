package com.example.shopapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.shopapp.domain.model.User
import com.example.shopapp.domain.model.Address

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String?
) {
    fun toUser(addresses: List<AddressEntity> = emptyList()): User {
        return User(
            id = id,
            name = name,
            email = email,
            phone = phone,
            addresses = addresses.map { it.toAddress() }
        )
    }

    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                phone = user.phone
            )
        }
    }
}

@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AddressEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val isDefault: Boolean
) {
    fun toAddress(): Address {
        return Address(
            id = id,
            userId = userId,
            street = street,
            city = city,
            postalCode = postalCode,
            country = country,
            isDefault = isDefault
        )
    }

    companion object {
        fun fromAddress(address: Address): AddressEntity {
            return AddressEntity(
                id = address.id,
                userId = address.userId,
                street = address.street,
                city = address.city,
                postalCode = address.postalCode,
                country = address.country,
                isDefault = address.isDefault
            )
        }
    }
} 