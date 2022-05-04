package net.wangyl.life.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import net.wangyl.base.data.MsgEvent
import net.wangyl.base.manager.postEvent
import net.wangyl.base.mvi.orbit.BaseMviFragment
import net.wangyl.life.R
import net.wangyl.life.databinding.FragmentSettingsBinding
import net.wangyl.life.model.PostData
import timber.log.Timber

class SettingsFragment : BaseMviFragment<PostData>() {

    val settingsViewModel by viewModels<SettingsViewModel>()
    val binding by viewBinding<FragmentSettingsBinding>()

    override fun initView(v: View?, savedInstanceState: Bundle?) {
        super.initView(v, savedInstanceState)
        val textView: Button = binding.textSettings
        textView.setOnClickListener {
            postEvent(MsgEvent("test", "发送登出事件"))
            settingsViewModel.logout()
        }
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        Timber.d("onCreateView arguments itemId= ${arguments?.get("itemId")}, item= ${arguments?.get("item")}")
    }

    override fun onReceiveMsg(data: MsgEvent<Any>) {
        super.onReceiveMsg(data)
        binding.textSettings.text = data.msg as? String
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }
}