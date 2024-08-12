package com.example.yardsync.model

import java.io.Serializable

data class VehicleNavArgs(
    val vehicleNumber: String,
    val vehicleType: String,
    val incomingWeight: Int,
    val outgoingWeight: Int? = null,
    val timeIn: String? = null,
    val timeOut: String? = null,
    val accompaniedPersons: Int,
    val objective: Int? = null,
    val dockNo: Int? = null,
    val parkingLot: String? = null,
    val vehicleImageUrl: String? = null,
    val driverID: String? = null,
    val origin: String? = null,
    val destination: String? = null,
) : Serializable