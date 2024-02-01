package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class AddInverterModel(
    val name: String? = null,
    val macAddress: String? = null,
    val position: String? = null,
    val isOnline: Boolean? = null,

    val values_id: Int? = null,
    var settings_id: Int? = null,
    var warnings_id: Int? = null

)
