package com.quiz.application.cardSwipe

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.asynctaskcoffee.cardstack.CardListener
import com.asynctaskcoffee.cardstack.pulse
import com.asynctaskcoffee.cardstack.px
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quiz.application.R
import com.quiz.application.activity.LevelActivity
import com.quiz.application.databinding.ActivityHomeBinding
import com.quiz.application.model.QuestionDataItem
import com.quiz.application.utils.CustomCountdownTimer
import com.quiz.application.utils.LoadJsonFromAssets
import java.text.DecimalFormat
import kotlin.math.roundToInt


class CardSwipeActivity : AppCompatActivity(), CardListener, CardSwipeListener {
    lateinit var binding: ActivityHomeBinding

    private lateinit var adapter: CardSwipeAdapter
    private var questionList: ArrayList<QuestionDataItem> = arrayListOf()
    private lateinit var questionTimer: CustomCountdownTimer

    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var currentQuestionIndex = 0


    private val countdownTime = 60 // 1 minute, 60 seconds
    private val clockTime = (countdownTime * 1000).toLong()
    private val progressTime = (clockTime / 1000).toFloat()


    // layout
    lateinit var headerViewLayout: View
    lateinit var footerViewLayout: View
    lateinit var timeTxt: TextView
    lateinit var numofQuestion: TextView
    lateinit var circularProgressBar: ProgressBar

    var typeStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_color)


//        setfullscreen(this)

        // Retrieve the type from the intent and assign it to typeStr
        typeStr = intent.getStringExtra("type")
        Log.d("==TAG", "OptionsSelectActivity onCreate: $typeStr")

        binding.toolbarLayout.tvLevel.text = typeStr
        binding.toolbarLayout.tvLevel.isAllCaps = true
        binding.toolbarLayout.ivBack.setOnClickListener { onBackPressed() }


        // Load and filter questions based on the typeStr
        questionList = loadQuestionsFromAssets("question.json").filter {
            it.type == typeStr
        } as ArrayList<QuestionDataItem>

        Log.d("===TAG", "onCreate: ${questionList.size}")

        binding.cardContainer.setOnCardActionListener(this)
        /*Customization*/
        binding.cardContainer.maxStackSize = 5
        binding.cardContainer.marginTop = 13.px
        binding.cardContainer.margin = 20.px
        binding.cardContainer.setBackgroundResource(android.R.color.transparent)

