package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
class ModifyInverterModel(
    val macAddress: String,
    val name: String,
    val position: String,
    val isOnline: Boolean,
    val batteryPercentage: Int,
    val outputActivePower: Double,
    val outputVoltage: Double
)