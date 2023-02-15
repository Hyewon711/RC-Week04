package com.example.myrc_04.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        initBinding()

        // Rank
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

    }

    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}