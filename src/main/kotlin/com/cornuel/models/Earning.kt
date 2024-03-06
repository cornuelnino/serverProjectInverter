package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class Earning(var date: String? = null, var euro: Double? = null, var kilowatter: Double? = null)