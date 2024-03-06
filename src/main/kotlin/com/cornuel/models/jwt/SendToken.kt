package com.cornuel.models.jwt

import com.cornuel.validityInMs
import kotlinx.serialization.Serializable

@Serializable
data class SendToken(
    val iduser: Int,
    val email: String,
    val name: String,
    val isAdmin: Boolean,
    val token: String,
    val tokenTimeValid: Int = validityInMs,
)
