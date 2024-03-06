package com.cornuel.models.jwt

data class User(
    val iduser: Int,
    val email: String,
    val name: String,
    val password: String,
    val isAdmin: Boolean
)
