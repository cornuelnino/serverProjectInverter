package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddWarningsInverterModel(
    val wUn: String? = null,
    val wDeux: String? = null,
    val wTrois: String? = null
)
