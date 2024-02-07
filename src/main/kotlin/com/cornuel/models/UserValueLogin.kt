package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class UserValueLogin(
    var user_id: Int? = null,
    var isAdmin: Boolean? = null
)
