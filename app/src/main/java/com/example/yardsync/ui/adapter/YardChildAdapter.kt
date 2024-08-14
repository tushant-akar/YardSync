package com.example.yardsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yardsync.databinding.YardItemRecyclerViewBinding
import com.example.yardsync.model.VehicleStatus

class YardChildAdapter(
    private var vehicleStatus: List<VehicleStatus>
) : RecyclerView.Adapter<YardChildAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: YardItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicleStatus: VehicleStatus) {
            binding.vehicleNumber.text = vehicleStatus.vehicleNo
            binding.segmentedProgressBar.setSegmentCount(vehicleStatus.totalStep)
            binding.segmentedProgressBar.setCompletedSegments(vehicleStatus.currentStep)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            YardItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vehicleStatus.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vehicleStatus[position])
    }

    fun updateData(newData: List<VehicleStatus>) {
        vehicleStatus = newData
        notifyDataSetChanged()
    }
}
