package net.wangyl.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.wangyl.base.dialog.LoadingDialog
import net.wangyl.base.enums.LoadingState
import net.wangyl.base.interf.loading
import timber.log.Timber



class FragmentLifecycle private constructor(): FragmentManager.FragmentLifecycleCallbacks() {

    companion object {
        val instance = FragmentLifecycle.holder
    }

    private object FragmentLifecycle {
        val holder = FragmentLifecycle()
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        (f as? IBase)?.baseDelegate()?.onAttach(context)
        Timber.d("FragmentLifecycle onFragmentAttached $f")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        (f as? IBase)?.apply {
            uiState()?.loading?.observe(f) {
                if (it == LoadingState.LOADING) {
                    LoadingDialog.show(f)
                } else {
                    LoadingDialog.dismiss(f)
                }
            }
            baseDelegate()?.onCreate(savedInstanceState)
            initData(savedInstanceState ?: f.arguments)
        }
        Timber.d("FragmentLifecycle onFragmentCreated $f")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        (f as? IBase)?.baseDelegate()?.onCreateView(v, savedInstanceState)
        Timber.d("FragmentLifecycle onFragmentViewCreated $f")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onStart()
        Timber.d("FragmentLifecycle onFragmentStarted $f")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.apply {
            baseDelegate()?.onResume()
        }
        Timber.d("FragmentLifecycle onFragmentResumed $f")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onPause()
        Timber.d("FragmentLifecycle onFragmentPaused $f")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onStop()
        Timber.d("FragmentLifecycle onFragmentStopped $f")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        (f as? IBase)?.baseDelegate()?.onSaveInstanceState(outState)
        Timber.d("FragmentLifecycle onFragmentSaveInstanceState $f")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onDestroy()
        Timber.d("FragmentLifecycle onFragmentDestroyed $f")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onDetach()
        Timber.d("FragmentLifecycle onFragmentDetached $f")
    }


}