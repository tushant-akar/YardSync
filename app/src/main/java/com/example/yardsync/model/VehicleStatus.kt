package com.example.yardsync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class VehicleStatus(
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("vehicle_no")
    val vehicleNo: String,
    @SerialName("total_step")
    val totalStep: Int,
    @SerialName("current_step")
    val currentStep: Int = 0,
)
