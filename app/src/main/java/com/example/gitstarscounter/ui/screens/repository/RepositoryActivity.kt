package com.example.gitstarscounter.ui.screens.repository

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.User
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.base.adapters.OmegaAutoAdapter
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.presenter.InjectPresenter

class RepositoryActivity : BaseActivity(), RepositoryView {
    companion object {
        fun createLauncher() = createActivityLauncher()
    }

    private val repositoryRecyclerView: OmegaRecyclerView by bind(R.id.recycler_view_repositories)

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter

    private var repositoryAdapter = OmegaAutoAdapter.create<Repository>(
        R.layout.cell_repository,
        R.layout.item_right_menu,
        callback = { item -> presenter.requestToOpenStars(item) }
    ) {
        bind(R.id.text_view_repository_name, Repository::name)
        bindString(R.id.text_view_repository_owner_name, Repository::user, User::name)
        bindImage(R.id.circle_image_view_repository_image, Repository::user, User::avatar)

        bindClick(
            R.id.button_delete_repository,
            block = { item -> presenter.requestToDeleteRepository(item) })
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        supportActionBar.apply {
            this?.setHomeButtonEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
        }

        repositoryRecyclerView.adapter = repositoryAdapter
    }

    override fun setRepositoryList(repositoryList: List<Repository>) {
        repositoryAdapter.list = repositoryList
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
