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

    // 1 - answer was correct | 0 - answer was wrong | -1 - time out
    private var _wasTheAnswerCorrect: MutableLiveData<Int> = MutableLiveData()
    var wasTheAnswerCorrect: LiveData<Int> = _wasTheAnswerCorrect

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
    }

    fun getQuestion(): String{
        return game.quizList[_turnCount.value!!].prompt
    }

    fun getCurrentQuiz(): Quiz{
        return game.quizList[_turnCount.value!! - 1]
    }

    fun getCurrentQuizAnswer(position:Int): String{
        val picName = getCurrentQuiz().imageUrlAnswer[position].picName
        val value = getCurrentQuiz().imageUrlAnswer[position].quizAnswer
        val metric = getCurrentQuiz().answerType

        return String.format("$picName => $value$metric")
    }

    // 1 - answer was correct | 0 - answer was wrong | -1 - time out
    fun getWasAnswerCorrect(): String{
        return when (_wasTheAnswerCorrect.value) {
            1 -> "Correct"
            0 -> "Wrong"
            -1 -> "You had enough time..."
            else -> "Error: 404"
        }
    }

    /* There are 3 userAnswer, correct - 1 | wrong - 0 */
    /* Returns Boolean value, if true, the game continues, if false its over */
    fun nextTurn(userAnswer: Int):Boolean {
        // if there is a next quiz then proceed, if not then go to the results page
        if (trueIfThereAreStillQuizzes()) {
            // check if answer is correct
            if (userAnswer == 0 || userAnswer == 1) {
                if (userAnswer == game.quizList[getCurrentTurnCount()].answerPosition)
                {
                    _correctCount.value = _correctCount.value?.plus(1)
                    _wasTheAnswerCorrect.value = 1
                }
                else
                {
                    _wrongCount.value = _wrongCount.value?.plus(1)
                    _wasTheAnswerCorrect.value = 0
                }
            } else if (userAnswer == -1) {
                // when ran out of time
                _timeOutCount.value = _timeOutCount.value?.plus(1)
                _wasTheAnswerCorrect.value = -1
            }
            increaseTurnCount()
            return true
        }else{
            return false
        }
    }
}

