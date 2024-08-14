package com.example.yardsync.viewModel

import androidx.lifecycle.ViewModel
import com.example.yardsync.model.VehicleNavArgs

class VehicleRegisterationViewModel: ViewModel() {
    lateinit var vehicle: VehicleNavArgs
    lateinit var vehicleImageUri: String
}