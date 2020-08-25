package com.example.gitstarscounter.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.git_api.Star
import com.github.rahatarmanahmed.cpv.CircularProgressView
import java.io.Serializable

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class UserStarredActivity : MvpAppCompatActivity(), UserStarredView {

    private lateinit var searchEditText: EditText
    private lateinit var waitProgressView: CircularProgressView
    private lateinit var usersRecycleView: RecyclerView

    private lateinit var userStarredAdapter: UserStarredAdapter

    @InjectPresenter
    lateinit var userStarredPresenter: UserStarredPresenter

    companion object {

        private const val KEY_STAR_LIST = "starsList"

        fun createIntent(context: Context, starsList: MutableList<Star>) = Intent(
            context,
            UserStarredActivity::class.java
        )
            .putExtra(KEY_STAR_LIST, starsList as Serializable)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_starred)

        searchEditText = findViewById(R.id.edit_text_user_name)
        waitProgressView = findViewById(R.id.progress_view_user_starred)
        usersRecycleView = findViewById(R.id.recycler_users) //chage

        val starsList = intent.getSerializableExtra(KEY_STAR_LIST) as? MutableList<Star>

        userStarredPresenter.loadUserList(starsList!!)

        userStarredAdapter = UserStarredAdapter()

        usersRecycleView.adapter = userStarredAdapter
        usersRecycleView.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        usersRecycleView.hasFixedSize()

        searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userStarredAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {

                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER ->
                        hideKeyboard()
                }
            }
            true
        }
    }

    override fun startLoading() {
        waitProgressView.isVisible = true
        usersRecycleView.isVisible = false
    }

    override fun endLoading() {
        waitProgressView.isVisible = false
    }

    override fun setupUsersList(starList: MutableList<Star>) {
        userStarredAdapter.setupUsers(starList)
        usersRecycleView.isVisible = true
    }

    override fun showError(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
}