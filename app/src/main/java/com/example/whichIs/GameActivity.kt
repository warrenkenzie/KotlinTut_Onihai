package com.example.whichIs

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.whichIs.adapter.GameAdapter
import com.example.whichIs.model.Game
import com.example.whichIs.model.Quiz
import com.example.whichIs.model.QuizImage

class GameActivity : AppCompatActivity(), GameAdapter.OnItemClickListener {

    private var timer: CountDownTimer? = null
    private lateinit var recyclerViewGame: RecyclerView
    private val quizList : ArrayList<Quiz> = initialiseList()
    private var game : Game = Game(quizList,5000)

    private var correctCount: Int=0
    private var wrongCount: Int=0
    private var timeOutCount: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewGame = findViewById(R.id.game_RV)
        val mAdapter = GameAdapter(game.quizList)
        val mLayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)

        // Set the layout manager and adapter on the RecyclerView
        recyclerViewGame.layoutManager = mLayoutManager
        recyclerViewGame.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
        LinearSnapHelper().attachToRecyclerView(recyclerViewGame)

        recyclerViewGame.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

                if (visibleItemPosition != RecyclerView.NO_POSITION) {
                    // An item is fully visible, start the timer
                    startTimer(game.timeForEachQuiz)
                }
            }
        })
    }

    private fun initialiseList() : ArrayList<Quiz>{
        val quiz0 = Quiz(
            ArrayList(listOf(
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/dog.jpeg?alt=media&token=71ad9cf2-32c7-4f2b-a20f-4e908fbebef3","2"),
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/cat.jpeg?alt=media&token=a7103da3-59b3-4faf-9bc5-858b56693cb4", "4"))
            ),0,"comparison","Who has less eyes?")

        val quiz1 = Quiz(
            ArrayList(listOf(
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/bird.jpeg?alt=media&token=9a6d77b1-3265-42ee-8a26-ccb4dcdc0fa2","20000"),
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/cow.jpg?alt=media&token=21f86aac-6da4-4829-919b-93e577f8a9d9", "40000"))
                ),1,"comparison","Who has more followers?")

        val quiz2 = Quiz(ArrayList(listOf(
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/hamster.jpg?alt=media&token=1f1237a6-26d5-4b21-8e7a-eb71692ce592","200"),
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/rat.jpeg?alt=media&token=610a0239-80a3-4a04-bf4a-f045b6e7f0a6", "400"))
        ),1,"comparison","who has more siblings?")

        val quizList : ArrayList<Quiz> = ArrayList()
        quizList.add(quiz0)
        quizList.add(quiz1)
        quizList.add(quiz2)
        return quizList
    }

    fun startTimer(durationMillis: Long) {
        timer?.cancel() // Cancel any existing timer

        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI with remaining time
                val secondsRemaining = millisUntilFinished / 1000
                val timer : TextView = findViewById(R.id.timer)
                timer.text = secondsRemaining.toString()
            }

            override fun onFinish() {
                nextTurn(-1)
            }
        }.start()
    }

    // check if there is a next turn, if there is a turn after the current turn, return the nextItemPosition
    // if its -1, then there is not next turn and its the final quiz
    fun nextTurn(userAnswer: Int) {
        val layoutManager = recyclerViewGame.layoutManager as LinearLayoutManager
        val currentItemPosition = layoutManager.findFirstVisibleItemPosition()
        val nextItemPosition = currentItemPosition + 1

        // check if answer is correct
        if (userAnswer == 0 || userAnswer == 1){

            if(userAnswer == quizList[currentItemPosition].answerPosition){
                // answer is correct
                correctCount += 1
            }else{
                wrongCount += 1
            }
        }else if(userAnswer == -1){
            // when ran out of time
            timeOutCount += 1
        }

        // if there is a next quiz then proceed, if not then go to the results page
        if (nextItemPosition < (recyclerViewGame.adapter?.itemCount ?: 0)) {
            timer?.cancel() // Cancel any existing timer
            recyclerViewGame.smoothScrollToPosition(nextItemPosition)
        }else{
            val intent = Intent(this, EndScreenActivity::class.java)
            intent.putExtra("correctCount", correctCount.toString())
            intent.putExtra("wrongCount", wrongCount.toString())
            intent.putExtra("timeOutCount", timeOutCount.toString())
            startActivity(intent)
        }
    }

    override fun onItemClick(userAnswer: Int) {
        nextTurn(userAnswer)
    }
}