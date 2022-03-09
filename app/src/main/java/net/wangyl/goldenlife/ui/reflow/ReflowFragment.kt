package net.wangyl.goldenlife.ui.reflow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import net.wangyl.goldenlife.databinding.FragmentReflowBinding
import net.wangyl.goldenlife.extension.goActivity
import net.wangyl.goldenlife.ui.frag.ViewPagerFragment

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
            goActivity(ViewPagerFragment::class.java.name)
        }
        reflowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        binding.textReflow.setOnClickListener {
//            viewModel.login("raheem", "android")
//        }

        lifecycleScope.launchWhenCreated {
            viewModel.loginUIState.collect {
                Log.d("ReflowFragment", "receive state = ${it}")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}