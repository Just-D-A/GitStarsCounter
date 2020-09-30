package com.example.gitstarscounter.ui.screens.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.gitstarscounter.GitStarsApplication
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.base.adapters.OmegaAutoAdapter
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import com.omegar.mvp.presenter.InjectPresenter
import javax.inject.Inject
import javax.inject.Named

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : BaseActivity(), LoginView {
    companion object {
        private const val TAG = "LoginActivity"
        private const val PAGINATION_TAG = "PAGINATION"
        private const val PAGE_NUMBER_PREVENTION_FOR_END = 5
    }

    private val repositoryOmegaRecycleView: OmegaRecyclerView by bind(R.id.recycler_repositories)
    private val accountNameEditText: EditText by bind(R.id.edit_text_view_login_repository_name)

    private val loginAdapter = OmegaAutoAdapter.create<Repository>(
        R.layout.cell_login,
        { item -> presenter.requestToOpenStars(item) }
    ) {
        bind(R.id.text_view_cell_login_repository_name, Repository::name)
    }

    private var pageNumber = 1

    @InjectPresenter
    override lateinit var presenter: LoginPresenter

    @Inject
    @field:Named("StarWorker")
    lateinit var starWorker: PeriodicWorkRequest

    @Inject
    @field:Named("LimitWorker")
    lateinit var limitWorker: PeriodicWorkRequest

    init {
        GitStarsApplication.instance.gitStarsCounterComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //setOnClickListener(R.id.button_find_rep to loadRepositories())
        setOnClickListeners(
            R.id.button_find_rep to this::loadRepositories,
            R.id.button_start_repository_screen to presenter::requestToStartRepositoryActivity
        )

        accountNameEditText.setOnEditorActionListener { _, actionId, event -> // Identifier of the action. This will be either the identifier you supplied,
            // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                loadRepositories()
            }
            false
        }

        //OMEGA_R PAGGINATION
        repositoryOmegaRecycleView.setPaginationCallback(object : OnPageRequestListener {
            override fun onPageRequest(page: Int) {
                Log.d(PAGINATION_TAG, "onPageRequest()")// You can load data inside this callback
                pageNumber++
                presenter.requestToLoadMoreRepositories(pageNumber)
            }

            override fun getPagePreventionForEnd(): Int {
                Log.d(PAGINATION_TAG, "getPagePreventionForEnd()")
                return PAGE_NUMBER_PREVENTION_FOR_END
            }
        })
        repositoryOmegaRecycleView.adapter = loginAdapter

        setNewStarsFinder()//start worker
    }

    private fun loadRepositories() {
        val userName = accountNameEditText.text.toString().trim()
        pageNumber = 1 // init to first page
        presenter.requestToLoadRepositories(userName, pageNumber)
        loginAdapter.list = mutableListOf()
        repositoryOmegaRecycleView.showProgressPagination()
    }

    override fun setupRepositoriesList(repositoriesList: List<Repository>) {
        loginAdapter.list = repositoriesList
        repositoryOmegaRecycleView.isVisible = true
    }

    override fun addRepositoriesToList(repositoriesList: List<Repository>) {
        loginAdapter.list += repositoriesList
    }

    override fun endPagination() {
        repositoryOmegaRecycleView.hidePagination()
    }

    private fun setNewStarsFinder() {
        WorkManager.getInstance(this).enqueue(limitWorker)
        WorkManager.getInstance(this).enqueue(starWorker)
    }
}
