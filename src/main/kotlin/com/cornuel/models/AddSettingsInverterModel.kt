package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddSettingsInverterModel(
    val un: String? = null,
    val deux: String? = null,
    val trois: String? = null
)
