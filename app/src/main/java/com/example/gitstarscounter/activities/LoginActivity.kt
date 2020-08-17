package com.example.gitstarscounter.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.gitstarscounter.R
import com.example.gitstarscounter.presenters.LoginPresenter
import com.example.gitstarscounter.views.LoginView
import com.github.rahatarmanahmed.cpv.CircularProgressView

class LoginActivity : MvpAppCompatActivity(), LoginView {

    private lateinit var mCpvWait: CircularProgressView
    private lateinit var mBtnFind: Button

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCpvWait = findViewById(R.id.cpv_login)
        mBtnFind = findViewById(R.id.btn_find_acc)


        val mTxtAccountName: EditText = findViewById(R.id.txt_account_name)
        mBtnFind.setOnClickListener{
            val accountName: String = mTxtAccountName.getText().toString().trim()
            loginPresenter.testLoadStars(accountName = accountName)
        }
    }

    override fun startLoading() {
        mBtnFind.visibility = View.GONE
        mCpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mBtnFind.visibility = View.VISIBLE
        mCpvWait.visibility = View.GONE
    }

    override fun openStars() {
        startActivity(Intent(applicationContext, StarsActivity::class.java))
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, textResource, Toast.LENGTH_SHORT).show()
    }
}