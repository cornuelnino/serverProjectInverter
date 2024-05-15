package com.cornuel.models
import kotlinx.serialization.Serializable

@Serializable
data class LocationClient(
    var iduser: Int? = null,
    var position: String? = null
)
