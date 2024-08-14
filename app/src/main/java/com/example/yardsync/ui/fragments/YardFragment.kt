package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yardsync.databinding.CarouselViewItemBinding
import com.example.yardsync.databinding.FragmentYardBinding
import com.example.yardsync.ui.adapter.YardChildAdapter
import com.example.yardsync.ui.adapter.YardParentAdapter
import com.example.yardsync.viewModel.YardViewModel

class YardFragment : Fragment() {
    private var _binding: FragmentYardBinding? = null
    private val binding get() = _binding!!
    private val dockList = arrayListOf(1,2,3,4)
    private lateinit var viewModel: YardViewModel
    private lateinit var mAdapter: YardParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[YardViewModel::class.java]
        mAdapter = YardParentAdapter(dockList, viewModel, viewLifecycleOwner)

        binding.carouselView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}