package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
class GetEarningsWith2Dates(
    val iduser: Int,
    val dateDebut: String,
    val dateFin: String
)