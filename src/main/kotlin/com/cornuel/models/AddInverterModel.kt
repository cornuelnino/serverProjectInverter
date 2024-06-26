package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddInverterModel(
    val name: String? = null,
    val macAddress: String? = null,
    val position: String? = null,
    val isOnline: Boolean? = null,
    val batteryPercentage: Int? = null,
    val outputActivePower: Double? = null,
    val outputVoltage: Double? = null,

    val outputSourcePriority: Int? = null,

    val inverterFault: Boolean? = null,
    val lineFail: Boolean? = null,
    val voltageTooLow: Boolean? = null,
    val voltageTooHigh: Boolean? = null,
    val overTemperature: Boolean? = null,
    val fanLocked: Boolean? = null,
    val batteryLowAlarm: Boolean? = null,
    val softFail: Boolean? = null,
    val batteryTooLowToCharge: Boolean? = null
)
