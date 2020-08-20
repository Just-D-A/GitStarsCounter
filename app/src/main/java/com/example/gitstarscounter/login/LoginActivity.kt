package com.example.gitstarscounter.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.stars.StarsActivity
import com.github.rahatarmanahmed.cpv.CircularProgressView

class LoginActivity : MvpAppCompatActivity(), LoginView {

    private lateinit var waitProgressView: CircularProgressView
    private lateinit var findButton: Button

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waitProgressView = findViewById(R.id.progress_view_login) // need login in name??
        findButton = findViewById(R.id.button_find_rep)


        val accountNameEditText: EditText = findViewById(R.id.text_rep_name)
        findButton.setOnClickListener{
            val userName = accountNameEditText.text.toString().trim()
            loginPresenter.loadUser(userName)
        }


    }

    override fun startLoading() {
        findButton.isVisible = false
        waitProgressView.isVisible = true
    }

    override fun endLoading() {
        findButton.isVisible = true
        waitProgressView.isVisible = false
    }

    override fun openStars(userName: String) {
        startActivity(StarsActivity.createIntent(this, userName))
    }

    override fun showError(textResource: Int) {
        Toast.makeText(this, textResource, Toast.LENGTH_SHORT).show()
    }
}