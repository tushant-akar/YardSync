package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yardsync.databinding.FragmentRecordsBinding
import com.example.yardsync.ui.adapter.RecordsAdapter
import com.example.yardsync.viewModel.RecordsViewModel

class RecordsFragment : Fragment() {
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recordsAdapter: RecordsAdapter
    private lateinit var recordsViewModel: RecordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        recordsViewModel = RecordsViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        recordsViewModel.records.observe(viewLifecycleOwner) { records ->
            records?.let { recordsAdapter.setRecords(it) }
        }
    }

    private fun setupRecyclerView() {
        recordsAdapter = RecordsAdapter(mutableListOf())
        binding.recyclerView.apply {
            adapter = recordsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}