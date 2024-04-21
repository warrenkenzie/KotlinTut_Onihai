package com.example.whichIs

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.whichIs.ViewModel.GameViewModel
import com.example.whichIs.adapter.GameAdapter
import com.example.whichIs.databinding.ActivityGameBinding
import com.example.whichIs.utils.curtainTouchListener

class GameActivity : AppCompatActivity(), GameAdapter.OnItemClickListener {
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding
    private lateinit var recyclerViewGame: RecyclerView
    private lateinit var gameAdapter:GameAdapter
    private var isSmoothScrolling:Boolean = false
    private lateinit var curtain:ImageView

    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewGame = binding.gameRV
        curtain = binding.curtain
        this.curtain.setOnTouchListener(curtainTouchListener())
        val mAdapter = GameAdapter(gameViewModel.getGameData().quizList)
        gameAdapter = mAdapter
        val mLayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)

        // Set the layout manager and adapter on the RecyclerView
        recyclerViewGame.layoutManager = mLayoutManager
        recyclerViewGame.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
        LinearSnapHelper().attachToRecyclerView(recyclerViewGame)

        /* when a new turn starts restart the cycle */
        gameViewModel.turnCount.observe(this, Observer { newTurnCount ->
            if(!gameViewModel.trueIfThereAreStillQuizzes()){
                val intent = Intent(this, EndScreenActivity::class.java)
                startActivity(intent)
            }else{
                if(gameViewModel.getCurrentTurnCount() != 0){
                    curtain.visibility = View.VISIBLE
                    gameAdapter.setDisabled_Enabled(gameViewModel.getCurrentTurnCount(),false)
                }
                recyclerViewGame.smoothScrollToPosition(gameViewModel.getCurrentTurnCount())
                startTimer(gameViewModel.getGameData().timeForEachQuiz)

                // Set a scroll listener to track the completion of smooth scroll
                recyclerViewGame.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && isSmoothScrolling) {
                            // Smooth scroll has completed
                            recyclerView.removeOnScrollListener(this) // Remove the listener
                            isSmoothScrolling = false
                            curtain.visibility = View.GONE
                            gameAdapter.setDisabled_Enabled(gameViewModel.getCurrentTurnCount(),true)
                            startTimer(gameViewModel.getGameData().timeForEachQuiz)
                        }
                    }
                })
                // Set the flag to true indicating that a smooth scroll is in progress
                isSmoothScrolling = true
            }
        })
    }

    fun startTimer(durationMillis: Long) {
        timer?.cancel() // Cancel any existing timer

        timer = object : CountDownTimer(durationMillis + 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI with remaining time
                val timer = recyclerViewGame.findViewHolderForAdapterPosition(gameViewModel.getCurrentTurnCount())?.itemView?.findViewById<TextView>(R.id.timer)
                timer?.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                gameViewModel.nextTurn(-1)
            }
        }.start()
    }

    override fun onItemClick(userAnswer: Int) {
        gameViewModel.nextTurn(userAnswer)
    }
}