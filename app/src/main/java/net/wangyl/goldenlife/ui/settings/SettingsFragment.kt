package net.wangyl.goldenlife.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import net.wangyl.base.data.MsgEvent
import net.wangyl.goldenlife.databinding.FragmentSettingsBinding
import net.wangyl.goldenlife.model.PostData
import net.wangyl.base.mvi.orbit.BaseMviFragment
import net.wangyl.base.manager.postEvent
import timber.log.Timber

class SettingsFragment : BaseMviFragment<PostData>() {

    private var _binding: FragmentSettingsBinding? = null
//    private val args: SettingsFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: Button = binding.textSettings
        textView.setOnClickListener {
            postEvent(MsgEvent("test", "${textView.text}"))
        }
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        Timber.d("onCreateView arguments itemId= ${arguments?.get("itemId")}, item= ${arguments?.get("item")}")
//        Log.d(TAG, "onCreateView args itemId= ${args.itemId}, item= ${args.item}")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}