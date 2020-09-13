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
import com.example.gitstarscounter.data.remote.entity.StarRemote
import com.example.gitstarscounter.ui.screens.base.BaseActivity
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.presenter.InjectPresenter
import java.io.Serializable


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class UserStarredActivity : BaseActivity(), UserStarredView {
    private lateinit var searchEditText: EditText
    private lateinit var usersRecycleView: RecyclerView
    private lateinit var noInternetTextView: TextView
    private lateinit var userStarredAdapter: UserStarredAdapter

    @InjectPresenter
    override lateinit var presenter: UserStarredPresenter

    companion object {
        const val BACK_BUTTON_ID = 16908332

        private const val KEY_STAR_LIST = "starsList"
        private const val KEY_HAS_INTERNET = "hasInternet"

        fun createLauncher(
            starsList: MutableList<StarRemote>,
            noInternetVisible: Boolean
        ) =
            createActivityLauncher(
                KEY_STAR_LIST put starsList as Serializable,
                KEY_HAS_INTERNET put noInternetVisible
            )
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_starred)

        searchEditText = findViewById(R.id.edit_text_user_name)!!
        usersRecycleView = findViewById(R.id.recycler_view_users)!!
        noInternetTextView = findViewById(R.id.text_view_no_internet_user_starred)!!
        noInternetTextView.isVisible = intent.getBooleanExtra(KEY_HAS_INTERNET, false)

        val starsList = intent.getSerializableExtra(KEY_STAR_LIST) as? MutableList<StarRemote>

        val actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        presenter.loadUserList(starsList!!)

        userStarredAdapter = UserStarredAdapter(this)

        usersRecycleView.adapter = userStarredAdapter
        usersRecycleView.layoutManager =
            LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        usersRecycleView.hasFixedSize()

        searchEditText.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
            userStarredAdapter.filter(charSequence.toString())
        })

        searchEditText.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        searchEditText.setOnEditorActionListener(
            TextView.OnEditorActionListener { v, actionId, event -> // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })
    }

    override fun setupUsersList(starRemoteList: MutableList<StarRemote>) {
        userStarredAdapter.setupUsers(starRemoteList)
        usersRecycleView.isVisible = true
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content)?.windowToken, 0)
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

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }
}
