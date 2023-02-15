package com.example.myrc_04.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ActivityScoreBinding

class ScoreActivity: AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        initBinding()
        // Rank
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

    }

    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}