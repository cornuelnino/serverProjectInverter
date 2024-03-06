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

    val gridVoltage: String? = null,
    val gridFrequency: String? = null,
    val ACoutputVoltage: String? = null,
    val ACoutputFrequency: String? = null,
    val ACoutputApparentPower: String? = null,
    val ACoutputActivePower: String? = null,
    val BUSvoltage: String? = null,
    val batteryVoltage: String? = null,
    val batteryChargingCurrent: String? = null,
    val batteryCapacity: String? = null,
    val inverterHeatSinkTemperature: String? = null,
    val PVinputCurrent: String? = null,
    val PVinputVoltage: String? = null,
    val batteryVoltageSCC: String? = null,
    val batteryDischargeCurrent: String? = null,
    val deviceStatus: String? = null,

    val lineFail: Boolean? = null,
    val OPVShort: Boolean? = null,
    val batteryLowAlarm: Boolean? = null,
    val EEPROMdefault: Boolean? = null,
    val powerLimit: Boolean? = null,
    val highPVvoltage: Int? = null,
    val MPPTOverloadFault: Int? = null,
    val MPPTOverloadWarning: Int? = null,
    val batteryLowToCharge: Int? = null,
)
