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
        (f as? IBase)?.getDelegate(fm)?.onAttach(context)
        Timber.d("FragmentLifecycle onFragmentAttached f=$f  delegate=${(f as? IBase)?.getDelegate(fm)}")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        (f as? IBase)?.getDelegate(fm)?.onCreate(savedInstanceState)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        (f as? IBase)?.getDelegate(fm)?.onCreateView(v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onStart()
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onResume()
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onPause()
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onStop()
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        (f as? IBase)?.getDelegate(fm)?.onSaveInstanceState(outState)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onDestroy()
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        (f as? IBase)?.getDelegate(fm)?.onDetach()
    }


}