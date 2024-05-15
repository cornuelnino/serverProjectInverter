package com.cornuel.database

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val url: String,
    val bdd: String,
    val username: String,
    val password: String
)

