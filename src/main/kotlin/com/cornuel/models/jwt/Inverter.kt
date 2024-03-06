package com.cornuel.models.jwt

data class Inverter(
    val idinverter: Int,
    val name: String,
    val macAddress: String,
    val position: String,
    val isOnline: Boolean,
    val batteryPercentage: Int,
    val warnings_idwarnings: Int,
    val settings_idsettings: Int,
    val user_iduser: Int,
)
