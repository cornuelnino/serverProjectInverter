package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AllInverterValues(
    var idinverter: Int? = null,

    var inverterName: String? = null,
    var macAddress: String? = null,
    var position: String? = null,
    var isInverterOnline: Boolean? = null,
    var batteryPercentage: Int? = null,
    var outputActivePower: Double? = null,
    var outputVoltage: Double? = null,

    val outputSourcePriority: Int? = null,

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
