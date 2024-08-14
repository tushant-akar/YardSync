package com.example.yardsync.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentDriverQRBinding
import com.example.yardsync.model.Driver
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class DriverQRFragment : Fragment() {
    private var _binding: FragmentDriverQRBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            lifecycleScope.launch {
                val driverID = it.text
                Log.d("DriverQRFragment", "Driver ID: $driverID")
                fetchDataFromSupabase(driverID)

            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private suspend fun fetchDataFromSupabase(driverID: String) {
        try {
            val driver = client.from("driver").select {
                filter {
                    eq("driver_id", driverID)
                }
            }.decodeSingle<Driver>()
            val sharedPreferences =
                requireContext().getSharedPreferences("DriverData", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.apply {
                putString("driverID", driver.driverID)
                putString("driverName", driver.driverName)
                putString("driverPhone", driver.driverPhone)
                putString("driverPhoto", driver.driverPhoto)
                putString("driverLicenseNumber", driver.driverLicenseNumber)
                apply()
            }
            findNavController().navigate(R.id.driverRegisterationFragment)
        } catch (e: Exception) {
            showToast("${e.message}")
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
        super.onDestroyView()
        _binding = null
    }
}