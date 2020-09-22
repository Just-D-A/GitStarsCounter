package com.example.gitstarscounter.ui.screens.repository

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.example.gitstarscounter.ui.screens.login.LoginAdapter
import com.example.gitstarscounter.ui.screens.user_starred.UserStarredActivity
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.presenter.InjectPresenter

class RepositoryActivity : BaseActivity(), RepositoryView, RepositoryAdapter.DeleteCallback {
    companion object {
        fun createLauncher() = createActivityLauncher()
    }

    private val repositoryRecyclerView: OmegaRecyclerView by bind(R.id.recycler_view_repositories)

    private lateinit var repositoryAdapter: RepositoryAdapter

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        val onRepositoryClickListener: LoginAdapter.OnRepositoryClickListener =
            object : LoginAdapter.OnRepositoryClickListener {
                override fun onRepositoryClick(repository: Repository) {
                    presenter.responseToOpenStars(applicationContext, repository)
                }
            }

        repositoryAdapter = RepositoryAdapter(this, onRepositoryClickListener)

        repositoryRecyclerView.adapter = repositoryAdapter
        repositoryRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        repositoryRecyclerView.hasFixedSize()
    }

    override fun setRepositoryList(repositoryList: List<Repository>) {
        repositoryAdapter.setRepositoriesList(repositoryList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                this.finish()
                true
            }

            UserStarredActivity.BACK_BUTTON_ID -> {
                this.finish()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onPressedDeleteButton(repository: Repository) {
        presenter.responseToDeleteRepository(repository)
    }
}
