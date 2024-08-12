package com.example.yardsync.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentScanBinding
import com.example.yardsync.databinding.YardDialogBinding
import com.example.yardsync.model.Vehicle
import com.example.yardsync.model.VehicleStatus
import com.example.yardsync.utils.Supabase.client
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            lifecycleScope.launch {
                val vehicleNumber = it.text
                fetchDataFromSupabase(vehicleNumber)
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private suspend fun fetchDataFromSupabase(vehicleNumber: String) {
        try {
            val vehicle = client.from("vehicle").select {
                filter {
                    eq("vehicle_no", vehicleNumber)
                }
            }.decodeSingle<Vehicle>()

            showDialogBox(vehicle)
        } catch (e: Exception) {
            showToast("${e.message}")
        }
    }

    private suspend fun showDialogBox(vehicle: Vehicle) {
        val dialogBinding = YardDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext()).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()

        val status = fetchVehicleStatus(vehicle)
        status?.let {
            dialogBinding.vehicleNumber.text = it.vehicleNo
            dialogBinding.status.text = getStatusText(it)
            dialogBinding.selectTime.setOnClickListener {
                picker.show(parentFragmentManager, "ScanFragment")
                picker.addOnPositiveButtonClickListener {
                    dialogBinding.selectTime.text = "${picker.hour}:${picker.minute}"
                }
            }
            dialogBinding.okBtn.setOnClickListener {
                updateStatus(vehicle, status.currentStep + 1)
                dialog.dismiss()
            }
        } ?: showToast("No Status Found")

        dialog.show()
    }

    private suspend fun fetchVehicleStatus(vehicle: Vehicle): VehicleStatus? {
        return try {
            client.from("vehicle_status").select {
                filter {
                    eq("vehicle_no", vehicle.vehicleNumber)
                }
            }.decodeSingle<VehicleStatus>()
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
            null
        }
    }

    private fun getStatusText(status: VehicleStatus): String {
        return when {
            status.totalStep == 8 -> when (status.currentStep) {
                0 -> "Vehicle Parking In"
                1 -> "Vehicle Parking Out"
                2 -> "Vehicle Dock In"
                3 -> "Unloading Start Time"
                4 -> "Unloading End Time"
                5 -> "Loading Start Time"
                6 -> "Loading End Time"
                else -> "Vehicle Dock Out"
            }
            else -> when (status.currentStep) {
                0 -> "Vehicle Parking In"
                1 -> "Vehicle Parking Out"
                2 -> "Vehicle Dock In"
                3 -> "Operation Start Time"
                4 -> "Operation End Time"
                else -> "Vehicle Dock Out"
            }
        }
    }

    private fun updateStatus(vehicle: Vehicle, currentStep: Int) {
        lifecycleScope.launch {
            try {
                client.from("vehicle_status").update(
                    {
                        set("current_step", currentStep)
                    }
                ) {
                    filter {
                        eq("vehicle_no", vehicle.vehicleNumber)
                    }
                }
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
