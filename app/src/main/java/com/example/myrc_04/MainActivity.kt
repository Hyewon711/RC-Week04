package com.example.myrc_04

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.text.TextUtils.replace
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myrc_04.databinding.ActivityMainBinding
import com.example.myrc_04.fragment.BottomSheetFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private val bottomSheetFragment = BottomSheetFragment()
    var bundle = Bundle()

    // 좌표 관련 변수
    private var startX = 0f
    private var startY = 0f

    //     카운트 타이머 선언
    val mTimer = Timer()

    // 당근 굽기 카운트
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

    // 스코어, 라이프
    var score: Int = 0
    var life: Int = 5
    var state = 0

    var handler = Handler(Looper.getMainLooper())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 데이터 바인딩
        initBinding()
        // 풀 스크린
        fullScreen()
        // 타이머
        timerTask()

        // 1. carrot Touch Event
        binding.carrot1.setOnTouchListener { view: View, event: MotionEvent ->
            val width: Int = (view.getParent() as ViewGroup).width - view.getWidth()
            val height: Int = (view.getParent() as ViewGroup).height - view.getWidth()
            binding.character.setImageResource(R.drawable.ic_character_default)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 눌렀을 때
                    startX = event.x
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
                    if (view.getX() > width && view.getY() > height) {
                        view.setX(width.toFloat())
                        view.setY(height.toFloat())
                    } else if (view.getX() < 0 && view.getY() > height) {
                        view.setX(width.toFloat())
                        view.setY(height.toFloat())
                    } else if (view.getX() > width && view.getY() < 0) {
                        view.setX(width.toFloat())
                        view.setY(0f)
                    } else if (view.getX() < 0 && view.getY() < 0) {
                        view.setX(0f)
                        view.setY(0f)
                    } else if (view.getX() < 0 || view.getX() > width) {
                        if (view.getX() < 0) {
                            view.setX(0f)
                            view.setY(event.rawY - startY - view.getHeight())
                        } else {
                            view.setX(width.toFloat())
                            view.setY(event.rawY - startY - view.getHeight())
                        }
                    } else if (view.getY() < 0 || view.getY() > height) {
                        if (view.getY() < 0) {
                            view.setX(event.rawX - startX)
                            view.setY(0f)
                        } else {
                            view.setX(event.rawX - startX)
                            view.setY(height.toFloat())
                        }
                    }
                    // ui 접근을 위한 handler
                    handler.post(object : Runnable {
                        override fun run() {
                            if (count1 < 3) {
                                state = 0
                                binding.carrot1.setImageResource(R.drawable.ic_carrot_default_1)
                            } else if (count1 < 8) {
                                state = 1
                                binding.carrot1.setImageResource(R.drawable.ic_carrot_default_2)
                            } else if (count1 < 11) {
                                state = 2
                                binding.carrot1.setImageResource(R.drawable.ic_carrot_default_3)
                            } else {
                                state = 3
                                binding.carrot1.setImageResource(R.drawable.ic_carrot_default_4)
                            }
                        }
                    })
                    Log.d(TAG, "당근 상태 : ${state}")
                    fire1(view.x, view.y, state as Int?, view as ImageView?)
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

    /* 타이머 */
    private fun timerTask() {
        var currentTime: Long = binding.time.text.toString().toLong()
        val timerTask = object : TimerTask() {
            override fun run() {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    // 반복실행할 구문
                    currentTime--
                    Log.d(TAG,"$currentTime")
                    if (currentTime <= 0) {
                        mTimer.cancel()
                        bottomSheetFragment.setCancelable(false)
                        bundle.putInt("score", score)
                        bottomSheetFragment.arguments = bundle
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                        Log.d(TAG,"타이머 종료")
                    }
                    binding.time.text = currentTime.toString()
                }, 0)
            }
        }
        mTimer.schedule(timerTask, 0, 1000)
        Log.d(TAG,"${currentTime}초 타이머 시작")
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

    /* 굽기 메서드 */
    private fun fire1(x: Float, y: Float, state: Int?, v: ImageView?){

        // 버너 프레임 안에 있는 경우 thread 실행
        if((binding.frameBurner.x < x && x < binding.frameBurner.x + binding.frameBurner.width)
            && (binding.frameBurner.y < y && y < binding.frameBurner.y + binding.frameBurner.height)){
            Thread(Runnable {
                run {
                    while (true) {
                        try {
                            Thread.sleep(1000)
                            count1++
                        } catch (e: java.lang.NumberFormatException) {
                            return@run
                        }
                    }
                }
            }).start()
        }
        // 서머에게 준 경우
        else if((binding.character.x < x && x < binding.character.x + binding.character.width)
            && (binding.character.y < y && y < binding.character.y + binding.character.height)) {
            if (v != null) {
                v.visibility = View.GONE
            }

            if (state == 1) {
                score += 1000
                binding.score.text = score.toString()+"점"
                binding.character.setImageResource(R.drawable.ic_character_good)
            } else {
                when (life) {
                    5 -> {
                        binding.life1.visibility = View.GONE
                        binding.character.setImageResource(R.drawable.ic_character_bad)
                        life-- }
                    4 -> {
                        binding.life2.visibility = View.GONE
                        binding.character.setImageResource(R.drawable.ic_character_bad)
                        life-- }
                    3 -> { binding.life2.visibility = View.GONE
                        binding.character.setImageResource(R.drawable.ic_character_bad)
                        life-- }
                    2 -> { binding.life2.visibility = View.GONE
                        binding.character.setImageResource(R.drawable.ic_character_bad)
                        life-- }
                    else -> {
                        binding.life2.visibility = View.GONE
                        binding.character.setImageResource(R.drawable.ic_character_bad)
                        life--
                        bottomSheetFragment.setCancelable(false)
                        bundle.putInt("score", score)
                        bottomSheetFragment.arguments = bundle
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                }
            }
        }
    }

//    /* 단순 카운트다운 */
//    private fun createCountDownTimer(initialMills: Long) =
//        object : CountDownTimer(initialMills, 1000){
//            // 121초 카운트
//            override fun onTick(millisUntilFinished: Long) {
//                binding.time.text = (millisUntilFinished/1000).toString()
//            }
//            // 끝난 경우
//            override fun onFinish() {
//                val intent = Intent(applicationContext, ScoreActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

    override fun onResume() {
        Log.d(TAG, "MainActivity - onResume() called")
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        Log.d(TAG, "MainActivity - onRestart() called")
        super.onRestart()
    }

    override fun onDestroy() {
        mTimer.cancel()
        super.onDestroy()
    }

}