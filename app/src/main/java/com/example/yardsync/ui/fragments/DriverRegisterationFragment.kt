package com.example.yardsync.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentDriverRegisterationBinding
import com.example.yardsync.model.VehicleNavArgs
import com.example.yardsync.viewModel.VehicleRegisterationViewModel
import kotlinx.coroutines.launch

class DriverRegisterationFragment : Fragment() {
    private var _binding: FragmentDriverRegisterationBinding? = null
    private val binding get() = _binding!!
    private lateinit var vehicle: VehicleNavArgs
    private lateinit var vehicleImageUri: String
    private val sharedViewModel: VehicleRegisterationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverRegisterationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicle = sharedViewModel.vehicle
        vehicleImageUri = sharedViewModel.vehicleImageUri


        Log.d("DriverRegisterationFragment", "Vehicle: $vehicle")
        Log.d("DriverRegisterationFragment", "Vehicle Image Uri: $vehicleImageUri")

        val sharedPreferences = requireContext().getSharedPreferences("DriverData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val driverId = sharedPreferences.getString("driverID", "")
        val driverName = sharedPreferences.getString("driverName", "")
        val driverPhone = sharedPreferences.getString("driverPhone", "")
        val driverLicense = sharedPreferences.getString("driverLicenseNumber", "")
        val driverPhoto = sharedPreferences.getString("driverPhoto", "")

        val description = arrayOf("Vehicle", "Driver", "Checking In")
        binding.stateProgressBar.setStateDescriptionData(description)
        binding.stateProgressBar.setStateDescriptionTypeface("font/nunito_medium.ttf")
        binding.stateProgressBar.setStateNumberTypeface("font/nunito_medium.ttf")

        binding.edtID.setText(driverId)
        binding.edtName.setText(driverName)
        binding.edtPhone.setText(driverPhone)
        binding.edtLicense.setText(driverLicense)
        binding.driverImage.load(driverPhoto) {
            crossfade(true)
        }

        binding.qrCodeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driverRegisterationFragment_to_driverQRFragment2)
        }

        binding.continueBtn.setOnClickListener {
            val id = binding.edtID.text.toString().trim()
            val name = binding.edtName.text.toString().trim()
            val license = binding.edtLicense.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()

            if (id.isEmpty() || name.isEmpty() || license.isEmpty() || phone.isEmpty() || binding.driverImage.drawable == null) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val updatedVehicle = VehicleNavArgs(
                vehicleNumber = vehicle.vehicleNumber,
                vehicleType = vehicle.vehicleType,
                incomingWeight = vehicle.incomingWeight,
                accompaniedPersons = vehicle.accompaniedPersons,
                driverID = id
            )
            sharedViewModel.vehicle = updatedVehicle
            findNavController().navigate(R.id.action_driverRegisterationFragment_to_checkingInFragment)
            editor.clear()
            editor.apply()

        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driverRegisterationFragment_to_mainActivity)
            editor.clear()
            editor.apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}