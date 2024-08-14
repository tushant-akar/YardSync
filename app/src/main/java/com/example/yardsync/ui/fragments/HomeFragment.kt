package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentHomeBinding
import com.example.yardsync.model.Vehicle
import com.example.yardsync.utils.Supabase.client
import com.example.yardsync.viewModel.AuthViewModel
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AuthViewModel(requireContext())
        getEmployeeDetails()
        fetchNumberOfVehicles()

        binding.incomingCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_incomingActivity)
        }

        binding.outgoingCard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_outgoingActivity)
        }

        binding.yardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_yardFragment)
        }
    }

    private fun fetchNumberOfVehicles() {
        lifecycleScope.launch {
            try {
                val vehicle = client.from("vehicle").select().decodeList<Vehicle>()
                val filterVehicle = vehicle.filter {
                    it.timeOut == null
                }
                binding.totalVehicle.text = "You have ${filterVehicle.size} vehicles\nin the yard"
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching vehicles: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getEmployeeDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            val emp = viewModel.getEmployee(requireContext())
            binding.empName.text = emp?.name
            binding.warehouseName.text = emp?.warehouse
        }
    }
}