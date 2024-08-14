package com.example.yardsync.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentCheckingInBinding
import com.example.yardsync.model.Vehicle
import com.example.yardsync.model.VehicleNavArgs
import com.example.yardsync.model.VehicleStatus
import com.example.yardsync.utils.Supabase.client
import com.example.yardsync.viewModel.VehicleRegisterationViewModel
import com.example.yardsync.viewModel.VehicleViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckingInFragment : Fragment() {
    private var _binding: FragmentCheckingInBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: VehicleRegisterationViewModel by activityViewModels()
    private lateinit var vehicle: VehicleNavArgs
    private lateinit var vehicleImageUri: Uri
    private lateinit var inTime: String
    private val dockNo: Int = (1..4).random()
    private val parkingLot: String = "A"
    private var objective: Int = 0
    private lateinit var viewModel: VehicleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckingInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VehicleViewModel::class.java]

        val description = arrayOf("Vehicle", "Driver", "Checking In")
        binding.stateProgressBar.setStateDescriptionData(description)
        binding.stateProgressBar.setStateDescriptionTypeface("font/nunito_medium.ttf")
        binding.stateProgressBar.setStateNumberTypeface("font/nunito_medium.ttf")

        vehicle = sharedViewModel.vehicle
        vehicleImageUri = sharedViewModel.vehicleImageUri.toUri()

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()

        binding.edtTimeIn.setOnClickListener {
            picker.show(parentFragmentManager, "CheckInFragment")
            picker.addOnPositiveButtonClickListener {
                binding.edtTimeIn.setText(String.format("%02d:%02d", picker.hour, picker.minute))
            }
        }

        binding.objective.setItemClickListener { i, item ->
            objective = when (item.text) {
                "Loading" -> 0
                "Unloading" -> 1
                else -> 2
            }
        }

        binding.continueBtn.setOnClickListener {
            inTime = binding.edtTimeIn.text.toString()
            if (inTime.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                uploadData()
                Log.d("CheckingInFragment", "Data uploaded")
                delay(2000)
                uploadVehicleStatus()
                Log.d("CheckingInFragment", "Vehicle status uploaded")
                val action =
                    CheckingInFragmentDirections.actionCheckingInFragmentToVehicleQRFragment(
                        vehicleNumber = vehicle.vehicleNumber,
                        ParkingLot = parkingLot,
                        DockNo = dockNo
                    )
                findNavController().navigate(action)
            }
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_checkingInFragment_to_mainActivity)
        }
    }

    private suspend fun uploadData() {
        var imageUrl: String? = null

        val imagePath = "vehicle_image/${System.currentTimeMillis()}_${
            vehicle.vehicleNumber.replace(
                " ",
                "_"
            )
        }.jpg"
        client.storage.from("vehicle_image").upload(imagePath, vehicleImageUri)
        imageUrl = client.storage.from("vehicle_image").publicUrl(imagePath)

        val updatedVehicle = Vehicle(
            vehicleNumber = vehicle.vehicleNumber,
            vehicleType = vehicle.vehicleType,
            incomingWeight = vehicle.incomingWeight,
            accompaniedPersons = vehicle.accompaniedPersons,
            driverID = vehicle.driverID,
            timeIn = inTime,
            objective = objective,
            dockNo = dockNo,
            parkingLot = parkingLot,
            vehicleImageUrl = imageUrl
        )
        viewModel.uploadVehicleDetails(updatedVehicle) { success, message ->
            if (success) {
                Toast.makeText(requireContext(), "Data uploaded Successfully!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun uploadVehicleStatus() {
        val vehicleStatus = VehicleStatus(
            vehicleNo = vehicle.vehicleNumber,
            totalStep = if (objective == 2) 8 else 6,
            currentStep = 0
        )
        try {
            client.from("vehicle_status").insert(vehicleStatus)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("CheckingInFragment", " ${e.message} ${vehicleStatus.vehicleNo}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}