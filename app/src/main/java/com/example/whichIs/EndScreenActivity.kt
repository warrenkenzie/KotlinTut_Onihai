package com.example.whichIs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whichIs.ViewModel.GameViewModel

class EndScreenActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val correctCount = intent.getStringExtra("correctCount")
        val wrongCount = intent.getStringExtra("wrongCount")
        val timeOutCount = intent.getStringExtra("timeOutCount")

        val correctTextView : TextView = findViewById(R.id.correctAns)
        val wrongTextView : TextView = findViewById(R.id.wrongAns)
        val timeOutTextView : TextView = findViewById(R.id.timeOutAns)

        correctTextView.text = "Correct Answers: $correctCount"
        wrongTextView.text = "Wrong Answers: $wrongCount"
        timeOutTextView.text = "TimeOuts: $timeOutCount"
    }
}