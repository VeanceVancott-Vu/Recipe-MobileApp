package com.example.dacs_3.model

data class User(

    val userId: String = "",
    var email: String = "",
    var username: String = "",
    var profileImageUrl: String = "",
    var dayJoin: Long = System.currentTimeMillis() // get time when created

)
