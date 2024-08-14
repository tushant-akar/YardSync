package com.example.yardsync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yardsync.model.Vehicle
import com.example.yardsync.model.VehicleStatus
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class YardViewModel : ViewModel() {

    private val _filteredVehicleStatus = MutableLiveData<List<VehicleStatus>>()
    val filteredVehicleStatus: LiveData<List<VehicleStatus>> = _filteredVehicleStatus

    fun getFilteredVehicleStatus(dockNo: Int) {
        viewModelScope.launch {
            try {
                val vehicleList = fetchVehicles(dockNo)
                val vehicleStatusList = fetchVehicleStatus(vehicleList.map { it.vehicleNumber })
                val filteredList = vehicleStatusList.filter { it.currentStep != it.totalStep }
                _filteredVehicleStatus.postValue(filteredList)
                Log.d("YardViewModel", "Filtered vehicle status: ${_filteredVehicleStatus.value}")
            } catch (e: Exception) {
                _filteredVehicleStatus.postValue(emptyList())
                Log.e("YardViewModel", "Error: ${e.message}")
            }
        }
    }

    private suspend fun fetchVehicles(dockNo: Int): List<Vehicle> {
        return try {
            client.from("vehicle")
                .select()
                {
                    filter {
                        eq("dock_no", dockNo)
                    }
                }
                .decodeList()
        } catch (e: Exception) {
            Log.e("YardViewModel", "Error fetching vehicles: ${e.message}")
            emptyList()
        }
    }

    private suspend fun fetchVehicleStatus(vehicleNumbers: List<String>): List<VehicleStatus> {
        return try {
            client.from("vehicle_status")
                .select()
                {
                    filter {
                        isIn("vehicle_no", vehicleNumbers)
                    }
                }
                .decodeList()
        } catch (e: Exception) {
            Log.e("YardViewModel", "Error fetching vehicle status: ${e.message}")
            emptyList()
        }
    }
}
