package com.example.dacs_3.model

import java.util.UUID

//data class Instruction(
//    val id: String = UUID.randomUUID().toString(), // để phân biệt từng bước
//    val stepNumber: Int = 0,
//    val description: String = "",
//    val images: List<String> = emptyList(),
//    val links: List<Link> = emptyList()
//)

data class Instruction(
    val id: String = UUID.randomUUID().toString(), // để phân biệt từng bước
    val stepNumber: Int = 0,
    val description: String = "",
    val images: List<Int> = emptyList(),
    val links: List<Link> = emptyList()
)
