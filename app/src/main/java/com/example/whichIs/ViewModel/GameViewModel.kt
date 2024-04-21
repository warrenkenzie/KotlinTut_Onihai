package com.example.whichIs.ViewModel

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whichIs.EndScreenActivity
import com.example.whichIs.GameActivity
import com.example.whichIs.R
import com.example.whichIs.model.Game
import com.example.whichIs.model.GameRepository
import com.example.whichIs.model.Quiz
import com.example.whichIs.model.QuizImage

class GameViewModel : ViewModel() {
    /* Game Test Data */
    private var  game : Game = GameRepository().getGameData()

    /* Game Stats */
    private var correctCount: Int=0
    private var wrongCount: Int=0
    private var timeOutCount: Int=0

    // MutableLiveData for turnCount
    private var _turnCount: MutableLiveData<Int> = MutableLiveData(0)
    var turnCount: LiveData<Int> = _turnCount

    fun getGameData():Game{
        return game
    }

    fun getCurrentTurnCount(): Int {
        return turnCount.value?.toInt()!!
    }

    fun trueIfThereAreStillQuizzes(): Boolean{
        return (getCurrentTurnCount() <= game.quizList.size-1)
    }

    private fun increaseTurnCount(){
        _turnCount.value = _turnCount.value!!.plus(1)
        Log.w("INCREASE",_turnCount.value.toString())
    }

    /* There are 3 userAnswer, correct - 1 | wrong - 0 */
    /* Returns Boolean value, if true, the game continues, if false its over */
    fun nextTurn(userAnswer: Int):Boolean {
        // if there is a next quiz then proceed, if not then go to the results page
        if (trueIfThereAreStillQuizzes()) {
            // check if answer is correct
            if (userAnswer == 0 || userAnswer == 1) {
                if (userAnswer == game.quizList[getCurrentTurnCount()].answerPosition)
                { correctCount += 1 }
                else
                { wrongCount += 1 }
            } else if (userAnswer == -1) {
                // when ran out of time
                timeOutCount += 1
            }
            increaseTurnCount()
            return true
        }else{
            return false
        }
    }
}

