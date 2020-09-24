package com.example.gitstarscounter.ui.screens.repository

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.presenter.InjectPresenter

class RepositoryActivity : BaseActivity(), RepositoryView, RepositoryAdapter.DeleteCallback {
    companion object {
        private const val BACK_BUTTON_ID = 16908332
        fun createLauncher() = createActivityLauncher()
    }

    private val repositoryRecyclerView: OmegaRecyclerView by bind(R.id.recycler_view_repositories)

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter

    lateinit var repositoryAdapter: RepositoryAdapter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val onRepositoryClickListener: RepositoryAdapter.OnRepositoryClickListener =
            object : RepositoryAdapter.OnRepositoryClickListener {
                override fun onRepositoryClick(repository: Repository) {
                    presenter.responseToOpenStars(applicationContext, repository)
                }
            }

        repositoryAdapter = RepositoryAdapter(this, onRepositoryClickListener)
        //repositoryAdapter = OmegaAutoAdapter.create(R.layout.cell_repository, R.layout.item_right_menu, AutoBindModel<Repository>(), this)

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

            BACK_BUTTON_ID -> {
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
