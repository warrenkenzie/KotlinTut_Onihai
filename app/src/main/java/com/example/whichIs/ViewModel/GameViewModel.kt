package com.example.whichIs.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whichIs.model.Game
import com.example.whichIs.model.GameRepository
import com.example.whichIs.model.Quiz
import com.example.whichIs.model.QuizImage

class GameViewModel : ViewModel() {
    private var  game : Game = GameRepository().getGameData()
    private val timer = MutableLiveData<Long>()

    fun getGameData():Game{
        return game
    }
}

