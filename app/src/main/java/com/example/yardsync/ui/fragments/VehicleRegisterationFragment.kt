package com.example.yardsync.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentVehicleRegisterationBinding
import com.example.yardsync.model.Vehicle
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.launch

class VehicleRegisterationFragment : Fragment() {
    private var _binding: FragmentVehicleRegisterationBinding? = null
    private val binding get() = _binding!!
    private lateinit var vehicleNumber: String
    private lateinit var vehicleType: String
    private lateinit var persons: String
    private lateinit var incomingWeight: String
    private var vehicleImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleRegisterationBinding.inflate(inflater, container, false)

        binding.uploadBtn.setOnClickListener {
            pickImage.launch("image/*")
        }

        val description = arrayOf("Vehicle", "Driver", "Checking In")
        binding.stateProgressBar.setStateDescriptionData(description)
        binding.stateProgressBar.setStateDescriptionTypeface("fonts/nunito_medium.ttf")
        binding.stateProgressBar.setStateNumberTypeface("fonts/nunito_medium.ttf")

        binding.continueBtn.setOnClickListener {
            vehicleNumber = binding.edtVehicleNumber.text.toString().trim()
            vehicleType = binding.edtVehicletype.text.toString().trim()
            incomingWeight = binding.edtIncomingWeight.text.toString().trim()
            persons = binding.edtPersons.text.toString().trim()

            if (vehicleNumber.isEmpty() || vehicleType.isEmpty() || persons.isEmpty() || incomingWeight.isEmpty() || binding.vehicleImage.drawable == null) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                uploadData()
                val action =
                    VehicleRegisterationFragmentDirections.actionVehicleRegisterationFragmentToDriverRegisterationFragment(
                        vehicleNumber
                    )
                findNavController().navigate(action)
            }
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleRegisterationFragment_to_mainActivity)
        }

        return binding.root
    }

    private suspend fun uploadData() {
        var imageUrl: String? = null

        if (vehicleImageUri != null) {
            val imagePath = "vehicle_image/${System.currentTimeMillis()}_${
                vehicleNumber.replace(
                    " ",
                    "_"
                )
            }.jpg"
            client.storage.from("vehicle_image").upload(imagePath, vehicleImageUri!!)
            imageUrl = client.storage.from("vehicle_image").publicUrl(imagePath)
        }
        val vehicle = Vehicle(
            vehicleNumber = vehicleNumber,
            vehicleType = vehicleType,
            vehicleImageUrl = imageUrl!!,
            incomingWeight = incomingWeight.toInt(),
            accompaniedPersons = persons.toInt()
        )
        client.from("vehicle").insert(vehicle)
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        vehicleImageUri = uri
        binding.vehicleImage.setImageURI(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}