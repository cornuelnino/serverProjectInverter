package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddValuesInverterModel(
    val kilowatter: Int? = null,
    val volts: Int? = null,
    val batteryPercentage: Int? = null,
    val earnings_id: Int? = null
)
