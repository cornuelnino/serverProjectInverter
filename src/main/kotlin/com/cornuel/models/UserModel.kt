package com.cornuel.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(val username:String?=null,
                     val password:String?=null)