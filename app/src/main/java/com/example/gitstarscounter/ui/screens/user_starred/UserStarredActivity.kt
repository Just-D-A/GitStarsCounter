package com.example.gitstarscounter.ui.screens.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gitstarscounter.R
import com.example.gitstarscounter.entity.Star
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.presenter.InjectPresenter
import java.io.Serializable

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class UserStarredActivity : BaseActivity(), UserStarredView {
    companion object {
        private const val KEY_STAR_LIST = "starsList"
        private const val KEY_HAS_INTERNET = "hasInternet"

        fun createLauncher(
            starsList: List<Star>,
            noInternetVisible: Boolean
        ) =
            createActivityLauncher(
                KEY_STAR_LIST put ArrayList(starsList),
                KEY_HAS_INTERNET put noInternetVisible
            )

    }

    private val searchEditText: EditText by bind(R.id.edit_text_user_name)
    private val usersRecycleView: RecyclerView by bind(R.id.recycler_view_users)

    private lateinit var userStarredAdapter: UserStarredAdapter

    @InjectPresenter
    override lateinit var presenter: UserStarredPresenter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_starred)

        val starsList = intent.getSerializableExtra(KEY_STAR_LIST) as? MutableList<Star>

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.loadUserList(starsList!!)

        userStarredAdapter = UserStarredAdapter(this)

        usersRecycleView.adapter = userStarredAdapter
        usersRecycleView.layoutManager =
            LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        usersRecycleView.hasFixedSize()

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

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content)?.windowToken, 0)
    }
}
