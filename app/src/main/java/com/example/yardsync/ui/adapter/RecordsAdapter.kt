package com.example.yardsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.yardsync.R
import com.example.yardsync.model.Vehicle
import com.example.yardsync.databinding.ExpandableCardItemBinding

class RecordsAdapter(
    private var records: MutableList<Vehicle>
) : RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ExpandableCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
          val objective = when (vehicle.objective) {
            0 -> "Loading"
            1 -> "Unloading"
            else -> "Both"
          }
          var isExpanded = false
          binding.vehicleUid.text = vehicle.id
          binding.vehicleNumber.text = vehicle.vehicleNumber
          binding.checkIn.text = vehicle.timeIn.toString()
          binding.checkOut.text = vehicle.timeOut.toString()
          binding.dockNo.text = vehicle.dockNo.toString()
          binding.parkingLot.text = vehicle.parkingLot
          binding.objective.text = objective
          binding.vehicleImage.load(vehicle.vehicleImageUrl) {
              crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
          }
          binding.card.setOnClickListener {
              if (isExpanded) {
                    val collapseAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.collapse)
                    collapseAnim.setAnimationListener(object: Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}
                        override fun onAnimationEnd(animation: Animation?) {
                            binding.container2.visibility = View.GONE
                        }
                        override fun onAnimationRepeat(animation: Animation?) {}
                    })
                    binding.container2.startAnimation(collapseAnim)
                } else {
                    binding.container2.visibility = View.VISIBLE
                    val expandAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.expand)
                    binding.container2.startAnimation(expandAnim)
              }
              isExpanded = !isExpanded
          }
          binding.expandButton.setOnClickListener {
              if (isExpanded) {
                  val collapseAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.collapse)
                  collapseAnim.setAnimationListener(object: Animation.AnimationListener {
                      override fun onAnimationStart(animation: Animation?) {}
                      override fun onAnimationEnd(animation: Animation?) {
                          binding.container2.visibility = View.GONE
                      }
                      override fun onAnimationRepeat(animation: Animation?) {}
                  })
                  binding.container2.startAnimation(collapseAnim)
              } else {
                  binding.container2.visibility = View.VISIBLE
                  val expandAnim = AnimationUtils.loadAnimation(binding.root.context, R.anim.expand)
                  binding.container2.startAnimation(expandAnim)
              }
              isExpanded = !isExpanded
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExpandableCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }

    fun setRecords(records: List<Vehicle>) {
        this.records = records.toMutableList()
        notifyDataSetChanged()
    }
}