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
import com.example.gitstarscounter.service.RateLimitWorker
import com.example.gitstarscounter.service.StarWorker
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.base.adapters.OmegaAutoAdapter
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import com.omegar.mvp.presenter.InjectPresenter
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : BaseActivity(), LoginView {
    companion object {
        const val LABEL = "user_name"
        const val TAG = "LoginActivity"
        const val PAGINATION_TAG = "PAGINATION"
        const val REPEAT_TIME: Long = 1
    }

    private val findButton: Button by bind(R.id.button_find_rep)
    private val startRepositoryButton: Button by bind(R.id.button_start_repository_screen)
    private val repositoryOmegaRecycleView: OmegaRecyclerView by bind(R.id.recycler_repositories)
    private val noInternetTextView: TextView by bind(R.id.text_view_no_internet_login)
    private val limitedTextView: TextView by bind(R.id.text_view_limited_resource_login)

    private lateinit var loginAdapter: OmegaAutoAdapter<Repository, OmegaAutoAdapter.ViewHolder<Repository>>
    private var pageNumber = 1

    @InjectPresenter
    override lateinit var presenter: LoginPresenter

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginAdapter = OmegaAutoAdapter.create(
            R.layout.cell_login,
            { item -> presenter.responseToOpenStars(applicationContext, item) }
        ) {
            bind(R.id.text_view_login_repository_name, Repository::name)
        }

        val accountNameEditText: EditText? = findViewById(R.id.text_view_login_repository_name)
        findButton.setOnClickListener {
            val userName = accountNameEditText?.text.toString().trim()
            pageNumber = 1
            presenter.responseToLoadRepositories(userName, pageNumber)
            loginAdapter.list = mutableListOf()
            repositoryOmegaRecycleView.showProgressPagination()
        }

        startRepositoryButton.setOnClickListener {
            presenter.responseToStartRepositoryActivity(this)
        }

        accountNameEditText?.setImeActionLabel(LABEL, KeyEvent.KEYCODE_ENTER)
        accountNameEditText?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event -> // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN
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
                Log.d(PAGINATION_TAG, "onPageRequest()")// You can load data inside this callback
                pageNumber++
                presenter.responseToLoadMoreRepositories(pageNumber)
            }

            override fun getPagePreventionForEnd(): Int {
                Log.d(PAGINATION_TAG, "getPagePreventionForEnd()")
                return 5
            }
        })
        repositoryOmegaRecycleView.adapter = loginAdapter

        repositoryOmegaRecycleView.layoutManager = LinearLayoutManager(
            applicationContext,
            OrientationHelper.VERTICAL,
            false
        )
        repositoryOmegaRecycleView.hasFixedSize()

        setNewStarsFinder()//start worker
    }

    override fun setupRepositoriesList(repositoriesList: List<Repository>) {
        loginAdapter.list = repositoriesList
        repositoryOmegaRecycleView.isVisible = true
    }

    override fun changeVisibilityOfNoInternetView(visible: Boolean) {
        noInternetTextView.isVisible = visible
    }

    override fun changeVisibilityOfLimitedView(visible: Boolean) {
        limitedTextView.isVisible = visible
    }

    override fun addPagination(repositoriesList: List<Repository>) {
        loginAdapter.list += repositoriesList
    }

    override fun endPagination() {
        repositoryOmegaRecycleView.hidePagination()
    }

    private fun hideKeyboard() {
        findButton.performClick()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            findViewById<View>(android.R.id.content)?.windowToken,
            0
        )//is MAGIC??
    }

    private fun setNewStarsFinder() {
        val workStars = PeriodicWorkRequestBuilder<StarWorker>(REPEAT_TIME, TimeUnit.HOURS)
            .build()

        val workLimit = PeriodicWorkRequestBuilder<RateLimitWorker>(REPEAT_TIME, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueue(workLimit)
        WorkManager.getInstance(this).enqueue(workStars)
    }
}
