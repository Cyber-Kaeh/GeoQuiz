package com.web151.geoquiz

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        // Handle the result
        if(result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questionTextResId = quizViewModel.currentQuestionText
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
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        binding.resetButton.setOnClickListener {
            quizViewModel.reset()
            updateQuestion()
            Snackbar.make(binding.root, "Quiz Reset!", Snackbar.LENGTH_LONG)
                .setTextColor(Color.BLACK)
                .setBackgroundTint(Color.rgb(0, 255, 255))
                .setDuration(5000)
                .show()
        }

        binding.cheatButton.setOnClickListener {
            // start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        if (quizViewModel.checkIsAnswered) {
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        } else {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
        checkQuestions()
    }

    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.setIsAnswered = true
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Snackbar.make(binding.root, getString(messageResId), Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                when {
                    quizViewModel.isCheater -> Color.rgb(255,165, 0)
                    userAnswer == correctAnswer -> Color.rgb(0, 153, 0)
                    else -> Color.rgb(204, 0, 0)
                }
            )
            .setDuration(1500)
            .show()
    }

    private fun checkQuestions() {
        if (quizViewModel.allAnswered) {
            Snackbar.make(binding.root,
                "All questions answered! You got ${calcScore()}% right!",
                Snackbar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.rgb(64, 64, 64))
                .setDuration(5000)
                .show()
        }
    }

    private fun calcScore(): String {
        val formattedPercentage = String.format("%.2f", quizViewModel.percentage)
        Log.d(TAG, "Percentage: $formattedPercentage")
        return formattedPercentage
    }

}