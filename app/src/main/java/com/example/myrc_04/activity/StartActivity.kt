package com.example.myrc_04.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.example.myrc_04.MainActivity
import com.example.myrc_04.R
import com.example.myrc_04.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initBinding()
        fullScreen()

        // Start
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Rank
        binding.btnScore.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /* Full Screen */
    private fun fullScreen() {
        // noActionBar를 사용하므로 ActionBar hide는 생략

        // Android 11(R)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)

            val controller = window.insetsController
            if(controller != null) {
                // status bar , navigation 사라지게 하기
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                //특정 행동시 시스템 바가 나타남 (스와이프)
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } // R버전 이하
            else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // 네비게이션, 상태바 사라지게 하기
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        )
            }
        }
    }
}