package net.wangyl.goldenlife.base

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
        (f as? IBase)?.getDelegate()?.onAttach(context)
        Timber.d("FragmentLifecycle onFragmentAttached f=$f ")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        (f as? IBase)?.getDelegate()?.onCreate(savedInstanceState)
        Timber.d("FragmentLifecycle onFragmentCreated f=$f ")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        (f as? IBase)?.getDelegate()?.onCreateView(v, savedInstanceState)
        Timber.d("FragmentLifecycle onFragmentViewCreated f=$f ")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onStart()
        Timber.d("FragmentLifecycle onFragmentStarted f=$f ")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onResume()
        Timber.d("FragmentLifecycle onFragmentResumed f=$f ")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onPause()
        Timber.d("FragmentLifecycle onFragmentPaused f=$f ")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onStop()
        Timber.d("FragmentLifecycle onFragmentStopped f=$f ")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        (f as? IBase)?.getDelegate()?.onSaveInstanceState(outState)
        Timber.d("FragmentLifecycle onFragmentSaveInstanceState f=$f ")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onDestroy()
        Timber.d("FragmentLifecycle onFragmentDestroyed f=$f ")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate()?.onDetach()
        Timber.d("FragmentLifecycle onFragmentDetached f=$f ")
    }


}