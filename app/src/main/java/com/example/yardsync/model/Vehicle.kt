package com.example.yardsync.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Vehicle (
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("vehicle_no")
    val vehicleNumber: String,
    @SerialName("vehicle_type")
    val vehicleType: String,
    @SerialName("incoming_weight")
    val incomingWeight: Int,
    @SerialName("outgoing_weight")
    val outgoingWeight: Int? = null,
    @SerialName("time_in")
    val timeIn: LocalTime? = null,
    @SerialName("time_out")
    val timeOut: LocalTime? = null,
    @SerialName("persons")
    val accompaniedPersons: Int,
    @SerialName("qr_code")
    val qrCode: String? = null,
    @SerialName("objective")
    val objective: Int? = null,
    @SerialName("dock_no")
    val dockNo: Int? = null,
    @SerialName("parking_lot")
    val parkingLot: String? = null,
    @SerialName("vehicle_image")
    val vehicleImageUrl: String,
    @SerialName("driver_id")
    val driverID: String? = null,
    @SerialName("origin")
    val origin: String? = null,
    @SerialName("destination")
    val destination: String? = null,
)
