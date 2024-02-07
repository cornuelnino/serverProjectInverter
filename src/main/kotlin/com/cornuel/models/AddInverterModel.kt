package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddInverterModel(
    val name: String? = null,
    val macAddress: String? = null,
    val position: String? = null,
    val isOnline: Boolean? = null,
    val kilowatter: Double? = null,
    val volts: Double? = null,
    val batteryPercentage: Int? = null,
    val un: String? = null,
    val deux: String? = null,
    val trois: String? = null,
    val wUn: String? = null,
    val wDeux: String? = null,
    val wTrois: String? = null
)
