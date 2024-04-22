package com.example.whichIs.model

data class Game(
    val gameID:Int,
    val quizList: ArrayList<Quiz>,
    var timeForEachQuiz: Long,
)

class GameRepository{
    /* Generate Test Data */
    fun getGameData():Game{

        val quiz0 = Quiz(1,
            ArrayList(listOf(
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/dog.jpeg?alt=media&token=71ad9cf2-32c7-4f2b-a20f-4e908fbebef3","2"),
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/cat.jpeg?alt=media&token=a7103da3-59b3-4faf-9bc5-858b56693cb4", "4"))
            ),0,"comparison","Who has less eyes?")

        val quiz1 = Quiz(2,
            ArrayList(listOf(
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/bird.jpeg?alt=media&token=9a6d77b1-3265-42ee-8a26-ccb4dcdc0fa2","20000"),
                QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/cow.jpg?alt=media&token=21f86aac-6da4-4829-919b-93e577f8a9d9", "40000"))
            ),1,"comparison","Who has more followers?")

        val quiz2 = Quiz(3,ArrayList(listOf(
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/hamster.jpg?alt=media&token=1f1237a6-26d5-4b21-8e7a-eb71692ce592","200"),
            QuizImage("https://firebasestorage.googleapis.com/v0/b/ohinai-4d90a.appspot.com/o/rat.jpeg?alt=media&token=610a0239-80a3-4a04-bf4a-f045b6e7f0a6", "400"))
        ),1,"comparison","who has more siblings?")

        val quizList : ArrayList<Quiz> = ArrayList()
        quizList.add(quiz0)
        quizList.add(quiz1)
        quizList.add(quiz2)
        return Game(1,quizList,5000)

    }
}
