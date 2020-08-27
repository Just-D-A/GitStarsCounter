package com.example.gitstarscounter.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Repository
import com.example.gitstarscounter.stars.StarsActivity
import com.github.rahatarmanahmed.cpv.CircularProgressView


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : MvpAppCompatActivity(), LoginView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var findButton: Button
    private lateinit var repositoryRecycleView: RecyclerView

    private lateinit var repositoriesAdapter: RepositoryAdapter

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waitProgressView = findViewById(R.id.progress_view_login) // need login in name??
        findButton = findViewById(R.id.button_find_rep)
        repositoryRecycleView = findViewById(R.id.recycler_repositories)




        var userName = "" // как правильно передавать??
        val accountNameEditText: EditText = findViewById(R.id.text_rep_name)
        findButton.setOnClickListener {
            userName = accountNameEditText.text.toString().trim()
            loginPresenter.loadUser(userName)
        }

        accountNameEditText.setImeActionLabel("user_name", KeyEvent.KEYCODE_ENTER);
        accountNameEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {

                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER ->
                        hideKeyboard()

                }
            }
            false
        }


        val onRepositoryClickListener: RepositoryAdapter.OnRepositoryClickListener =
            object : RepositoryAdapter.OnRepositoryClickListener {
                override fun onRepositoryClick(repository: Repository?) {
                    loginPresenter.openStars(userName, repository)
                }
            }

        repositoriesAdapter = RepositoryAdapter(onRepositoryClickListener)
        repositoryRecycleView.adapter = repositoriesAdapter
        repositoryRecycleView.layoutManager = LinearLayoutManager(
            applicationContext,
            OrientationHelper.VERTICAL,
            false
        )
        repositoryRecycleView.hasFixedSize()
    }


    override fun startLoading() {
        findButton.isVisible = false
        waitProgressView.isVisible = true
    }

    override fun endLoading() {
        findButton.isVisible = true
        waitProgressView.isVisible = false
    }

    override fun setupRepositoriesList(repositoriesList: List<Repository?>?) {
        repositoriesAdapter.setupRepositoriesList(repositoriesList)
        repositoryRecycleView.isVisible = true
    }

    override fun openStars(userName: String, repository: Repository) {
        startActivity(StarsActivity.createIntent(this, userName, repository))
    }

    override fun showError(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        findButton.performClick()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
}