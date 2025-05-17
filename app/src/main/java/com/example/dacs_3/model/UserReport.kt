package com.example.dacs_3.model

data class UserReport(
    val id:String ="",
    val reportingUserId: String = "",  // 1. ID người báo cáo
    val reportedUserId: String = "",   // 2. ID người bị báo cáo
    val reason: String = "",            // 3. Lý do báo cáo
    val status: String = "Unresolved",
    val date: Long = System.currentTimeMillis() // 4. Thời gian tạo báo cáo (timestamp)
)
