package com.example.yardsync.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.yardsync.R
import com.example.yardsync.data.model.EmployeeState
import com.example.yardsync.databinding.ActivityLandingBinding
import com.example.yardsync.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = AuthViewModel(this)

        // Create a Spannable String
        val spanString = SpannableString(binding.textView.text)
        val yellowSpan = ForegroundColorSpan(getColor(R.color.themeColorDark))
        spanString.setSpan(yellowSpan, 10, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView.text = spanString

        binding.getStartedBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.isUserLoggedIn()
                viewModel.employeeState.observe(this@LandingActivity) { state ->
                    when (state) {
                        is EmployeeState.Success -> {
                            if (state.message == "User already logged in!") {
                                Toast.makeText(this@LandingActivity, state.message, Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this@LandingActivity, MainActivity::class.java))
                                finish()
                            } else {
                                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
                                finish()
                            }
                        }

                        else -> {
                            startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}