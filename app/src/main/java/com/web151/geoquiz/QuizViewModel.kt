package com.web151.geoquiz

import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CHEAT_TOKENS_KEY = "CHEAT_TOKENS_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    val questionBank = listOf(
        Question(R.string.question_asia, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_africa, false),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_australia, true)
    )

    var cheatTokens: Int
        get() = savedStateHandle.get(CHEAT_TOKENS_KEY) ?: 3
        set(value) = savedStateHandle.set(CHEAT_TOKENS_KEY, value)

//    var isCheater: Boolean
//        get() = savedStateHandle.get("${IS_CHEATER_KEY}_$currentIndex") ?: false
//        set(value) = savedStateHandle.set("${IS_CHEATER_KEY}_$currentIndex", value)

    val isCheater: Boolean
        get() = questionBank[currentIndex].isCheated

    var setIsCheated: Boolean
        get() = questionBank[currentIndex].isCheated
        set(value) { questionBank[currentIndex].isCheated = value }

    var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY)?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val checkIsAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered

    var setIsAnswered: Boolean = false
        set(value) {
            questionBank[currentIndex].isAnswered = value
            field = value
        }

    var setIsCorrect: Boolean = false
        set(value) {
            questionBank[currentIndex].isCorrect = value
            field = value
        }

    val allAnswered: Boolean
        get() = questionBank.all { it.isAnswered }

    val questionBankSize: Int
        get() = questionBank.size

    val correctAnswers: Int
        get() = questionBank.count { it.isCorrect }

    val percentage: Double
        get() = (correctAnswers.toDouble() / questionBankSize) * 100

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex - 1 < 0) {
            // wrap to last question
                questionBank.size - 1
            // stop at first
//            return@setOnClickListener
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    fun useToken(): Boolean {
        return if (cheatTokens > 0) {
            cheatTokens -= 1
            true
        } else {
            false
        }
    }

    fun reset() {
        questionBank.forEach { question ->
            question.isAnswered = false
            question.isCorrect = false
            question.isCheated = false
        }
        currentIndex = 0
        savedStateHandle.set(CURRENT_INDEX_KEY, currentIndex)
        savedStateHandle.set(CHEAT_TOKENS_KEY, 3)
        savedStateHandle.set(IS_CHEATER_KEY, false)
    }

}
