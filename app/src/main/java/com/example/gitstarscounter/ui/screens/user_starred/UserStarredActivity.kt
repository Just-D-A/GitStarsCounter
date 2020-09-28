package com.example.gitstarscounter.ui.screens.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Repository
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.example.gitstarscounter.ui.screens.stars.StarsActivity
import com.example.gitstarscounter.ui.screens.stars.StarsPresenter
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.presenter.InjectPresenter
import com.omegar.mvp.presenter.ProvidePresenter

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class UserStarredActivity : BaseActivity(), UserStarredView {
    companion object {
        private const val STAR_LIST_EXTRA = "starsList"

        fun createLauncher(
            starsList: List<Star>
        ) =
            createActivityLauncher(
                STAR_LIST_EXTRA put ArrayList(starsList)
            )
    }

    private val searchEditText: EditText by bind(R.id.edit_text_user_name)
    private val usersRecycleView: RecyclerView by bind(R.id.recycler_view_users)

    private lateinit var userStarredAdapter: UserStarredAdapter

    @InjectPresenter
    override lateinit var presenter: UserStarredPresenter

    @ProvidePresenter
    fun provideDetailsPresenter(): UserStarredPresenter {
        return UserStarredPresenter(
            intent.getSerializableExtra(STAR_LIST_EXTRA) as ArrayList<Star>
        )
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_starred)

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.loadUserList()

        userStarredAdapter = UserStarredAdapter()
        usersRecycleView.adapter = userStarredAdapter

        searchEditText.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
            userStarredAdapter.filter(charSequence.toString())
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setupUsersList(remoteStarList: MutableList<Star>) {
        userStarredAdapter.setupUsers(remoteStarList)
        usersRecycleView.isVisible = true
    }
}
