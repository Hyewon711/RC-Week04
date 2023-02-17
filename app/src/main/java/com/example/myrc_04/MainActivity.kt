package com.example.myrc_04

import android.annotation.SuppressLint
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myrc_04.databinding.ActivityMainBinding
import com.example.myrc_04.fragment.BottomSheetFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private val bottomSheetFragment = BottomSheetFragment()
    var bundle = Bundle()

    //뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0

    // 좌표 관련 변수
    private var startX = 0f
    private var startY = 0f

    // 당근 리필
    private var visibilityCount = 0

    // 카운트 타이머 선언
    val mTimer = Timer()
    var currentTime: Long = 121

    // 당근
    private lateinit var carrot1: ImageView
    private lateinit var carrot2: ImageView
    private lateinit var carrot3: ImageView
    private lateinit var carrot4: ImageView
    private lateinit var carrot5: ImageView
    private lateinit var carrot6: ImageView
    private lateinit var carrot7: ImageView
    private lateinit var carrot8: ImageView
    private lateinit var carrot9: ImageView
    private lateinit var carrot10: ImageView
    private var carrot = emptyArray<ImageView>()

    // 각 당근의 시간 count
    private var count = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    // 당근 좌표 저장할 변수
    private var locationCheck = 0
    private var carrotLocation = IntArray(2)
    private var carrotWidth = IntArray(10)
    private var carrotHeight = IntArray(10)

    // thread flag
    private var flag = arrayOf(false, false, false, false, false, false, false, false, false, false)

    // 스코어, 라이프
    var score: Int = 0
    private var life: Int = 5
    var state = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var handler = Handler(Looper.getMainLooper())

    // 효과음
    private lateinit var soundPool: SoundPool
    private var soundBad: Int = 0
    private var soundGood: Int = 0
    private var soundCook: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 데이터 바인딩
        initBinding()

        soundPool = SoundPool.Builder().build()
        soundBad = soundPool.load(this, R.raw.bad, 1)
        soundGood = soundPool.load(this, R.raw.happy, 1)
        soundCook = soundPool.load(this, R.raw.cook, 1)

        carrot1 = binding.carrot1
        carrot2 = binding.carrot2
        carrot3 = binding.carrot3
        carrot4 = binding.carrot4
        carrot5 = binding.carrot5
        carrot6 = binding.carrot6
        carrot7 = binding.carrot7
        carrot8 = binding.carrot8
        carrot9 = binding.carrot9
        carrot10 = binding.carrot10
        carrot = arrayOf(carrot1, carrot2, carrot3, carrot4, carrot5, carrot6, carrot7, carrot8, carrot9, carrot10)

        // 풀 스크린
        fullScreen()
        // 타이머
        timerTask()
        carrotGood()
    }

    /* 레이아웃 설정 */
    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /* 타이머 */
    private fun timerTask() {
        currentTime = binding.time.text.toString().toLong()
        val timerTask = object : TimerTask() {
            override fun run() {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    // 반복실행할 구문
                    currentTime--
                    refill()
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

    /* 당근 */
    @SuppressLint("ClickableViewAccessibility")
    private fun carrotGood() {
        // carrot Touch Event
        for (i in 0 until 10){
            carrot[i].setOnTouchListener { view: View, event: MotionEvent ->
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
                        if (locationCheck == 0) {
                            locationCarrot()
                            locationCheck++
                        } // 최초 1번만 실행

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
                        // 버너 프레임 안에 있는 경우 thread 실행
                        if((binding.frameBurner.x < view.x && view.x < binding.frameBurner.x + binding.frameBurner.width)
                            && (binding.frameBurner.y < view.y && view.y < binding.frameBurner.y + binding.frameBurner.height)){
                            flag[i] = true
                            soundPool.play(soundCook, 1.0f, 1.0f, 0, 0, 1.0f)
                            Thread(Runnable {
                                run {
                                    while (flag[i]) {
                                        try {
                                            handler(i)
                                            Thread.sleep(1000)
                                            count[i]++
                                        } catch (e: InterruptedException) {
                                            break
                                        }
                                    }
                                }
                            }).start()
                        }
                        // 서머에게 준 경우
                        else if((binding.character.x < view.x && view.x < binding.character.x + binding.character.width)
                            && (binding.character.y < view.y && view.y < binding.character.y + binding.character.height)) {
                            view.x = carrotWidth[i].toFloat()
                            view.y = carrotHeight[i].toFloat()
                            Log.d(TAG, "${view.x} ${view.y}")
                            view.visibility = View.INVISIBLE
                            visibilityCount += 1

                            flag[i] = false
                            if (state[i] == 1) {
                                soundPool.play(soundGood, 1.0f, 1.0f, 0, 0, 1.0f)
                                score += 1000
                                binding.score.text = score.toString()+"점"
                                binding.character.setImageResource(R.drawable.ic_character_good)
                            } else {
                                soundPool.play(soundBad, 1.0f, 1.0f, 0, 0, 1.0f)
                                when (life) {
                                    5 -> {
                                        binding.life1.visibility = View.GONE
                                        binding.character.setImageResource(R.drawable.ic_character_bad)
                                        life-- }
                                    4 -> {
                                        binding.life2.visibility = View.GONE
                                        binding.character.setImageResource(R.drawable.ic_character_bad)
                                        life-- }
                                    3 -> { binding.life3.visibility = View.GONE
                                        binding.character.setImageResource(R.drawable.ic_character_bad)
                                        life-- }
                                    2 -> { binding.life4.visibility = View.GONE
                                        binding.character.setImageResource(R.drawable.ic_character_bad)
                                        life-- }
                                    else -> {
                                        binding.life5.visibility = View.GONE
                                        binding.character.setImageResource(R.drawable.ic_character_bad)
                                        life--
                                        for (i in 0 until 10){
                                            flag[i] = false
                                        }
                                        mTimer.cancel()
                                        bottomSheetFragment.setCancelable(false)
                                        bundle.putInt("score", score)
                                        bottomSheetFragment.arguments = bundle
                                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                                    }
                                }
                            }
                        }
                    }
                }
                true
            }
        }
    }

    /* 좌표 구하기 */
    private fun locationCarrot() {
        for (i in 0 until 10) {
            carrot[i].getLocationInWindow(carrotLocation)
            carrotWidth[i] = carrotLocation[0] // x좌표
            carrotHeight[i] = carrotLocation[1] // y좌표
        }
    }

    private fun handler(i: Int) {
        // ui 접근을 위한 handler
        handler.post(object : Runnable {
            override fun run() {
                if (count[i] < 3) {
                    state[i] = 0
                    carrot[i].setImageResource(R.drawable.ic_carrot_default_1)
                } else if (count[i] < 8) {
                    state[i] = 1
                    carrot[i].setImageResource(R.drawable.ic_carrot_default_2)
                } else if (count[i] < 11) {
                    state[i] = 2
                    carrot[i].setImageResource(R.drawable.ic_carrot_default_3)
                } else {
                    state[i] = 3
                    carrot[i].setImageResource(R.drawable.ic_carrot_default_4)
                }
            }
        })
    }

    /* 당근 리필 */
    private fun refill() {
        if (visibilityCount == 10) {
            for (i in 0 until 10) {
                // 당근 상태, 당근 시간, thread flag 모두 초기화
                carrot[i].visibility = View.VISIBLE
                carrot[i].setImageResource(R.drawable.ic_carrot_default_1)
                count[i] = 0
                flag[i] = false
                state[i] = 0
            }
            visibilityCount = 0
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

    /* onBackPressed 메서드 */
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finishAffinity() //액티비티 종료
        }
        mBackWait = System.currentTimeMillis()
    }

    override fun onResume() {
        Log.d(TAG, "MainActivity - onResume() called")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "MainActivity - onPause() called")
        super.onPause()
    }


    override fun onStop() {
        super.onStop()

        Thread(Runnable {
            run {
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) { }
            }
        }).start()
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
