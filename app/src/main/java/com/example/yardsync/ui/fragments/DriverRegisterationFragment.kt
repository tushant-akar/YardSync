package com.example.yardsync.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentDriverRegisterationBinding
import com.example.yardsync.model.Driver
import com.example.yardsync.model.Vehicle
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.launch

class DriverRegisterationFragment : Fragment() {
    private var _binding: FragmentDriverRegisterationBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DriverRegisterationFragmentArgs>()
    private lateinit var driverID: String
    private lateinit var driverName: String
    private lateinit var driverLicense: String
    private lateinit var driverPhone: String
    private var driverImageUri: Uri? = null
    private lateinit var vehicleNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverRegisterationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicleNumber = args.id

        val description = arrayOf("Vehicle", "Driver", "Checking In")
        binding.stateProgressBar.setStateDescriptionData(description)
        binding.stateProgressBar.setStateDescriptionTypeface("fonts/nunito_medium.ttf")
        binding.stateProgressBar.setStateNumberTypeface("fonts/nunito_medium.ttf")

        binding.uploadBtn.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.continueBtn.setOnClickListener {
            driverID = binding.edtID.text.toString().trim()
            driverName = binding.edtName.text.toString().trim()
            driverLicense = binding.edtLicense.text.toString().trim()
            driverPhone = binding.edtPhone.text.toString().trim()

            if (driverID.isEmpty() || driverName.isEmpty() || driverLicense.isEmpty() || driverPhone.isEmpty() || binding.driverImage.drawable == null) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                uploadData()
                val action = DriverRegisterationFragmentDirections.actionDriverRegisterationFragmentToCheckingInFragment(
                    id = vehicleNumber,
                    driverId = driverID
                )
                findNavController().navigate(action)
            }
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driverRegisterationFragment_to_mainActivity)
        }
    }

    private suspend fun uploadData() {
        var imageUrl: String? = null

        if (driverImageUri != null) {
            val imagePath = "driver_image/${System.currentTimeMillis()}_${
                driverID.replace(
                    " ",
                    "_"
                )
            }.jpg"
            client.storage.from("driver_image").upload(imagePath, driverImageUri!!)
            imageUrl = client.storage.from("driver_image").publicUrl(imagePath)
        }
        val driver = Driver(
            driverID = driverID,
            driverName = driverName,
            driverLicenseNumber = driverLicense,
            driverPhone = driverPhone,
            driverPhoto = imageUrl!!,
        )
        client.from("driver").insert(driver)
    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        driverImageUri = uri
        binding.driverImage.setImageURI(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}