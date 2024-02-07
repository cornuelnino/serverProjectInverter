package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
class AddEarningsModel(
    val macAddress: String? = null,
    val date: String? = null,
    val perHour: Double? = null
)