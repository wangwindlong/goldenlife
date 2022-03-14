package net.wangyl.goldenlife.ui.reflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import net.wangyl.base.data.FragmentData
import net.wangyl.base.TAG_FRAGS
import net.wangyl.base.TabViewPagerFragment
import net.wangyl.goldenlife.databinding.FragmentReflowBinding
import net.wangyl.base.extension.goActivity
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment
import timber.log.Timber

class ReflowFragment : Fragment() {

    private var _binding: FragmentReflowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReflowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reflowViewModel =
            ViewModelProvider(this).get(ReflowViewModel::class.java)

        _binding = FragmentReflowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textReflow
        textView.setOnClickListener {
            goActivity(
                TabViewPagerFragment::class.java.name,
                Intent().putParcelableArrayListExtra(TAG_FRAGS, testFragments()))
        }
        reflowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        binding.textReflow.setOnClickListener {
//            viewModel.login("raheem", "android")
//        }

        lifecycleScope.launchWhenCreated {
            viewModel.loginUIState.collect {
                Timber.d("ReflowFragment", "receive state = ${it}")
                when (it) {
                    is ReflowViewModel.LoginUIState.Loading -> {
//                        progressBar.visibility = View.VISIBLE
                    }

                    is ReflowViewModel.LoginUIState.Success -> {
                        Snackbar.make(binding.root, "Successfully logged in", Snackbar.LENGTH_SHORT).show()
//                        progressBar.visibility = View.GONE
                    }

                    is ReflowViewModel.LoginUIState.Error -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
//                        progressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
        return root
    }

    private fun testFragments() : ArrayList<FragmentData> {
        val list = arrayListOf<FragmentData>()
       for (i in 1..10) {
           Timber.d("testFragments i=$i")
           list.add(
               FragmentData(
                   SlideshowFragment::class.java.name, "标题_$i",
                   Intent().putExtra("args_1", "args1")
               )
           )
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}