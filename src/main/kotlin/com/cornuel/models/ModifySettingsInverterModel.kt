package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
class ModifySettingsInverterModel(
    val iduser: Int? = null,
    val outputSourcePriority: Int? = null,
)