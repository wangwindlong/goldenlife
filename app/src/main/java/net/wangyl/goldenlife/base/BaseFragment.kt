package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

open class BaseFragment : Fragment(), IBase {
    open val TAG = javaClass.simpleName
    var _delegate: ILifeDelegate? = null

    override fun getDelegate(fm: FragmentManager): ILifeDelegate? {
        return if (_delegate == null) {
            _delegate = LifeDelegateIml(fm, this)
            _delegate
        } else _delegate
    }

    @Subscribe
    open fun test(data: String) {
        Timber.d("$TAG received data: $data")
    }
}