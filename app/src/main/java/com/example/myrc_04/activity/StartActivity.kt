package com.example.myrc_04.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myrc_04.MainActivity
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initBinding()

        // Start
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Rank
        binding.btnScore.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
        }

    }
    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}