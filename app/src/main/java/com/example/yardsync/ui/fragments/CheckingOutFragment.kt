package com.example.yardsync.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yardsync.R
import com.example.yardsync.databinding.CheckOutDialogBoxBinding
import com.example.yardsync.databinding.FragmentCheckingOutBinding
import com.example.yardsync.model.VehicleStatus
import com.example.yardsync.utils.Supabase.client
import com.example.yardsync.viewModel.VehicleViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class CheckingOutFragment : Fragment() {
    private var _binding: FragmentCheckingOutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VehicleViewModel
    private lateinit var vehicleID: String
    private lateinit var timeOut: String
    private lateinit var origin: String
    private lateinit var destination: String
    private lateinit var outgoingWeight: String
    private val args by navArgs<CheckingOutFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckingOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vehicleID = args.vehicleID
        viewModel = ViewModelProvider(this)[VehicleViewModel::class.java]

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()

        binding.edtTimeOut.setOnClickListener {
            picker.show(parentFragmentManager, "CheckInFragment")
            picker.addOnPositiveButtonClickListener {
                binding.edtTimeOut.setText(String.format("%02d:%02d", picker.hour, picker.minute))
            }
        }

        binding.continueBtn.setOnClickListener {
            origin = binding.edtOrigin.text.toString().trim()
            destination = binding.edtDestination.text.toString().trim()
            outgoingWeight = binding.edtOutgoingWeight.text.toString().trim()
            timeOut = binding.edtTimeOut.text.toString()

            viewModel.updateVehicleDetails(
                vehicleNumber = vehicleID,
                origin = origin,
                destination = destination,
                outgoingWeight = outgoingWeight.toInt(),
                timeOut = timeOut
            ) { success, message ->
                if (success) {
                    updateStatus()
                    Toast.makeText(
                        requireContext(),
                        "Vehicle $vehicleID checked out successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_checkingOutFragment_to_mainActivity2)
        }
    }

    private fun updateStatus() {
        lifecycleScope.launch {
            try {
                val status = client.from("vehicle_status").select {
                    filter {
                        eq("vehicle_no", vehicleID)
                    }
                }.decodeSingle<VehicleStatus>()
                client.from("vehicle_status").update({
                    set("current_step", status.totalStep)
                }) {
                    filter {
                        eq("vehicle_no", vehicleID)
                    }
                }
                val dialogBinding = CheckOutDialogBoxBinding.inflate(layoutInflater)
                val dialog = Dialog(requireContext()).apply {
                    setContentView(dialogBinding.root)
                    window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setCancelable(true)
                }
                dialogBinding.checkOutBtn.setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_checkingOutFragment_to_mainActivity2)
                }
                dialog.show()
            } catch (e: Exception) {
                Log.e("CheckOut", "${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}