package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(val email:String?=null,
                     val password:String?=null)