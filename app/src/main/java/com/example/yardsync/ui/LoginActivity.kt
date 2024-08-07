package com.example.yardsync.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.yardsync.R
import com.example.yardsync.data.model.EmployeeState
import com.example.yardsync.databinding.ActivityLoginBinding
import com.example.yardsync.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel = AuthViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginBtn.setOnClickListener {
            lifecycleScope.launch {
                signIn()
            }
        }
    }

    private suspend fun signIn() {
        val emailText = binding.email.text.toString().trim()
        val passwordText = binding.password.text.toString()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.signInWithCredentials(emailText, passwordText)

        val employeeState = viewModel.employeeState
        Log.d("LoginActivity", "Employee State: ${employeeState.value}")
        when (employeeState.value) {
            is EmployeeState.Success -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            is EmployeeState.Error -> {
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}