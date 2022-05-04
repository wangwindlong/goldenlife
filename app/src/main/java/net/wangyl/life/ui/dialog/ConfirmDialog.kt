package net.wangyl.life.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import net.wangyl.base.dialog.BaseBindDialogFragment
import net.wangyl.base.dialog.DialogResult
import net.wangyl.life.R
import net.wangyl.life.databinding.DialogTestBinding

class ConfirmDialog : BaseBindDialogFragment<DialogTestBinding>(R.layout.dialog_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.testBtn.setOnClickListener {
            lifecycleScope.launch {
                channelViewModel.channel.send(
                    DialogResult.Ok(1)
                )
            }
        }

    }
}