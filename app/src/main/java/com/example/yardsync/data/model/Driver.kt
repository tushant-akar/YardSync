package com.example.yardsync.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val id: String,
    val driverID: String,
    val driverName: String,
    val driverPhone: String,
    val driverPhoto: String,
    val driverLicenseNumber: String,
    val vehicleNumber: String
)
