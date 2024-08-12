package com.example.yardsync.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.yardsync.model.Vehicle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch


class RecordsViewModel: ViewModel() {
    private val _records = MutableLiveData<List<Vehicle>>()
    val records: LiveData<List<Vehicle>> = _records

    init {
        getVehicleRecords()
    }

    private fun getVehicleRecords() {
        viewModelScope.launch {
            try {
                val fetchedRecords = client.from("vehicle").select().decodeList<Vehicle>()
                _records.postValue(fetchedRecords)
            } catch (e: Exception) {
                _records.postValue(emptyList())
                Log.e("RecordsViewModel", "Error fetching records: ${e.message}")
            }
        }
    }
}