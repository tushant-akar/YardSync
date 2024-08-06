package com.example.yardsync.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yardsync.R
import com.example.yardsync.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

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

        // Create a Spannable String
        val spanString = SpannableString(binding.textView.text)
        val yellowSpan = ForegroundColorSpan(getColor(R.color.themeColorDark))
        spanString.setSpan(yellowSpan, 11, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView.text = spanString

        // Navigate to Login Activity
        binding.getStartedBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}