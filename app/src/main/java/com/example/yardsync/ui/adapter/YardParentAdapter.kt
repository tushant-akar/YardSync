package com.example.yardsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yardsync.databinding.CarouselViewItemBinding
import com.example.yardsync.databinding.ParkingLayoutBinding
import com.example.yardsync.viewModel.YardViewModel

class YardParentAdapter(
    private val dockList: List<Int>,
    private val viewModel: YardViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MainViewHolder(private val binding: CarouselViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dockNo: Int) {
            binding.dockNotv.text = "Dock No. $dockNo"
            val yardChildAdapter = YardChildAdapter(mutableListOf())
            binding.yardRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = yardChildAdapter
            }

            viewModel.getFilteredVehicleStatus(dockNo)
            viewModel.filteredVehicleStatus.observe(lifecycleOwner) { filteredStatusList ->
                yardChildAdapter.updateData(filteredStatusList)
            }
        }
    }

    inner class ParkingViewHolder(private val binding: ParkingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding =
                    CarouselViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainViewHolder(binding)
            }
            1 -> {
                val binding =
                    ParkingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ParkingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return dockList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                (holder as MainViewHolder).bind(dockList[position])
            }
            1 -> {
                (holder as ParkingViewHolder).bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dockList.size) 1 else 0
    }
}
