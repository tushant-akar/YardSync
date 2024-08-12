package com.example.yardsync.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.ActivityIncomingBinding
import com.example.yardsync.databinding.ActivityOutgoingBinding

class OutgoingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOutgoingBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOutgoingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.demo)

        navController = findNavController(R.id.outgoing_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.vehicleOutgoingFragment,
                R.id.driverOutgoingFragment,
                R.id.checkingOutFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}