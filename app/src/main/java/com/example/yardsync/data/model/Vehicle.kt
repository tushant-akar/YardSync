package com.example.yardsync.data.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle (
    val id: String,
    val vehicleNumber: String,
    val vehicleType: String,
    val incomingWeight: Int,
    val outgoingWeight: Int,
    val timeIn: LocalTime,
    val timeOut: LocalTime,
    val accompaniedPersons: Int,
    val qrCode: String,
    val objective: Int,
    val dockNo: Int,
    val parkingLot: String,
    val vehicleImageUrl: String
)

/*val vehicleList = mutableListOf(
    Vehicle(
        id = "1",
        vehicleNumber = "RJ14 SZ 1234",
        vehicleType = "Truck",
        incomingWeight = 2500,
        outgoingWeight = 2000,
        timeIn = LocalTime.parse("08:15:00"),
        timeOut = LocalTime.parse("12:30:00"),
        accompaniedPersons = 3,
        qrCode = "QR1",
        objective = 1,
        dockNo = 3,
        parkingLot = "A1",
        vehicleImageUrl = "https://example.com/image1.jpg"
    ),
    Vehicle(
        id = "2",
        vehicleNumber = "RJ14 SZ 5678",
        vehicleType = "Van",
        incomingWeight = 1500,
        outgoingWeight = 1200,
        timeIn = LocalTime.parse("09:00:00"),
        timeOut = LocalTime.parse("13:00:00"),
        accompaniedPersons = 2,
        qrCode = "QR2",
        objective = 2,
        dockNo = 1,
        parkingLot = "B2",
        vehicleImageUrl = "https://example.com/image2.jpg"
    ),
    Vehicle(
        id = "3",
        vehicleNumber = "RJ14 SZ 9101",
        vehicleType = "Car",
        incomingWeight = 1000,
        outgoingWeight = 800,
        timeIn = LocalTime.parse("10:00:00"),
        timeOut = LocalTime.parse("14:00:00"),
        accompaniedPersons = 1,
        qrCode = "QR3",
        objective = 1,
        dockNo = 2,
        parkingLot = "C3",
        vehicleImageUrl = "https://example.com/image3.jpg"
    ),
    Vehicle(
        id = "4",
        vehicleNumber = "RJ14 SZ 1122",
        vehicleType = "Truck",
        incomingWeight = 3000,
        outgoingWeight = 2500,
        timeIn = LocalTime.parse("07:30:00"),
        timeOut = LocalTime.parse("11:45:00"),
        accompaniedPersons = 4,
        qrCode = "QR4",
        objective = 3,
        dockNo = 4,
        parkingLot = "D4",
        vehicleImageUrl = "https://example.com/image4.jpg"
    ),
    Vehicle(
        id = "5",
        vehicleNumber = "RJ14 SZ 3344",
        vehicleType = "Van",
        incomingWeight = 1600,
        outgoingWeight = 1400,
        timeIn = LocalTime.parse("08:45:00"),
        timeOut = LocalTime.parse("13:15:00"),
        accompaniedPersons = 2,
        qrCode = "QR5",
        objective = 2,
        dockNo = 5,
        parkingLot = "E5",
        vehicleImageUrl = "https://example.com/image5.jpg"
    ),
    Vehicle(
        id = "6",
        vehicleNumber = "RJ14 SZ 5566",
        vehicleType = "Car",
        incomingWeight = 1200,
        outgoingWeight = 1000,
        timeIn = LocalTime.parse("09:30:00"),
        timeOut = LocalTime.parse("15:00:00"),
        accompaniedPersons = 1,
        qrCode = "QR6",
        objective = 1,
        dockNo = 6,
        parkingLot = "F6",
        vehicleImageUrl = "https://example.com/image6.jpg"
    ),
    Vehicle(
        id = "7",
        vehicleNumber = "RJ14 SZ 7788",
        vehicleType = "Truck",
        incomingWeight = 2800,
        outgoingWeight = 2200,
        timeIn = LocalTime.parse("06:30:00"),
        timeOut = LocalTime.parse("10:00:00"),
        accompaniedPersons = 3,
        qrCode = "QR7",
        objective = 3,
        dockNo = 7,
        parkingLot = "G7",
        vehicleImageUrl = "https://example.com/image7.jpg"
    ),
    Vehicle(
        id = "8",
        vehicleNumber = "RJ14 SZ 9900",
        vehicleType = "Van",
        incomingWeight = 1700,
        outgoingWeight = 1300,
        timeIn = LocalTime.parse("11:00:00"),
        timeOut = LocalTime.parse("16:00:00"),
        accompaniedPersons = 2,
        qrCode = "QR8",
        objective = 2,
        dockNo = 8,
        parkingLot = "H8",
        vehicleImageUrl = "https://example.com/image8.jpg"
    ),
    Vehicle(
        id = "9",
        vehicleNumber = "RJ14 SZ 2233",
        vehicleType = "Car",
        incomingWeight = 1100,
        outgoingWeight = 900,
        timeIn = LocalTime.parse("07:00:00"),
        timeOut = LocalTime.parse("12:00:00"),
        accompaniedPersons = 1,
        qrCode = "QR9",
        objective = 1,
        dockNo = 9,
        parkingLot = "I9",
        vehicleImageUrl = "https://example.com/image9.jpg"
    ),
    Vehicle(
        id = "10",
        vehicleNumber = "RJ14 SZ 4455",
        vehicleType = "Truck",
        incomingWeight = 3500,
        outgoingWeight = 3000,
        timeIn = LocalTime.parse("08:00:00"),
        timeOut = LocalTime.parse("13:30:00"),
        accompaniedPersons = 5,
        qrCode = "QR10",
        objective = 3,
        dockNo = 10,
        parkingLot = "J10",
        vehicleImageUrl = "https://example.com/image10.jpg"
    )
)*/