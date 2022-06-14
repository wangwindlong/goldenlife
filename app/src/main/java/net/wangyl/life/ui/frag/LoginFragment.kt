package net.wangyl.life.ui.frag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import net.wangyl.base.mvi.orbit.BindMviFragment
import net.wangyl.life.R
import net.wangyl.life.databinding.FragmentLoginBinding
import net.wangyl.life.model.UserSession
import net.wangyl.life.obj.Global
import net.wangyl.life.ui.launchMain
import net.wangyl.life.vm.LoginViewModel
import timber.log.Timber

class LoginFragment: BindMviFragment<UserSession, FragmentLoginBinding>() {
    val viewModel by viewModels<LoginViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView(v: View?, savedInstanceState: Bundle?) {
        super.initView(v, savedInstanceState)
        binding.mSignInBt.setOnClickListener {
            viewModel.login {
                Timber.d(" $it")
                Global.updateUserData(it)
                requireContext().launchMain()
            }
        }
    }

    override fun loadData() {
        super.loadData()

    }
}