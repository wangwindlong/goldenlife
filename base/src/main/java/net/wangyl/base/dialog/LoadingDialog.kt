package net.wangyl.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import net.wangyl.base.R
import net.wangyl.base.extension.dp2px
import net.wangyl.base.extension.toPx
import net.wangyl.base.widget.jumpbean.JumpingBeans

/**
 * @author Lin
 * @date 2021/10/20
 * @function 加载中对话框
 */
class LoadingDialog : DialogFragment() {
    private var jumpingBeans: JumpingBeans? = null
    lateinit var msgTv: TextView
    private var msg: String? = null

    private val screenWidth by lazy {
        val point = Point()
        (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealSize(
            point
        )
        point.x
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = savedInstanceState ?: arguments
        msg = args?.getString("msg") ?: "努力加载中"
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
        contentView.radius = 16.dp2px().toFloat()
        val margin = 20.dp2px()
        val color = resources.getColor(R.color.base_dimmer)
        contentView.setCardBackgroundColor(color)
        contentView.foreground = ColorDrawable(color)
//        contentView.setBackgroundColor(resources.getColor(R.color.base_red_700))
        contentView.addView(ProgressBar(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            ).apply {
//                bottomMargin = margin
//                topMargin = margin
//                rightMargin = margin
//                leftMargin = margin
            }
        })
        msgTv = TextView(context)
        msgTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
        msgTv.setTextColor(resources.getColor(R.color.base_white))
        msgTv.text = msg
        contentView.addView(msgTv.apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            ).apply {
                bottomMargin = margin / 2
            }
        })
        return contentView
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogSize = (screenWidth * 0.35).toInt()
            setLayout(dialogSize, dialogSize)
        }
        setMessage(msg)
    }

    fun setMessage(message: String?) {
        if (!isAdded) return
        msgTv.text = message
        jumpingBeans?.stopJumping()
        jumpingBeans = JumpingBeans.with(msgTv)
            .appendJumpingDots()
            .build()
    }

    override fun onDetach() {
        super.onDetach()
        jumpingBeans?.stopJumping()
    }

    companion object {
        private val TAG = LoadingDialog::class.java.simpleName
        private fun showDialog(fm: FragmentManager, msg: String? = null) {
            (fm.findFragmentByTag(TAG) as? LoadingDialog)?.dismiss()

//        if (getOwnerActivity() == null || getOwnerActivity().isFinishing()) return;
            LoadingDialog().apply { arguments = Bundle().also { it.putString("msg", msg) } }
                .showNow(fm, TAG)
        }

        private fun dismissDialog(fm: FragmentManager) {
            (fm.findFragmentByTag(TAG) as? LoadingDialog)?.dismiss()
        }

        fun show(activity: FragmentActivity, msg: String? = null) {
            showDialog(activity.supportFragmentManager, msg)
        }

        fun show(fragment: Fragment, msg: String? = null) {
            showDialog(fragment.childFragmentManager, msg)
        }

        fun dismiss(activity: FragmentActivity) {
            dismissDialog(activity.supportFragmentManager)
        }

        fun dismiss(fragment: Fragment) {
            dismissDialog(fragment.childFragmentManager)
        }
    }

}