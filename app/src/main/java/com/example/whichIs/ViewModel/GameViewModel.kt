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
    private var _correctCount: MutableLiveData<Int> = MutableLiveData(0)
    var correctCount: LiveData<Int> = _correctCount

    private var _wrongCount: MutableLiveData<Int> = MutableLiveData(0)
    var wrongCount: LiveData<Int> = _wrongCount

    private var _timeOutCount: MutableLiveData<Int> = MutableLiveData(0)
    var timeOutCount: LiveData<Int> = _timeOutCount

    // MutableLiveData for turnCount
    private var _turnCount: MutableLiveData<Int> = MutableLiveData(0)
    var turnCount: LiveData<Int> = _turnCount

    fun getCorrectCount(): Int {
        return correctCount.value?.toInt()!!
    }
    fun getWrongCount(): Int {
        return wrongCount.value?.toInt()!!
    }
    fun getTimeOutCount(): Int {
        return timeOutCount.value?.toInt()!!
    }


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
                { _correctCount.value = _correctCount.value?.plus(1)
                }
                else
                { _wrongCount.value = _wrongCount.value?.plus(1) }
            } else if (userAnswer == -1) {
                // when ran out of time
                _timeOutCount.value = _timeOutCount.value?.plus(1)
            }
            increaseTurnCount()
            return true
        }else{
            return false
        }
    }
}

