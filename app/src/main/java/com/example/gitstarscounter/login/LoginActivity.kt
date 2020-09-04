package com.example.gitstarscounter.login

//import com.example.gitstarscounter.entity.repository.Repository
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.GitStarsDatabase
import com.example.gitstarscounter.git_api.RepositoryModel
import com.example.gitstarscounter.service.StarService
import com.example.gitstarscounter.stars.StarsActivity
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : MvpAppCompatActivity(), LoginView, OnPageRequestListener {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var findButton: Button
    private lateinit var repositoryOmegaRecycleView: OmegaRecyclerView
    private lateinit var noInternetTextView: TextView
    private lateinit var repositoriesAdapter: RepositoryAdapter
    private var pageNumber = 1

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waitProgressView = findViewById(R.id.progress_view_login) // need login in name??
        findButton = findViewById(R.id.button_find_rep)
        repositoryOmegaRecycleView = findViewById(R.id.recycler_repositories)
        noInternetTextView = findViewById(R.id.text_view_no_internet_login)

        //For database
        GitStarsDatabase.createDatabase(applicationContext)

        var userName = "" // как правильно передавать??
        val accountNameEditText: EditText = findViewById(R.id.text_rep_name)
        findButton.setOnClickListener {
            userName = accountNameEditText.text.toString().trim()
            pageNumber = 1
            loginPresenter.loadRepositories(userName, pageNumber)
            repositoriesAdapter.setupRepositoriesList(mutableListOf())
            repositoryOmegaRecycleView.showProgressPagination()
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
                override fun onRepositoryClick(repository: RepositoryModel?) {
                    loginPresenter.openStars(repository)

                }
            }

        repositoriesAdapter = RepositoryAdapter(onRepositoryClickListener, this)

        repositoriesAdapter.setCallback(loginPresenter)
        repositoryOmegaRecycleView.adapter = repositoriesAdapter
        repositoryOmegaRecycleView.setPaginationCallback(this)
        repositoryOmegaRecycleView.layoutManager = LinearLayoutManager(
            applicationContext,
            OrientationHelper.VERTICAL,
            false
        )
        repositoryOmegaRecycleView.hasFixedSize()

        //OMEGA_R PAGGINATION
        repositoryOmegaRecycleView.setPaginationCallback(object : OnPageRequestListener {
            override fun onPageRequest(page: Int) {
                Log.d("PAGGINATION", "onPageRequest")
                pageNumber++
                loginPresenter.loadMoreRepositories(pageNumber)
                //loginPresenter.
            }

            override fun getPagePreventionForEnd(): Int {
                //repositoryOmegaRecycleView.showProgressPagination()
                Log.d("PAGGINATION", "getPagePreventionForEnd")

                // You can load data inside this callback
                return 5 // PREVENTION_VALUE - for how many positions until the end you want to be informed
            }
        })


    }


    override fun startLoading() {
        findButton.isVisible = false
        waitProgressView.isVisible = true
    }

    override fun endLoading() {
        findButton.isVisible = true
        waitProgressView.isVisible = false
    }

    override fun setupRepositoriesList(repositoriesList: List<RepositoryModel?>?) {
        repositoriesAdapter.setupRepositoriesList(repositoriesList)
        repositoryOmegaRecycleView.isVisible = true
    }

    override fun openStars(userName: String, repository: RepositoryModel) {
        startActivity(StarsActivity.createIntent(this, userName, repository))
        //ActivityLauncher(StarsActivity, )
    }


    override fun changeVisibilityOfNoInternetView(visible: Boolean) {
        noInternetTextView.isVisible = visible
    }

    override fun addPagination(repositoriesList: List<RepositoryModel>) {
        repositoriesAdapter.addMoreRepositories(repositoriesList)
        //loginPresenter.endPagination(repositoriesAdapter.getRepositoriesListSize())
    }

    override fun endPagination() {
        repositoryOmegaRecycleView.hidePagination();
    }

    override fun showError(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        findButton.performClick()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        startService(Intent(this, StarService::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startService(Intent(this, StarService::class.java))
    }

    override fun onPageRequest(page: Int) {
        Log.d("PAGGINATION", "onPageRequest()")// You can load data inside this callback
   //     repositoryOmegaRecycleView.showProgressPagination()
      //  downloadItems()
    }

    override fun getPagePreventionForEnd(): Int {
        Log.d("PAGGINATION", "getPagePreventionForEnd()")
        return 5
    }

    fun downloadItems() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            Log.d("PAGGINATION", "downloadItems()")
          //  repositoriesAdapter.addMoreRepositories(mutableListOf())//Image.createImageList(10))
        }, 3000)
    }


}
