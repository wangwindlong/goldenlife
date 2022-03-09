package net.wangyl.goldenlife.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import net.wangyl.goldenlife.base.MsgEvent
import net.wangyl.goldenlife.databinding.FragmentSettingsBinding
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.mvi.base.BaseMviFragment
import net.wangyl.goldenlife.utils.manager.EventBusManager

class SettingsFragment : BaseMviFragment<PostData>() {

    private var _binding: FragmentSettingsBinding? = null
    private val args: SettingsFragmentArgs by navArgs()

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
            eventBus.post(MsgEvent("test", "${textView.text}"))
        }
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        Log.d(TAG, "onCreateView arguments itemId= ${arguments?.get("itemId")}, item= ${arguments?.get("item")}")
        Log.d(TAG, "onCreateView args itemId= ${args.itemId}, item= ${args.item}")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}