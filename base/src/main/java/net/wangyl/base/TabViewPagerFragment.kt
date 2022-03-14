package net.wangyl.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_AUTO
import com.google.android.material.tabs.TabLayoutMediator
import net.wangyl.base.data.FragmentData
import net.wangyl.base.extension.createFragment
import net.wangyl.base.extension.dp

const val TAG_FRAGS = "tab_frags" //是否显示tabbar
const val FLAG_TABBAR = 0x000001 //是否显示tabbar
const val FLAG_TABBAR_BOTTOM = 0x000010 //是否在底部显示tabbar，默认为顶部

/**
 * 带有tablayout和viewpager2的简易fragment，tablayout 可在顶部、底部或不显示
 */
class TabViewPagerFragment : BaseFragment() {
    var tabBar = 0x000001  //tablayout在顶部
    val fragments = arrayListOf<FragmentData>()
    val offscreenCount = 40

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        savedInstanceState?.apply {
            tabBar = this.getInt("tab_state", tabBar)
            fragments.clear()
            this.getParcelableArrayList<FragmentData>(TAG_FRAGS)?.let {
                fragments.addAll(it)
            }
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (context == null) return null
        //初始化 ConstraintLayout
        val constriantLl = ConstraintLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.base_red_100))
        }

        //初始化TabLayout
        val tabLayout = TabLayout(requireContext()).apply {
            id = R.id.base_tablayout
            visibility = if (isShowTab()) View.VISIBLE else View.GONE
            background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.base_blue_500))
            tabMode = MODE_AUTO
        }
        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, 45.dp())
        params.startToStart = PARENT_ID
        params.endToEnd = PARENT_ID
//        params.marginStart = 18.dp()
        if (isTabTop()) {
            params.topToTop = PARENT_ID
        } else {
            params.bottomToBottom = PARENT_ID
        }
//        params.topMargin = 18.dp()
//        params.dimensionRatio = "h,16:9"
        tabLayout.layoutParams = params
        constriantLl.addView(tabLayout)

        //初始化viewpager2
        val viewPager2 = ViewPager2(requireContext()).apply {
            id = R.id.base_viewpager
            background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.base_green_300))
        }
        viewPager2.offscreenPageLimit = offscreenCount
        viewPager2.layoutParams =  ConstraintLayout.LayoutParams(0, 0).apply {
            leftToLeft = PARENT_ID
            rightToRight = PARENT_ID

            if (isTabTop()) {
                topToBottom = R.id.base_tablayout
                bottomToBottom = PARENT_ID
            } else {
                topToTop = PARENT_ID
                bottomToTop = R.id.base_tablayout
            }
        }
        viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return requireContext().createFragment(parentFragmentManager, fragments[position])
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }
        constriantLl.addView(viewPager2)
//        viewPager2.setCurrentItem(0)

        //tablayout关联viewpager2
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = fragments[position].title
        }.attach()
//        val barrier = Barrier(requireContext()).apply { layoutParams }
        return constriantLl
    }


    private fun isShowTab() = tabBar and FLAG_TABBAR == 1
    private fun isTabTop() = isShowTab() && (tabBar and FLAG_TABBAR_BOTTOM == 0)
}