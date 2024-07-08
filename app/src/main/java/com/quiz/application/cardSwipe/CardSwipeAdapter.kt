package com.quiz.application.cardSwipe

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.asynctaskcoffee.cardstack.CardContainerAdapter
import com.asynctaskcoffee.cardstack.pulse
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.quiz.application.R
import com.quiz.application.databinding.CardViewBinding
import com.quiz.application.model.QuestionDataItem
import com.quiz.application.utils.visible
import java.util.Random
import kotlin.math.min

class CardSwipeAdapter(
    private var list: ArrayList<QuestionDataItem>,
    val context: Context,
    private val listener: CardSwipeListener
) :
    CardContainerAdapter() {
    private val random = Random()

    private lateinit var binding: CardViewBinding
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private var answerSelected = false

    private var currentPosition = 0

    override fun getItem(position: Int) = list[position]

    override fun getView(position: Int): View {
        binding = CardViewBinding.inflate(LayoutInflater.from(context))

        binding.questionTxt.text = list[position].questionTxt


        binding.card.setOnTouchListener { _, _ -> true } // Prevent touch events
        binding.card.setBackgroundResource(R.drawable.card_background)


        val buttons = listOf(binding.firstOptionBtn, binding.secondOptionBtn, binding.thirdOptionBtn, binding.fourthOptionBtn)
        // Reset button states
       /* buttons.forEach { button ->
            button.setBackgroundColor(Color.TRANSPARENT)
        }*/
        val currentQuestion = list[position]
        binding.questionTxt.text = currentQuestion.questionTxt
        // Returns a new list with the elements of this list randomly shuffled.
        val optionList = list[position].options.shuffled()

        binding.firstOptionBtn.visible()
        binding.secondOptionBtn.visible()
        binding.thirdOptionBtn.visible()
        binding.fourthOptionBtn.visible()

        binding.firstOptionBtn.text = optionList[0]
        binding.secondOptionBtn.text = optionList[1]
        binding.thirdOptionBtn.text = optionList[2]
        binding.fourthOptionBtn.text = optionList[3]


        // Assuming correctOption should be compared with questionMarkValue
        val correctOption = list[position].questionSplit.questionMarkValue

        buttons.forEach { button ->
            button.setOnClickListener {
                val selectedOption = (it as TextView).text.toString()
                val isCorrect = selectedOption == correctOption
//                it.setBackgroundColor(if (isCorrect) ContextCompat.getColor(context,R.color.green) else ContextCompat.getColor(context,R.color.wrong))
                it.backgroundTintList = (if (isCorrect) ColorStateList.valueOf(context.resources.getColor(R.color.green)) else ColorStateList.valueOf(context.resources.getColor(R.color.wrong)))
                // Disable all buttons after selection
                buttons.forEach { it.isEnabled = false }

                Handler(Looper.getMainLooper()).postDelayed({
                    listener.onAnswerSelected(isCorrect)
//                    updateData(list)
                    swipeCard(position, isCorrect)
                }, 1000)
            }
        }
        return binding.root
    }
    private fun swipeCard(position: Int, isCorrect: Boolean) {
        if (isCorrect) {
            swipeRight()
        } else {
            swipeLeft()
        }
    }
    override fun getCount(): Int {
        return min(list.size,10)
    }
    fun updateData(newList: ArrayList<QuestionDataItem>) {
        list = newList
        // Invalidate the CardContainer to trigger a refresh
        (context as? CardSwipeActivity)?.binding?.cardContainer?.invalidate()
    }
}