package com.example.gitstarscounter.ui.screens.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.service.StarWorker
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import com.omegar.mvp.presenter.InjectPresenter
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : BaseActivity(), LoginView {
    companion object {
        const val LABEL = "user_name"
        const val TAG = "LoginActivity"

    }

    private val findButton: Button by bind(R.id.button_find_rep)
    private val repositoryOmegaRecycleView: OmegaRecyclerView by bind(R.id.recycler_repositories)
    private val noInternetTextView: TextView by bind(R.id.text_view_no_internet_login)
    private val limitedTextView: TextView by bind(R.id.text_view_limited_resource_login)

    private lateinit var repositoriesAdapter: LoginAdapter
    private var pageNumber = 1

    @InjectPresenter
    override lateinit var presenter: LoginPresenter

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onRepositoryClickListener: LoginAdapter.OnRepositoryClickListener =
            object : LoginAdapter.OnRepositoryClickListener {
                override fun onRepositoryClick(repository: Repository?) {
                    presenter.responseToOpenStars(applicationContext, repository)
                }
            }
        repositoriesAdapter = LoginAdapter(this, onRepositoryClickListener)

        val accountNameEditText: EditText? = findViewById(R.id.text_view_repository_name)
        findButton.setOnClickListener {
            val userName = accountNameEditText?.text.toString().trim()
            pageNumber = 1
            presenter.responseToLoadRepositories(userName, pageNumber)
            repositoriesAdapter.setRepositoriesList(mutableListOf())
            repositoryOmegaRecycleView.showProgressPagination()
     //       setNewStarsFinder()
        }

        accountNameEditText?.setImeActionLabel(LABEL, KeyEvent.KEYCODE_ENTER);
        accountNameEditText?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event -> // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

        //OMEGA_R PAGGINATION
        repositoryOmegaRecycleView.setPaginationCallback(object : OnPageRequestListener {
            override fun onPageRequest(page: Int) {
                Log.d("PAGGINATION", "onPageRequest()")// You can load data inside this callback
                pageNumber++
                presenter.responseToLoadMoreRepositories(pageNumber)
            }

            override fun getPagePreventionForEnd(): Int {
                Log.d("PAGGINATION", "getPagePreventionForEnd()")
                return 5
            }
        })
        repositoryOmegaRecycleView.adapter = repositoriesAdapter

        repositoryOmegaRecycleView.layoutManager = LinearLayoutManager(
            applicationContext,
            OrientationHelper.VERTICAL,
            false
        )
        repositoryOmegaRecycleView.hasFixedSize()
    }

    override fun setupRepositoriesList(repositoriesList: List<Repository?>?) {
        repositoriesAdapter.setRepositoriesList(repositoriesList)
        repositoryOmegaRecycleView.isVisible = true
    }

    override fun changeVisibilityOfNoInternetView(visible: Boolean) {
        noInternetTextView.isVisible = visible
    }

    override fun changeVisibilityOfLimitedView(visible: Boolean) {
        limitedTextView.isVisible = visible
    }

    override fun addPagination(repositoriesList: List<Repository>) {
        repositoriesAdapter.addMoreRepositories(repositoriesList)
    }

    override fun endPagination() {
        repositoryOmegaRecycleView.hidePagination();
    }

    private fun hideKeyboard() {
        findButton.performClick()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content)?.windowToken, 0)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("Login", "onUserLeaveHint")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("LoginActivity", "Start Receiver")
        //startFindStarsReceiver()
    }

    private fun setNewStarsFinder() {
        val work = PeriodicWorkRequestBuilder<StarWorker>(1, TimeUnit.MINUTES)
            .build()

        val result = WorkManager
            .getInstance(this)
            .enqueue(work)
    }
}
