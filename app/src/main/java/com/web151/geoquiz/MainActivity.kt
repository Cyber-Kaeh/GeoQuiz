package com.web151.geoquiz

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.web151.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_asia, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_africa, false),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_australia, true)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        binding.trueButton.setOnClickListener { view: View ->
             checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
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
            currentIndex = if (currentIndex - 1 < 0) {
                // wrap to last question
                questionBank.size - 1
                // stop at first
//                return@setOnClickListener
            } else {
                (currentIndex - 1) % questionBank.size
            }
            updateQuestion()
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if(userAnswer == correctAnswer){
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
}