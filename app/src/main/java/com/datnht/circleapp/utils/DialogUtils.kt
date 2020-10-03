package com.datnht.circleapp.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.datnht.circleapp.R

object DialogUtils {

    fun createLoadingDialog(context: Context?, cancelable: Boolean = false,
                            canceledOnTouchOutside: Boolean = false): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).setView(R.layout.progress_dialog).create().apply {
            setCancelable(cancelable)
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun showLoadingDialog(context: Context?, cancelable: Boolean = false,
                          canceledOnTouchOutside: Boolean = false): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog? = createLoadingDialog(context, cancelable)
        dialog?.show()
        return dialog
    }

    fun showMessage(context: Context?, title: String? = null, message: String? = null,
                    textPositive: String? = null, textNegative: String? = null,
                    callback: DialogCallback? = null, cancelable: Boolean = false): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog =
                AlertDialog.Builder(context).apply {
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton(textPositive, callback?.onPositiveListener())
                    setNegativeButton(textNegative, callback?.onNegativeListener())
                    setCancelable(cancelable)
                }.create()
        dialog.show()
        return dialog
    }

    fun showMessage(context: Context?, title: Int? = null, message: Int? = null,
                    textPositive: Int? = null, textNegative: Int? = null, callback: DialogCallback? = null,
                    cancelable: Boolean = false): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog = AlertDialog.Builder(context).apply {
            if (title != null) setTitle(title)
            if (message != null) setMessage(message)
            if (textPositive != null) {
                setPositiveButton(textPositive, callback?.onPositiveListener())
            }
            if (textNegative != null) {
                setNegativeButton(textNegative, callback?.onNegativeListener())
            }
            setCancelable(cancelable)
        }.create()
        dialog.show()
        return dialog
    }

    fun createMessage(context: Context?, title: Int? = null, message: Int? = null,
                    textPositive: Int? = null, textNegative: Int? = null, callback: DialogCallback? = null,
                    cancelable: Boolean = false): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).apply {
            if (title != null) setTitle(title)
            if (message != null) setMessage(message)
            if (textPositive != null) {
                setPositiveButton(textPositive, callback?.onPositiveListener())
            }
            if (textNegative != null) {
                setNegativeButton(textNegative, callback?.onNegativeListener())
            }
            setCancelable(cancelable)
        }.create()
    }

    fun showCustomDialog(context: Context?, title: String? = null, message: String? = null,
                         textPositive: String? = null, textNegative: String? = null,
                         callback: DialogCallback? = null, cancelable: Boolean = false,
                         customShowListener: DialogInterface.OnShowListener): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog =
            AlertDialog.Builder(context).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(textPositive, callback?.onPositiveListener())
                setNegativeButton(textNegative, callback?.onNegativeListener())
                setCancelable(cancelable)
            }.create()
        dialog.apply {
            dialog.findViewById<TextView>(android.R.id.message)?.apply {
                gravity = Gravity.CENTER
                width = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            setOnShowListener(customShowListener)
            show()
        }
        return dialog
    }

    fun showMessage2(
        context: Context?, title: String? = null, message: String? = null,
        textPositive: String? = null, positiveListener: (() -> Unit)? = null,
        textNegative: String? = null, negativeListener: (() -> Unit)? = null,
        cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textPositive) { dialog, which ->
                positiveListener?.invoke()
            }
            setNegativeButton(textNegative) { dialog, which ->
                negativeListener?.invoke()
            }
            setCancelable(cancelable)

        }.create().apply {
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            show()
        }
    }

    fun showMessage2(
        context: Context?, title: Int? = null, message: Int? = null,
        textPositive: Int? = null, positiveListener: (() -> Unit)? = null,
        textNegative: Int? = null, negativeListener: (() -> Unit)? = null,
        cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false
    ): AlertDialog? {
        if (context == null) return null
        return AlertDialog.Builder(context).apply {
            if (title != null) setTitle(title)
            if (message != null) setMessage(message)
            if (textPositive != null) {
                setPositiveButton(textPositive) { dialog, which ->
                    positiveListener?.invoke()
                }
            }
            if (textNegative != null) {
                setNegativeButton(textNegative) { dialog, which ->
                    negativeListener?.invoke()
                }
            }
            setCancelable(cancelable)
        }.create().apply {
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            show()
        }
    }

    fun showNotPremiumDialog(
        context: Context?,
        message: String? = null,
        textPositive: String? = null,
        textNegative: String? = null,
        cancelable: Boolean = false,
        dialogCallback: DialogCallback? = null
    ): AlertDialog? {
        return showCustomDialog(context = context, message = message,
            textPositive = textPositive,
            textNegative = textNegative,
            cancelable = cancelable,
            customShowListener = DialogInterface.OnShowListener {
                (it as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).apply {
                    setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    setTextColor(ContextCompat.getColor(context!!, android.R.color.black))
                    val positiveButtonParam =
                        layoutParams as LinearLayout.LayoutParams
                    positiveButtonParam.gravity = Gravity.CENTER
                    positiveButtonParam.weight = 2.5f
                    layoutParams = positiveButtonParam
                }

                it.getButton(DialogInterface.BUTTON_NEGATIVE).apply {
                    setTextColor(ContextCompat.getColor(context!!, android.R.color.black))
                    val negativeButtonParam =
                        layoutParams as LinearLayout.LayoutParams
                    negativeButtonParam.gravity = Gravity.CENTER
                    negativeButtonParam.weight = 1.0f
                    layoutParams = negativeButtonParam
                }
            },
            callback = dialogCallback
        )
    }
}

abstract class DialogCallback {
    open fun onNegativeListener(): DialogInterface.OnClickListener? {
        return null
    }

    open fun onPositiveListener(): DialogInterface.OnClickListener? {
        return null
    }
}
