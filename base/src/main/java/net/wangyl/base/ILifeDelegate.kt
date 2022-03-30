package net.wangyl.base

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * fragment 和 activity 生命周期代理类，实现类为 LifeDelegateIml
 */
interface ILifeDelegate {
    fun onAttach(context: Context)

    fun onCreate(savedInstanceState: Bundle?)

    fun onCreateView(view: View?, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroyView()

    fun onDestroy()

    fun onDetach()

    /**
     * Return true if the fragment is currently added to its activity.
     */
//    fun isAdded(): Boolean
}