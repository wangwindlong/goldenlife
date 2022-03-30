package net.wangyl.goldenlife.ui.reflow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import net.wangyl.base.IBase
import net.wangyl.base.TAG_FRAGS
import net.wangyl.base.TabViewPagerFragment
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.FragmentData
import net.wangyl.base.data.onError
import net.wangyl.base.data.onSuccess
import net.wangyl.base.extension.await
import net.wangyl.base.extension.getK
import net.wangyl.base.extension.goActivity
import net.wangyl.base.interf.StateHost
import net.wangyl.base.interf.apiCall
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.ErrorResponseMapper
import net.wangyl.goldenlife.databinding.FragmentReflowBinding
import net.wangyl.goldenlife.startup.AnalyticsEvent
import net.wangyl.goldenlife.startup.AnalyticsService
import net.wangyl.goldenlife.startup.readChapter
import net.wangyl.goldenlife.api.repo.RSSRepository
import net.wangyl.goldenlife.model.RSSData
import net.wangyl.goldenlife.model.UserSession
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment
import timber.log.Timber

class ReflowFragment : Fragment(), IBase {

    private var _binding: FragmentReflowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReflowViewModel>()

    override fun uiState(): StateHost {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReflowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textReflow
        textView.setOnClickListener {
            goActivity(
                TabViewPagerFragment::class.java.name,
                Intent().putParcelableArrayListExtra(TAG_FRAGS, testFragments())
            )
        }
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        binding.textReflow.setOnClickListener {
//            viewModel.login("raheem", "android")
//        }
        getK<AnalyticsService>().track(AnalyticsEvent.readChapter("clicked reflow", "click", 100))

        lifecycleScope.launchWhenCreated {
//            val shouldContinueToMainScreen = showCreateUserDialog("Federico")
//            Timber.d("showCreateUserDialog result=$shouldContinueToMainScreen")
//
//            if (shouldContinueToMainScreen) {
//                /* navigate to other activity */
//            }

//            val custom = BaseDialogFragment(R.layout.dialog_test).showAndResult(childFragmentManager, "test")
//
////            val test = SimpleDialogFragment.newInstance(
////                "这个是标题", message = "这是小心内容",
////                positive = "确定", negative = "取消", neutral = "不确定"
////            ).showAndResult(childFragmentManager, "test")
////            Timber.d("SimpleDialogFragment result=$test")
//            when(custom) {
//                is DialogResult.Ok<*> -> {
//                }
//                else -> {
//
//                }
//            }

            val stringMap = HashMap<String, String>()
            stringMap["op"] = "login"
            stringMap["user"] = "admin"
            stringMap["password"] = "6629782"

            viewModel.apiCall(getK<RSSRepository>(), method = "login", params = stringMap) { q ->
                Timber.d("apicall params = ${q}")
                val request1 = async {getK<ApiService>().login(q as Map<String, String>)}
                val request2 = async {getK<ApiService>().login(q as Map<String, String>)}

                val res = request1.await()
                val res2 = request2.await()
                Timber.d("login1 result =$res")
                res2.onSuccess { data ->
                    Timber.d("login2 success result =$data")
                }.onError(ErrorResponseMapper) {
                    Timber.d("login2 error [Code: $code]: $message")
                }
                res2
            }.collect {
                Timber.d("login1 and login2 success result =$it")
            }

//            getK<ApiService>().login(jsonBody(stringMap))
//            getK<ApiService>().login2(stringMap)
            viewModel.loginUIState.collect {
                Timber.d("receive state = ${it}")
                when (it) {
                    is ReflowViewModel.LoginUIState.Loading -> {
//                        progressBar.visibility = View.VISIBLE
                    }

                    is ReflowViewModel.LoginUIState.Success -> {
                        Snackbar.make(binding.root, "Successfully logged in", Snackbar.LENGTH_SHORT)
                            .show()
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

    private fun createNewUser(username: String) {
        /* perform business logic */
    }

    private suspend fun showCreateUserDialog(username: String): Boolean {
        val shouldCreateUser = MaterialAlertDialogBuilder(requireContext())
            .setTitle("User \"$username\" was not found.")
            .setMessage("Create new user?")
            .create()
            .await(positiveText = "Create", negativeText = "Cancel")

        if (shouldCreateUser) {
            createNewUser(username)
            return true
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Continue without login?")
            .create()
            .await(positiveText = "Confirm", negativeText = "Cancel")
    }

    private fun testFragments(): ArrayList<FragmentData> {
        val list = arrayListOf<FragmentData>()
        for (i in 1..10) {
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