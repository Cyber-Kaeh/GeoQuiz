package com.web151.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean,
                    var isAnswered: Boolean, var isCorrect: Boolean)