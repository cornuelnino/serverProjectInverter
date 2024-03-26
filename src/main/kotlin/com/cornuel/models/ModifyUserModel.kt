package com.cornuel.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
class ModifyUserModel(
    val iduser: Int,
    val email: String,
    val name: String,
    val password: String,
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
    fun isValidCredentials(): Boolean {
        return email.length >= 3 && password.length >= 5
    }
}