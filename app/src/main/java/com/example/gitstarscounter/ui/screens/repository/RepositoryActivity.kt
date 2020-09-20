package com.example.gitstarscounter.ui.screens.repository

import android.os.Bundle
import com.example.gitstarscounter.R
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.mvp.presenter.InjectPresenter


class RepositoryActivity : BaseActivity(), RepositoryView {
    private val repositoryRecyclerView: OmegaRecyclerView by bind(R.id.recycler_view_repositories)

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
    }

    override fun deleteRepository() {
        TODO("Not yet implemented")
    }
}
