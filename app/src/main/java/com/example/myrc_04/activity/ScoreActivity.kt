package com.example.myrc_04.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrc_04.App
import com.example.myrc_04.R
import com.example.myrc_04.adapter.RecordAdapter
import com.example.myrc_04.databinding.ActivityScoreBinding
import com.example.myrc_04.helper.PrefHelper
import com.example.myrc_04.helper.PrefHelper.Companion.KEY_DATA
import com.example.myrc_04.helper.PrefHelper.Companion.KEY_PREFS
import com.example.myrc_04.model.Rank
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

class ScoreActivity: AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityScoreBinding
    private lateinit var recordAdapter: RecordAdapter
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        initBinding()
        fullScreen()

        // Preference 초기화작업
        prefHelper = PrefHelper(App.instance)
        // 리사이클러뷰, 어답터 준비
        recordAdapter = RecordAdapter()
        binding.rvScore.adapter = recordAdapter
        loadPref()

        // 리사이클러뷰 레이아웃
        binding.rvScore.setHasFixedSize(false)
        binding.rvScore.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false )
        binding.rvScore.adapter = recordAdapter
        binding.rvScore.addItemDecoration(DividerItemDecoration(App.instance, DividerItemDecoration.VERTICAL))

        // Start btn
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityScoreBinding.inflate(layoutInflater)
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

    /* load Preference */
    @SuppressLint("NotifyDataSetChanged")
    private fun loadPref() {
        val sharedPreferences = getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(KEY_DATA)) {
            val gson = Gson()
            val json = sharedPreferences.getString(KEY_DATA, "")
            try {
                val typeToken = object : TypeToken<ArrayList<Rank>>() {}.type
                recordAdapter.dataSet = gson.fromJson(json, typeToken)
                recordAdapter.notifyDataSetChanged()
                Log.d(TAG, "${recordAdapter.dataSet.size}")

            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            Log.d("debug", "Data loaded")
        }
    }

}