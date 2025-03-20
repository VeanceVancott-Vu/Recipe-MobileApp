package com.example.dacs_3.model

data class User(

    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val dayJoin: Long = System.currentTimeMillis()

)
