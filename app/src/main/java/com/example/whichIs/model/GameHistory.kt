package com.example.whichIs.model

data class GameHistory(
    val gameHistoryID:Int,
    val gameID:Int,
    val correctCount:Int,
    val wrongCount:Int,
    val timeOutCount:Int,
)
