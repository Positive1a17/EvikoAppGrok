package com.example.shopapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val securityCode: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) 