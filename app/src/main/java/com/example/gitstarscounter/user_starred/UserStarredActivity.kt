package com.example.gitstarscounter.user_starred

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
    private lateinit var noInternetTextView: TextView

    private lateinit var userStarredAdapter: UserStarredAdapter

    @InjectPresenter
    lateinit var userStarredPresenter: UserStarredPresenter

    companion object {

        private const val KEY_STAR_LIST = "starsList"
        private const val KEY_HAS_INTERNET = "hasInternet"


        fun createIntent(context: Context, starsList: MutableList<Star>, noInternetVisible: Boolean) = Intent(
            context,
            UserStarredActivity::class.java
        )
            .putExtra(KEY_STAR_LIST, starsList as Serializable)
            .putExtra(KEY_HAS_INTERNET, noInternetVisible)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_starred)

        searchEditText = findViewById(R.id.edit_text_user_name)
        waitProgressView = findViewById(R.id.progress_view_user_starred)
        usersRecycleView = findViewById(R.id.recycler_users) //chage-----
        noInternetTextView = findViewById(R.id.text_view_no_internet_user_starred) //chage-----
        noInternetTextView.isVisible = intent.getBooleanExtra(KEY_HAS_INTERNET, false)

        val starsList = intent.getSerializableExtra(KEY_STAR_LIST) as? MutableList<Star>

        val actionBar= supportActionBar
        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        userStarredPresenter.loadUserList(starsList!!)

        userStarredAdapter = UserStarredAdapter()

        usersRecycleView.adapter = userStarredAdapter
        usersRecycleView.layoutManager =
            LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        usersRecycleView.hasFixedSize()

        searchEditText.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
            userStarredAdapter.filter(charSequence.toString())
        })

        //searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER ->
                        hideKeyboard()
                }
            }
            false
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

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                //Log.d("BackButton", "pressed")
                this.finish()
                true
            }

            16908332 -> {
                //Log.d("BackButton", "pressed by num id")
                this.finish()
                true
            }

            else -> {
                //Log.d("BackButtonElse", "${item.itemId} == ${R.id.home}")
                super.onOptionsItemSelected(item)
            }
        }
    }
}