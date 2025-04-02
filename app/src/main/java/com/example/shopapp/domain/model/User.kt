package com.example.shopapp.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val addresses: List<Address>
)

data class Address(
    val id: String,
    val userId: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val isDefault: Boolean
) 