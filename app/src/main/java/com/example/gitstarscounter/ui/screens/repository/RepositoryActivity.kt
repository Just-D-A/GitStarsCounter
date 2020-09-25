package com.example.gitstarscounter.ui.screens.repository

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.bumptech.glide.Glide
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omega_r.base.adapters.OmegaAutoAdapter
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.mvp.presenter.InjectPresenter

class RepositoryActivity : BaseActivity(), RepositoryView {
    companion object {
        private const val BACK_BUTTON_ID = 16908332
        fun createLauncher() = createActivityLauncher()
    }

    private val repositoryRecyclerView: OmegaRecyclerView by bind(R.id.recycler_view_repositories)

    @InjectPresenter
    override lateinit var presenter: RepositoryPresenter

    lateinit var repositoryAdapter: OmegaAutoAdapter<Repository, OmegaAutoAdapter.SwipeViewHolder<Repository>>

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        repositoryAdapter = OmegaAutoAdapter.create(
            R.layout.cell_repository,
            R.layout.item_right_menu,
            callback = { item -> presenter.responseToOpenStars(applicationContext, item) }
        ) {
            bind(R.id.text_view_repository_name, Repository::name)
            //bind(R.id.text_view_repository_owner_name, Repository::user::name)
            val image = Glide
                .with(applicationContext)
                .load(R.drawable.repository_img)
                .centerCrop()
            //bindImage(R.id.circle_image_view_repository_image, )

            bindClick(
                R.id.button_delete_repository,
                block = { item -> presenter.responseToDeleteRepository(item) })
        }

        repositoryRecyclerView.adapter = repositoryAdapter

        repositoryRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        repositoryRecyclerView.hasFixedSize()
    }

    override fun setRepositoryList(repositoryList: List<Repository>) {
        repositoryAdapter.list = repositoryList
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
}
