package com.example.yardsync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    @SerialName("id")
    val id: String,
    @SerialName("driver_id")
    val driverID: String,
    @SerialName("name")
    val driverName: String,
    @SerialName("phone")
    val driverPhone: String,
    @SerialName("image")
    val driverPhoto: String,
    @SerialName("license_no")
    val driverLicenseNumber: String,
)
