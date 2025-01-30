package com.web151.geoquiz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.web151.geoquiz.databinding.ActivityMainBinding
import java.util.Locale

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()
    private val questionBank = listOf(
        Question(R.string.question_asia, true, false, false),
        Question(R.string.question_americas, true, false, false),
        Question(R.string.question_africa, false, false, false),
        Question(R.string.question_oceans, true, false, false),
        Question(R.string.question_mideast, false, false, false),
        Question(R.string.question_australia, true, false, false)
    )
    private var currentIndex = 0
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")    // Added log msg
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            checkQuestions()
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            checkQuestions()
        }

        binding.questionTextView.setOnClickListener {
            nextQuestion()
            updateQuestion()
        }

        binding.nextButton.setOnClickListener {
            nextQuestion()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener {
            prevQuestion()
            updateQuestion()
        }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
        if (questionBank[currentIndex].isAnswered) {
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        } else {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
        checkQuestions()
    }

    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        Log.d(TAG, "Current question index: $currentIndex")
        questionBank[currentIndex].isAnswered = true
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false

        val messageResId = if(userAnswer == correctAnswer){
            correctAnswers++
            Snackbar.make(binding.root, "That is correct!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.rgb(0, 153, 0))
                .setDuration(1500)
                .show()
        } else {
            Snackbar.make(binding.root, "Sorry, that is incorrect.", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.rgb(204, 0, 0))
                .setDuration(1500)
                .show()
        }
    }

    private fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    private fun prevQuestion() {
        currentIndex = if (currentIndex - 1 < 0) {
            // wrap to last question
                questionBank.size - 1
            // stop at first
//            return@setOnClickListener
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    private fun checkQuestions() {
        var allAnswered = questionBank.all { it.isAnswered }
        if (allAnswered) {
            Snackbar.make(binding.root,
                "All questions answered! You got ${calcScore()}% right!",
                Snackbar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.rgb(64, 64, 64))
                .setDuration(5000)
                .show()
        }
    }

    private fun calcScore(): String {
        val percentage = (correctAnswers.toDouble() / questionBank.size.toDouble()) * 100
        val formattedPercentage = String.format("%.2f", percentage)
        Log.d(TAG, "Percentage: $formattedPercentage")
        return formattedPercentage
    }

}