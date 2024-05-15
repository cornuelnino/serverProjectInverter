package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
class ModifyWarningsInverterModel(
    val macAddress: String? = null,

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