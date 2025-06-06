package com.example.dacs_3.model

data class CooksnapReport(
    val id :String = "",
    val reportingUserId: String = "",    // ID người báo cáo
    val reportedUserId: String = "",     // ID người bị báo cáo (người dùng)
    val reportedCooksnapId: String = "",   // ID món ăn bị báo cáo
    val reason: String = "",              // Lý do báo cáo
    val status: String = "Unresolved",      // Trạng thái (Pending, Processed, v.v)
    val date: Long = System.currentTimeMillis()  // Thời gian tạo báo cáo
)
