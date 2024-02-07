package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddUserModel(
    val email: String? = null,
    val login: String? = null,
    val password: String? = null,
    val isAdmin: Boolean? = null,
    val createdAt: String? = null,
    val macAddress: String? = null,
)
