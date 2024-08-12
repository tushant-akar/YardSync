package com.example.yardsync.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentDriverOutgoingBinding
import com.example.yardsync.model.Driver
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class DriverOutgoingFragment : Fragment() {
    private var _binding: FragmentDriverOutgoingBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DriverOutgoingFragmentArgs>()
    private lateinit var driverID: String
    private lateinit var vehicleID: String
    private lateinit var driver: Driver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverOutgoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        driverID = args.driverID
        vehicleID = args.vehicleID

        fetchDriverDetails()
        binding.driverImage.setImageURI(driver.driverPhoto.toUri())
        binding.edtName.setText(driver.driverName)
        binding.edtPhone.setText(driver.driverPhone)
        binding.edtID.setText(driverID)
        binding.edtLicense.setText(driver.driverLicenseNumber)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driverOutgoingFragment_to_mainActivity2)
        }

        binding.continueBtn.setOnClickListener {
            val action = DriverOutgoingFragmentDirections.actionDriverOutgoingFragmentToCheckingOutFragment(vehicleID)
            findNavController().navigate(action)
        }
    }

    private fun fetchDriverDetails() {
        lifecycleScope.launch {
            try {
                driver = client.from("driver").select {
                    filter {
                        eq("driver_id", driverID)
                    }
                }.decodeSingle()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}