package com.example.myrc_04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.Motion
import com.example.myrc_04.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding

    // 좌표 관련 변수
    var startX = 0f
    var startY = 0f

    // 카운트 타이머 선언
    val mTimer = Timer()
    private var count1 = 0
    private var count2 = 0
    private var count3 = 0
    private var count4 = 0
    private var count5 = 0
    private var count6 = 0
    private var count7 = 0
    private var count8 = 0
    private var count9 = 0
    private var count10 = 0
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBinding()

        // 1. carrot Touch Event
        binding.carrot1.setOnTouchListener { view: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 눌렀을 때                    startX = event.x
                    startY = event.y

                }
                MotionEvent.ACTION_MOVE -> {
                    // 이동할 때
                    val movedX: Float = event.x - startX
                    val movedY: Float = event.y - startY

                    view.x = view.x + movedX
                    view.y = view.y + movedY
                }
                MotionEvent.ACTION_UP -> {
                    // 놓았을 때
                }
            }
            true
        }
    }


    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPause() {
        Log.d(TAG, "MainActivity - onPause() called")
        super.onPause()
    }

    override fun onRestart() {
        Log.d(TAG, "MainActivity - onRestart() called")
        super.onRestart()
    }
}