package com.example.gitstarscounter.ui.screens.base

import com.example.gitstarscounter.R
import com.omega_r.base.annotations.OmegaContentView
import com.omega_r.base.components.OmegaActivity

@OmegaContentView(R.layout.activity_login)
abstract class BaseActivity : OmegaActivity(), BaseView {
    abstract override val presenter: BasePresenter<out BaseView>
}
