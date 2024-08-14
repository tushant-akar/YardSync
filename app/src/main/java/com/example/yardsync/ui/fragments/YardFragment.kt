package com.example.yardsync.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentYardBinding
import com.example.yardsync.ui.adapter.YardAdapter
import com.example.yardsync.viewModel.YardViewModel

class YardFragment : Fragment() {
    private var _binding: FragmentYardBinding? = null
    private val binding get() = _binding!!
    private val dockList = arrayListOf("Dock No. 1", "Dock No. 2", "Dock No. 3", "Dock No. 4")
    private lateinit var mAdapter: YardAdapter
    private lateinit var viewModel: YardViewModel

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
        mAdapter = YardAdapter(mutableListOf())

        binding.carouselView.apply {
            size = dockList.size
            resource = R.layout.carousel_view_item
            scaleOnScroll = true
            hideIndicator(true)
            setCarouselViewListener { view, position ->
                val textView = view.findViewById<TextView>(R.id.dockNotv)
                textView.text = dockList[position]
                val recyclerView = view.findViewById<RecyclerView>(R.id.yardRv)

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mAdapter
                }
                viewModel.getFilteredVehicleStatus(position + 1)
            }
            show()
        }
        observeVehicleStatus()
    }

    private fun observeVehicleStatus() {
        viewModel.filteredVehicleStatus.observe(viewLifecycleOwner) { vehicleStatusList ->
            mAdapter.setVehicleStatus(vehicleStatusList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}