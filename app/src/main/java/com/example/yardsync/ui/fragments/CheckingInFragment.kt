package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentCheckingInBinding
import com.example.yardsync.model.VehicleStatus
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import java.time.LocalTime

class CheckingInFragment : Fragment() {
    private var _binding: FragmentCheckingInBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CheckingInFragmentArgs>()
    private lateinit var vehicleNumber: String
    private lateinit var driverID: String
    private lateinit var inTime: String
    private val dockNo: Int = (0..4).random()
    private val parkingLot: String = "A"
    private var objective: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckingInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val description = arrayOf("Vehicle", "Driver", "Checking In")
        binding.stateProgressBar.setStateDescriptionData(description)
        binding.stateProgressBar.setStateDescriptionTypeface("fonts/nunito_medium.ttf")
        binding.stateProgressBar.setStateNumberTypeface("fonts/nunito_medium.ttf")

        driverID = args.driverId
        vehicleNumber = args.id

        binding.timeIn.setOnClickListener {
            binding.edtTimeIn.setText(LocalTime.now().toString())
        }

        binding.objective.setItemClickListener { i, item ->
            objective = when (item.text) {
                "Loading" -> 0
                "Unloading" -> 0
                else -> 2
            }
        }

        binding.continueBtn.setOnClickListener {
            inTime = binding.edtTimeIn.text.toString()
            if (inTime.isEmpty() || !binding.objective.isChecked) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                uploadData()
                uploadVehicleStatus()
                val action =
                    CheckingInFragmentDirections.actionCheckingInFragmentToVehicleQRFragment(
                        vehicleNumber = vehicleNumber,
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
        client.from("vehicle")
            .update(
                mapOf(
                    "driver_id" to driverID,
                    "time_in" to inTime,
                    "dock_no" to dockNo,
                    "parking_lot" to parkingLot,
                    "objective" to objective
                )

            ) {
                filter {
                    eq("vehicle_number", vehicleNumber)
                }
            }
        Toast.makeText(requireContext(), "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
    }

    private suspend fun uploadVehicleStatus() {
        val vehicleStatus = VehicleStatus(
            vehicleNo = vehicleNumber,
            totalStep = if (objective == 2) 8 else 6,
        )
        client.from("vehicle_status")
            .insert(vehicleStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}