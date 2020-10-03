package com.datnht.circleapp.screen

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.core.animation.addListener
import com.datnht.circleapp.R
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment: BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_start

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startPlayButtonAnimation()
        button_start.setOnClickListener {
            replaceFragment(PlayFragment(), PlayFragment.TAG, true)
        }
    }

    private fun startPlayButtonAnimation() {
        ObjectAnimator.ofFloat(1f, 0.5f, 0f, 1f).apply {
            duration = 1000
            addUpdateListener { animator ->
                button_start?.alpha = animator.animatedValue as Float
            }
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }
}