package com.example.yardsync.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.yardsync.R
import com.example.yardsync.databinding.ActivityMainBinding
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        setBottomNavigationWithNavController()
    }

    private fun setBottomNavigationWithNavController() {
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.yardFragment,
                R.id.scanFragment,
                R.id.recordsFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val menuItems = arrayOf(
            CbnMenuItem(
                icon = R.drawable.ic_home,
                avdIcon = R.drawable.avd_home,
                destinationId = R.id.homeFragment,
            ),
            CbnMenuItem(
                icon = R.drawable.ic_dock,
                avdIcon = R.drawable.avd_dock,
                destinationId = R.id.yardFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_scan,
                R.drawable.avd_scan,
                R.id.scanFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_records,
                R.drawable.avd_records,
                R.id.recordsFragment,
            ),
            CbnMenuItem(
                R.drawable.ic_profile,
                R.drawable.avd_profile,
                R.id.profileFragment,
            )
        )

        binding.bottomNav.apply {
            setMenuItems(menuItems)
            setupWithNavController(navController)
        }

    }
}