//        binding.cardContainer.setEmptyView(generateEmptyView())
        binding.cardContainer.addFooterView(generateFooterView())
        binding.cardContainer.addHeaderView(generateHeaderView())

        // set card adapter
        adapter = CardSwipeAdapter(questionList, this, this)
        binding.cardContainer.setAdapter(adapter)

        setupTimer()
        questionTimer.startTimer()

    }

    private fun loadQuestionsFromAssets(filename: String): ArrayList<QuestionDataItem> {
        val jsonStr = LoadJsonFromAssets(filename)
        val allQuestions = Gson().fromJson(
            jsonStr,
            object : TypeToken<ArrayList<QuestionDataItem>>() {}.type
        ) as ArrayList<QuestionDataItem>
        val filteredQuestions = allQuestions.filter { it.type == typeStr }
        return ArrayList(filteredQuestions)
    }

    private fun generateEmptyView(): View {
        return LayoutInflater.from(this).inflate(R.layout.empty_layout, null)
    }

    private fun generateHeaderView(): View {
        headerViewLayout = LayoutInflater.from(this).inflate(R.layout.cardtopview, null)
        numofQuestion = headerViewLayout.findViewById<TextView>(R.id.numofQuestion)
        timeTxt = headerViewLayout.findViewById<TextView>(R.id.timeTxt)
        circularProgressBar = headerViewLayout.findViewById<ProgressBar>(R.id.circularProgressBar)

        return headerViewLayout
    }

    private fun generateFooterView(): View {
        footerViewLayout = LayoutInflater.from(this).inflate(R.layout.cardfooter_view, null)

        val shuffleView = footerViewLayout.findViewById<LinearLayout>(R.id.shuffleView)
        shuffleView.setOnClickListener {
            it.pulse()
            val shuffledQuestions = questionList.take(10).shuffled() + questionList.drop(10)
            adapter = CardSwipeAdapter(shuffledQuestions as ArrayList, this, this)
            binding.cardContainer.setAdapter(adapter)
        }
        return footerViewLayout
    }

    private fun setupTimer() {
        var secondsLeft = 0
        questionTimer = object : CustomCountdownTimer(clockTime, 1000) {}
        questionTimer.onTick = { millisUntilFinished ->
            val second = (millisUntilFinished / 1000.0f).roundToInt()
            if (second != secondsLeft) {
                secondsLeft = second

                timeTxt.text = DecimalFormat("00").format(secondsLeft)
            }
        }
        questionTimer.onFinish = {
            showScoreDialog()
        }
    }

    override fun onAnswerSelected(isCorrect: Boolean) {
        val totalAnswered = correctAnswers + incorrectAnswers

        if (isCorrect) {
            correctAnswers += 1

            Log.d("===TAG", "onAnswerSelected: ${correctAnswers}")
        } else {
            incorrectAnswers += 1
            Log.d("===TAG", "onAnswerSelected: ${incorrectAnswers}")
        }

        numofQuestion.text = "Question ${totalAnswered + 1} / 10"


        // Remove the current question from the list
        questionList.removeAt(currentQuestionIndex)
        Log.d("===isCorrect", "onAnswerSelected: ${currentQuestionIndex}")

        // Update the currentQuestionIndex if necessary
        currentQuestionIndex =
            if (currentQuestionIndex >= questionList.size) 0 else currentQuestionIndex
        Log.d("===TAG", "onAnswerSelected: ${currentQuestionIndex}")

        if (totalAnswered + 1 == 10 || questionList.isEmpty()) {
            showScoreDialog()
            questionTimer.pauseTimer() // Pause the timer
        } else {
            adapter.updateData(questionList)
            Log.d("===", "onAnswerSelected: ${adapter.updateData(questionList)}")
        }
    }

    override fun onAllQuestionsAnswered() {
        showScoreDialog()
    }

    private fun showScoreDialog() {
        questionTimer.destroyTimer()
        if (correctAnswers == 10) {
            val scoreDialog = Dialog(this)
            scoreDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
            scoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            scoreDialog.setCancelable(false)
            scoreDialog.setContentView(R.layout.win_cardview)

            val tvScore = scoreDialog.findViewById<TextView>(R.id.tvScore)
            val tvContinue = scoreDialog.findViewById<TextView>(R.id.tvContinue)

//            tvScore.text="You answered $correctAnswers questions correctly and ${10 - correctAnswers} questions incorrectly."
            tvContinue.setOnClickListener {
                val intent = Intent(this, LevelActivity::class.java)
                startActivity(intent)
                finish()
                scoreDialog.dismiss()
            }

            scoreDialog.show()
        } else {
            val scoreDialog = Dialog(this)
            scoreDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
            scoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            scoreDialog.setCancelable(false)
            scoreDialog.setContentView(R.layout.loose_cardview)

            val tvScore = scoreDialog.findViewById<TextView>(R.id.tvScore)
            val tvTrayAgain = scoreDialog.findViewById<TextView>(R.id.tvTrayAgain)
            val text = "$correctAnswers / 10"
//            tvScore.text="$correctAnswers / 10"
            val spannableText = SpannableString(text)

// Apply red color to the correct answer count
            spannableText.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                text.indexOf("/"),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

// Apply black color to "/10"
            spannableText.setSpan(
                ForegroundColorSpan(Color.BLACK),
                text.indexOf("/"),
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

// Set the SpannableString to your TextView
            tvScore.text = spannableText
//            tvScore.text="You answered $correctAnswers questions correctly and ${10 - correctAnswers} questions incorrectly."
            tvTrayAgain.setOnClickListener {
                val intent = Intent(this, LevelActivity::class.java)
                startActivity(intent)
                finish()
                scoreDialog.dismiss()
            }

            scoreDialog.show()

        }
    }

    override fun onPause() {
        questionTimer.pauseTimer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        questionTimer.resumeTimer()
    }

    override fun onDestroy() {
        questionTimer.destroyTimer()
        super.onDestroy()
    }


    override fun onItemShow(position: Int, model: Any) {
        Log.d(
            "==onItemShow",
            "onItemShow pos: $position model:" + (model as QuestionDataItem).toString()
        )
    }

    override fun onLeftSwipe(position: Int, model: Any) {}

    override fun onRightSwipe(position: Int, model: Any) {}

    override fun onSwipeCancel(position: Int, model: Any) {
        Log.d(
            "==onSwipeCancel",
            "onSwipeCancel pos: $position model:" + (model as QuestionDataItem).toString()
        )
    }

    override fun onSwipeCompleted() {
        Log.d("==onSwipeCompleted", "Out of swipe data")
    }

}