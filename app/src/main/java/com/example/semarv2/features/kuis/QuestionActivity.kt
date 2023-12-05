package com.example.semarv2.features.kuis

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.semarv2.R
import com.example.semarv2.data.source.local.entity.Soal
import com.example.semarv2.databinding.ActivityQuestionBinding
import com.example.semarv2.databinding.ItemCardLogoutBinding
import com.example.semarv2.databinding.ItemKuisCloseMessageBinding
import com.example.semarv2.features.auth.login.LoginActivity
import com.example.semarv2.features.kuis.Adapter.ContentKuisAdapter
import com.example.semarv2.features.learning.LearnDetailActivity
import com.example.semarv2.features.profile.ProfileFragment
import com.example.semarv2.features.profile.SettingPreference
import com.example.semarv2.features.profile.SettingViewModel
import com.example.semarv2.util.Result
import com.example.semarv2.util.displayToast
import com.example.semarv2.util.factory.SettingsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuestionBinding
    private lateinit var contentKuisAdapter : ContentKuisAdapter
    private lateinit var layoutManager : LinearLayoutManager
    private val viewModel : KuisViewModel by viewModels()
    private lateinit var settingViewModel : SettingViewModel
    private var datasize = 0
    private var currentpositions = 0

    private val settingsName = "settings"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(settingsName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //INIT
        initViewModel()
        contentKuisAdapter = ContentKuisAdapter(settingViewModel)
        getDataQuiz()
        updateScore()
        onClick()
    }

    override fun onBackPressed() {
        showCloseQuizDialog()
    }

    private fun onClick() {
        val quizId = intent.getLongExtra(QUIZ_ID,1)
        updateButtonVisibility(currentpositions)
        binding.btnBackToList.setOnClickListener{
            showCloseQuizDialog()
        }
        binding.btnNext.setOnClickListener{
            if (currentpositions < datasize -1){
                binding.rvContent.smoothScrollToPosition(currentpositions +1)
                currentpositions += 1
                updateButtonVisibility(currentpositions)
            }
        }

        binding.btnSubmit.setOnClickListener{
            if(currentpositions == datasize -1){
                val contents = contentKuisAdapter.getResult()
                val totalQuiz = contents.size
                var totalcorrectAnswer = 0

                for (content in contents){
                    for (option in content.options!!){
                        if (option.is_click == true && option.correct_answer == true){
                            totalcorrectAnswer += 1
                        }
                    }
                }
                val totalScore = totalcorrectAnswer.toDouble() / totalQuiz * 100
                viewModel.updatePassword(totalScore.toLong(),quizId)
                showFinishQuizDialog(quizId,totalScore)
            }
        }

        binding.btnPrev.setOnClickListener{
            if(currentpositions > 0){
                binding.rvContent.smoothScrollToPosition(currentpositions - 1)
                currentpositions -= 1
                updateButtonVisibility(currentpositions)
            }
        }

    }

    private fun showQuizContent(soal: List<Soal>?) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()

        if (soal != null){
            contentKuisAdapter.setData(soal as MutableList<Soal>)
        }

        snapHelper.attachToRecyclerView(binding.rvContent)
        binding.rvContent.layoutManager = layoutManager
        binding.rvContent.adapter = contentKuisAdapter
        datasize = layoutManager.itemCount

        binding.pbProgres.max = datasize -1
        //menampilkan indeks pertama
        var index = "${currentpositions +1} / $datasize"
        binding.tvHalamansoal.text = index


        binding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentpositions = layoutManager.findFirstVisibleItemPosition()
                index = "${currentpositions + 1} / $datasize"
                binding.tvHalamansoal.text = index
                binding.pbProgres.progress = currentpositions
            }
        })

    }

    private fun getDataQuiz(){
        val quizId = intent.getLongExtra(QUIZ_ID, 1)
        viewModel.getKuisById(quizId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbQuiz.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val quizData = result.data
                    showQuizContent(quizData.list_soal)
                    binding.pbQuiz.visibility = View.GONE

                }

                is Result.Error -> {
                    binding.pbQuiz.visibility = View.GONE
                }
            }
        }
    }
    private fun updateScore(){
        lifecycle.coroutineScope.launchWhenCreated{
            viewModel.updateScoreResult.collect{
                if (it.isLoading) {
                    setLoadingVisibility(true)
                }
                if (it.error.isNotBlank()) {
                    setLoadingVisibility(false)
                    displayToast(it.error)
                }
                it.data?.let {
                    setLoadingVisibility(false)
                }
            }
        }
    }

    private fun updateButtonVisibility(position: Int) {
        when (position) {
            0 -> {
                binding.btnPrev.visibility = View.INVISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
            datasize - 1 -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.INVISIBLE
                binding.btnSubmit.visibility = View.VISIBLE
            }
            else -> {
                binding.btnPrev.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
                binding.btnSubmit.visibility = View.INVISIBLE
            }
        }
    }

    private fun showCloseQuizDialog() {
        val dialog = Dialog(this)
        val dialogBinding = ItemKuisCloseMessageBinding.inflate(LayoutInflater.from(this))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvMessage.text = getString(R.string.message_kuis_out)
        dialogBinding.tvSuccess.text = getString(R.string.string_you_are_sure)
        dialogBinding.btnYes.setOnClickListener {
            dialog.dismiss()
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showFinishQuizDialog(quizId : Long,score : Double) {
        val dialog = Dialog(this)
        val dialogBinding = ItemCardLogoutBinding.inflate(LayoutInflater.from(this))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvMessage.text = getString(R.string.string_quiz_finish)

        dialogBinding.btnYes.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(ResultActivity.RESULT, score.toInt())
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setLoadingVisibility(isVisible: Boolean) {
        binding.apply {
            pbQuiz.visibility = if (isVisible) View.VISIBLE else View.GONE
            btnSubmit.isEnabled = !isVisible
            btnPrev.isEnabled = !isVisible
            btnBackToList.isEnabled = !isVisible
        }
    }

    private fun initViewModel() {
        val preferences = SettingPreference.getInstance(this.dataStore)
        settingViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(preferences)
        )[SettingViewModel::class.java]
    }

    companion object{
        const val QUIZ_ID = "quiz_id"
    }
}