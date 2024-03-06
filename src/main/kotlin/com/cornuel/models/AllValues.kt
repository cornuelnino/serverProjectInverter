package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AllValues(
    var iduser: Int? = null,
    var email: String? = null,
    var name: String? = null,
    var createdAt: String? = null,

    var inverterName: String? = null,
    var macAddress: String? = null,
    var position: String? = null,
    var isInverterOnline: Boolean? = null,

    var batteryPercentage: Int? = null,

    var gridVoltage: String? = null,
    var gridPercentage: String? = null,
    var ACoutputVoltage: String? = null,
    var ACoutputFrequency: String? = null,
    var ACoutputApparentPower: String? = null,

    var ACoutputActivePower: String? = null,
    var BUSvoltage: String? = null,
    var batteryVoltage: String? = null,

    var batteryChargingCurrent: String? = null,
    var batteryCapacity: String? = null,
    var inverterHeatSinkTemperature: String? = null,
    var PVinputCurrent: String? = null,
    var PVinputVoltage: String? = null,
    var batteryVoltageSCC: String? = null,
    var batteryDischargeCurrent: String? = null,
    var deviceStatus: String? = null,

    var lineFail: Boolean? = null,
    var OPVShort: Boolean? = null,
    var batteryLowAlarm: Boolean? = null,
    var EEPROMdefault: Boolean? = null,
    var powerLimit: Boolean? = null,

    var highPVvoltage: Int? = null,
    var MPPTOverloadFault: Int? = null,
    var MPPTOverloadWarning: Int? = null,
    var batteryLowToCharge: Int? = null

)
