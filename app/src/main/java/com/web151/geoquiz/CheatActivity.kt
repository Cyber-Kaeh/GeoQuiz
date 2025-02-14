package com.web151.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.web151.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.web151.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.web151.geoquiz.answer_is_true"
const val CHEAT_TOKENS = "com.web151.geoquiz.cheat_tokens"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private var cheatTokens = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        cheatTokens = intent.getIntExtra(CHEAT_TOKENS, 3)

        binding.tokensTextView.text = "Remaining cheat tokens: $cheatTokens"

        binding.showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            binding.answerTextView.setText(answerText)

            if (cheatTokens > 0) {
                cheatTokens -= 1
                binding.tokensTextView.text = "Remaining cheat tokens: $cheatTokens"
            }

            setAnswerShownResult(true, cheatTokens)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean, remainingTokens: Int) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(CHEAT_TOKENS, remainingTokens)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatTokens: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(CHEAT_TOKENS, cheatTokens)
            }
        }
    }
}