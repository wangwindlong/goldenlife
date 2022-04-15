package net.wangyl.goldenlife.ui.frag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import net.wangyl.base.mvi.orbit.BindMviFragment
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.databinding.FragmentLoginBinding
import net.wangyl.goldenlife.model.UserSession
import net.wangyl.goldenlife.obj.Global
import net.wangyl.goldenlife.ui.launchMain
import net.wangyl.goldenlife.vm.LoginViewModel
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
                Global.userSession = it
                requireContext().launchMain()
            }
        }
    }

    override fun loadData() {
        super.loadData()

    }
}