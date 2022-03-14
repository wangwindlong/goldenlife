package net.wangyl.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
        Timber.d("FragmentLifecycle onFragmentAttached ")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        (f as? IBase)?.apply {
            baseDelegate()?.onCreate(savedInstanceState)
            initData(savedInstanceState ?: f.arguments)
        }
        Timber.d("FragmentLifecycle onFragmentCreated ")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        (f as? IBase)?.baseDelegate()?.onCreateView(v, savedInstanceState)
        Timber.d("FragmentLifecycle onFragmentViewCreated ")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onStart()
        Timber.d("FragmentLifecycle onFragmentStarted ")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onResume()
        Timber.d("FragmentLifecycle onFragmentResumed ")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onPause()
        Timber.d("FragmentLifecycle onFragmentPaused ")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onStop()
        Timber.d("FragmentLifecycle onFragmentStopped ")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        (f as? IBase)?.baseDelegate()?.onSaveInstanceState(outState)
        Timber.d("FragmentLifecycle onFragmentSaveInstanceState ")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onDestroy()
        Timber.d("FragmentLifecycle onFragmentDestroyed ")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.baseDelegate()?.onDetach()
        Timber.d("FragmentLifecycle onFragmentDetached ")
    }


}