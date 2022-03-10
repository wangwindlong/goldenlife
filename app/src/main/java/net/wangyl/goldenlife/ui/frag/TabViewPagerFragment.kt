package net.wangyl.goldenlife.ui.frag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.base.BaseFragment
import net.wangyl.goldenlife.extension.dp

class TabViewPagerFragment:BaseFragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (context == null) return null
        val constrintLl = ConstraintLayout(requireContext())

        val tabLayout = TabLayout(requireContext())
        val layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            100.dp(), 0
        )
        layoutParams.leftToLeft = PARENT_ID
        layoutParams.marginStart = 18.dp()
        layoutParams.topToTop = PARENT_ID
        layoutParams.topMargin = 18.dp()
        layoutParams.dimensionRatio = "h,16:9"

        tabLayout.layoutParams = layoutParams

        val viewPager2 = ViewPager2(requireContext())

        constrintLl.addView(tabLayout)
        constrintLl.addView(viewPager2)

        return constrintLl
    }
}