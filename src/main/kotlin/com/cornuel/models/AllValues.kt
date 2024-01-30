package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AllValues(
    var user_id: Int? = null,
    var login: String? = null,
    var createdAt: String? = null,

    var inverterName: String? = null,
    var isInverterOnline: Boolean? = null,

    var kilowatters: Int? = null,
    var volts: Int? = null,
    var batteryPercentage: Int? = null,

    var earningPerHour: Int? = null,
    var earningPerDay: Int? = null,
    var earningPerMonth: Int? = null,

    var settings1: String? = null,
    var settings2: String? = null,
    var settings3: String? = null,

    var warning1: String? = null,
    var warning2: String? = null,
    var warning3: String? = null,

)
