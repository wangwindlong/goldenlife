package net.wangyl.goldenlife.ui.frag

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.base.BaseFragment
import net.wangyl.goldenlife.extension.dp
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment

const val FLAG_TABBAR = 0x000001 //是否显示tabbar
const val FLAG_TABBAR_BOTTOM = 0x000010 //是否在底部显示tabbar，默认为顶部
class TabViewPagerFragment : BaseFragment() {
    var tabBar = 0x000001  //tablayout在顶部

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (context == null) return null
        val constrintLl = ConstraintLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.red_100))
        }
        val tabLayout = TabLayout(requireContext()).apply { id = R.id.tablayout }
        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(0, 45.dp())
        params.leftToLeft = PARENT_ID
        params.rightToRight = PARENT_ID
//        params.marginStart = 18.dp()
        if ((tabBar and FLAG_TABBAR) == 1) {
            params.topToTop = PARENT_ID
        } else {
            params.bottomToBottom = PARENT_ID
        }
//        params.topMargin = 18.dp()
        params.dimensionRatio = "h,16:9"
        tabLayout.layoutParams = params

        val viewPager2 = ViewPager2(requireContext()).apply { id = R.id.viewpager }
        viewPager2.layoutParams =  ConstraintLayout.LayoutParams(0, 0).apply {
            leftToLeft = PARENT_ID
            rightToRight = PARENT_ID

            if ((tabBar and FLAG_TABBAR) == 1) {
                topToBottom = R.id.tablayout
                bottomToBottom = PARENT_ID
            } else {
                topToTop = PARENT_ID
                bottomToTop = R.id.tablayout
            }
        }

        val barrier = Barrier(requireContext()).apply { layoutParams }

        constrintLl.addView(tabLayout)
        constrintLl.addView(viewPager2)
        viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return SlideshowFragment()
            }

            override fun getItemCount(): Int {
                return ViewPagerFragment.Card.DECK.size
            }
        }
        return constrintLl
    }
}