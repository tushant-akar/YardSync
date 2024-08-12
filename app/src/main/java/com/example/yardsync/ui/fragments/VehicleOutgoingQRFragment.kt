package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.yardsync.databinding.FragmentVehicleOutgoingQRBinding
import kotlinx.coroutines.launch

class VehicleOutgoingQRFragment : Fragment() {
    private var _binding: FragmentVehicleOutgoingQRBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleOutgoingQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            lifecycleScope.launch {
                val vehicleNumber = it.text
                val action =
                    VehicleOutgoingQRFragmentDirections.actionVehicleOutgoingQRFragmentToVehicleOutgoingFragment(
                        vehicleNumber
                    )
                findNavController().navigate(action)
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
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