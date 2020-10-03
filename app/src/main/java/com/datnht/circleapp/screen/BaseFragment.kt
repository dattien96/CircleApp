package com.datnht.circleapp.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.datnht.circleapp.R
import com.datnht.circleapp.utils.DialogUtils

abstract class BaseFragment: Fragment() {

    @get:LayoutRes
    abstract val layoutId: Int

    private var mAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(layoutId, container, false)
    }

    fun replaceFragment(fragment: Fragment, TAG: String?, addToBackStack: Boolean = false,
                        transit: Int = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment, TAG)

        if (transaction != null) {
            commitTransaction(transaction, addToBackStack, transit)
        }
    }

    fun addFragment(fragment: Fragment, TAG: String?, addToBackStack: Boolean = false,
                    transit: Int = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, layoutId: Int = 0) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
            ?.add(if (layoutId == 0) R.id.container else layoutId, fragment, TAG)

        if (transaction != null) {
            commitTransaction(transaction, addToBackStack, transit)
        }
    }

    fun popFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun popChildFragment(parentFragment: Fragment = this) {
        parentFragment.childFragmentManager.popBackStack()
    }

    private fun commitTransaction(transaction: FragmentTransaction, addToBackStack: Boolean = false,
                                  transit: Int = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
        if (addToBackStack) transaction.addToBackStack(null)
        if (transit != -1) transaction.setTransition(transit)
        transaction.commit()
    }

    fun showLoading() {
        hideLoading()
        mAlertDialog = DialogUtils.showLoadingDialog(activity)
    }

    fun hideLoading() {
        if (mAlertDialog != null && mAlertDialog!!.isShowing) {
            mAlertDialog?.cancel()
        }
    }
}