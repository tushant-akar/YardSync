package com.example.yardsync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    @SerialName("id")
    val id: String,
    @SerialName("emp_id")
    val empId: String,
    @SerialName("name")
    val name: String,
    @SerialName("warehouse")
    val warehouse: String,
    @SerialName("email")
    val email: String,

)
