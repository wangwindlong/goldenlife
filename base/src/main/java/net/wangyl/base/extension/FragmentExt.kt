package net.wangyl.base.extension

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

//
//
//inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this) { view: View ->
//    T::class.java.getMethod("bind", View::class.java).invoke(null, view) as T
//}





fun Fragment.navTo(navId: Int, bundle: Bundle? = null,
                   navOptions: NavOptions? = NavOptions.Builder().setHorizontalSlide().build(),
                   extras: FragmentNavigator.Extras? = null, fallback: (() -> Unit)? = null) {
    try {
        findNavController().navigate(navId, bundle, navOptions, extras)
    } catch (e: IllegalStateException) {
        fallback?.invoke()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun AlertDialog.await(
    positiveText: String,
    negativeText: String
) = suspendCancellableCoroutine<Boolean> { cont ->
    val listener = DialogInterface.OnClickListener { _, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) cont.resume(true, null)
        else if (which == AlertDialog.BUTTON_NEGATIVE) cont.resume(false, null)
    }

    setButton(AlertDialog.BUTTON_POSITIVE, positiveText, listener)
    setButton(AlertDialog.BUTTON_NEGATIVE, negativeText, listener)

    // we can either decide to cancel the coroutine if the dialog
    // itself gets cancelled, or resume the coroutine with the
    // value [false]
    setOnCancelListener { cont.cancel() }

    // if we make this coroutine cancellable, we should also close the
    // dialog when the coroutine is cancelled
    cont.invokeOnCancellation { dismiss() }

    // remember to show the dialog before returning from the block,
    // you won't be able to do it after this function is called!
    show()
}