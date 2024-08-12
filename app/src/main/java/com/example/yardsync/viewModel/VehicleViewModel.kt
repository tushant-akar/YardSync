package com.example.yardsync.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yardsync.model.Vehicle
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class VehicleViewModel : ViewModel() {
    private val _vehicle = MutableLiveData<Vehicle?>()
    val vehicle: LiveData<Vehicle?> get() = _vehicle

    fun uploadVehicleDetails(vehicle: Vehicle, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                client.from("vehicle").insert(vehicle)
                onResult(true, "Details Uploaded Successfully!")
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun fetchVehicleDetails(vehicleNumber: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val fetchedVehicle = client.from("vehicle")
                    .select() {
                        filter {
                            eq("vehicle_no", vehicleNumber)
                        }
                    }.decodeSingleOrNull<Vehicle>()
                _vehicle.postValue(fetchedVehicle)
                onResult(true, null)
            } catch (e: Exception) {
                _vehicle.postValue(null)
                onResult(false, e.message)
            }
        }
    }

    fun updateVehicleDetails(vehicleNumber: String, timeOut: String, outgoingWeight: Int, origin: String, destination: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                client.from("vehicle").update({
                    set("time_out", timeOut)
                    set("outgoing_weight", outgoingWeight)
                    set("origin", origin)
                    set("destination", destination)
                }) {
                    filter {
                        eq("vehicle_no", vehicleNumber)
                    }
                }
                onResult(true, "Details Updated Successfully!")
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
