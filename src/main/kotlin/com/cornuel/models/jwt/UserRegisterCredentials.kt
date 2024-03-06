package com.cornuel.models.jwt

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserRegisterCredentials(
    val email: String,
    val name: String,
    val password: String,
    val macAddress: String
) {

    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
    fun isValidCredentials(): Boolean {
        return email.length >= 3 && password.length >= 5
    }
}