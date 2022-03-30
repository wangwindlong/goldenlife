package net.wangyl.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import net.wangyl.base.R
import net.wangyl.base.extension.dp2px
import net.wangyl.base.extension.toPx

/**
 * @author Lin
 * @date 2021/10/20
 * @function 加载中对话框
 */
class LoadingDialog : DialogFragment() {

    private val screenWidth by lazy {
        val point = Point()
        (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealSize(
            point
        )
        point.x
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val contentView = CardView(context)
        contentView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        contentView.radius = 16.toPx()
        val margin = 20.dp2px()
        contentView.setCardBackgroundColor(resources.getColor(R.color.base_shadow))
        contentView.addView(ProgressBar(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = margin
                topMargin = margin
                rightMargin = margin
                leftMargin = margin
            }
        })
        return contentView
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogSize = (screenWidth * 0.25).toInt()
            setLayout(dialogSize, dialogSize)
        }
    }

    companion object {
        private val TAG = LoadingDialog::class.java.simpleName
        private fun showDialog(fm: FragmentManager) {
            (fm.findFragmentByTag(TAG) as? LoadingDialog)?.dismiss()
            LoadingDialog().showNow(fm, TAG)
        }

        private fun dismissDialog(fm: FragmentManager) {
            (fm.findFragmentByTag(TAG) as? LoadingDialog)?.dismiss()
        }

        fun show(activity: FragmentActivity) {
            showDialog(activity.supportFragmentManager)
        }

        fun show(fragment: Fragment) {
            showDialog(fragment.childFragmentManager)
        }

        fun dismiss(activity: FragmentActivity) {
            dismissDialog(activity.supportFragmentManager)
        }

        fun dismiss(fragment: Fragment) {
            dismissDialog(fragment.childFragmentManager)
        }
    }

}