package com.example.gitstarscounter.ui.screens.base

import com.example.gitstarscounter.R
import com.omega_r.base.annotations.OmegaContentView
import com.omega_r.base.components.OmegaActivity
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put

@OmegaContentView(R.layout.activity_login)
abstract class BaseActivity : OmegaActivity(), BaseView {

    companion object {
        private const val EXTRA_TITLE = "title"

        fun createLauncher(title: String) = createActivityLauncher(
            EXTRA_TITLE put title
        )
    }

    abstract override val presenter: BasePresenter<out BaseView>
}
