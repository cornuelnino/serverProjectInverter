package com.cornuel.models.jwt

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserLoginCredentials(
    val email: String,
    val password: String,
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun isValidCredentials(): Boolean {
        return email.length >= 3 && password.length >= 5
    }
}