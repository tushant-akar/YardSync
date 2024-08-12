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

class IncomingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomingBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIncomingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.demo)

        navController = findNavController(R.id.incoming_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.vehicleRegisterationFragment,
                R.id.driverRegisterationFragment,
                R.id.checkingInFragment,
                R.id.vehicleQRFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}