package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
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

        lifecycleScope.launch {
            fetchDriverDetails()
            binding.driverImage.load(driver.driverPhoto) {
                crossfade(true)
            }
            binding.edtName.text = Editable.Factory.getInstance().newEditable(driver.driverName)
            binding.edtPhone.text = Editable.Factory.getInstance().newEditable(driver.driverPhone)
            binding.edtID.text = Editable.Factory.getInstance().newEditable(driver.driverID)
            binding.edtLicense.text =
                Editable.Factory.getInstance().newEditable(driver.driverLicenseNumber)
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_driverOutgoingFragment_to_mainActivity2)
        }

        binding.continueBtn.setOnClickListener {
            val action =
                DriverOutgoingFragmentDirections.actionDriverOutgoingFragmentToCheckingOutFragment(
                    vehicleID
                )
            findNavController().navigate(action)
        }
    }

    private suspend fun fetchDriverDetails() {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}