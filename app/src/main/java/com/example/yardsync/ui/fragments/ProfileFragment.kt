package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentProfileBinding
import com.example.yardsync.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel = AuthViewModel(requireContext())
        retrieveEmployee()

        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_profileFragment_to_landingActivity)
        }
        return binding.root
    }

    private fun retrieveEmployee() {
        viewLifecycleOwner.lifecycleScope.launch {
            val emp = viewModel.getEmployee(requireContext())
            binding.edtName.setText(emp?.name)
            binding.edtEmail.setText(emp?.email)
            binding.edtWarehouse.setText(emp?.warehouse)
            binding.edtID.setText(emp?.empId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}