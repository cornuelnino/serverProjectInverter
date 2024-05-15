package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AllValues(
    var iduser: Int? = null,
    var email: String? = null,
    var name: String? = null,
    var isAdmin: Boolean? = null,
    var createdAt: String? = null,

    var inverterName: String? = null,
    var macAddress: String? = null,
    var position: String? = null,
    var isInverterOnline: Boolean? = null,
    var batteryPercentage: Int? = null,
    var outputActivePower: Double? = null,
    var outputVoltage: Double? = null,

    val outputSourceDirectory: Int? = null,

    val inverterFault: Boolean? = null,
    val lineFail: Boolean? = null,
    val voltageTooLow: Boolean? = null,
    val voltageTooHigh: Boolean? = null,
    val overTemperature: Boolean? = null,
    val fanLocked: Boolean? = null,
    val batteryLowAlarm: Boolean? = null,
    val softFail: Boolean? = null,
    val batteryLowToCharge: Boolean? = null

)
