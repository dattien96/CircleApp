package com.datnht.circleapp.screen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import com.datnht.circleapp.render3d.FirePushRender
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.layout_congratulation.view.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class PlayFragment : BaseFragment(), FirePushRender.OnRenderListener {
    companion object {
        const val TAG = "com.datnht.circleapp.screen.PlayFragment"
    }

    override val layoutId: Int
        get() = R.layout.fragment_play

    private var currentItemSelectedType: ItemType? = null

    /**
     * Get main thread for update ui
     */
    private val mainHandler: Handler? by lazy {
        activity?.mainLooper?.let {
            Handler(it)
        }
    }

    /**
     * Runner for free boost local update
     */
    private val localFreeBoostRunner: Runnable by lazy {
        Runnable {
        }
    }

    private val catchItemRunner: Runnable by lazy {
        Runnable {
            showSuccessView()
        }
    }

    /**
     * Callback from Gl thread
     */
    private val flyEndAnimation = object : FirePushRender.EndFlyAnimation {
        override fun onEndFly() {
            mainHandler?.run {
                post(localFreeBoostRunner)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fire_push_view?.setOnCatchItemListener(this)
        layout_success?.btn_play_again?.setOnClickListener {
            setMovingAnimation()
        }
    }

    override fun onDestroyView() {
        clearView()
        super.onDestroyView()
    }

    private fun clearView() {
        fire_push_view?.clearView()
    }

    private fun emitPlane1Animation() {
        fire_push_view?.playAnimation(
            flyEndAnimation
        )
    }

    override fun onCatch(itemType: ItemType) {
        currentItemSelectedType = itemType
        mainHandler?.run {
            post(catchItemRunner)
        }
    }

    override fun onInitSenceSuccess() {
        hideLoading()
        emitPlane1Animation()
    }

    override fun onEndGame() {
        mainHandler?.run {
            post{
                popFragment()
            }
        }
    }

    private fun showSuccessView() {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        val height = metrics.heightPixels
        val width = metrics.widthPixels
        layout_success?.visibility = View.VISIBLE
        var builder = layout_success.viewKonfetti.build()
            .addColors(Color.DKGRAY, Color.GREEN, Color.MAGENTA, Color.BLUE, Color.GRAY, Color.BLACK)
            .setDirection(0.0, 359.0)
            .setSpeed(8f, 12f)
            .setFadeOutEnabled(true)
            .setTimeToLive(3000L)
            .addShapes(Shape.Square)
            .addSizes(Size(3), Size(6, 3f))
            .setPosition(
                width / 2f,
                height / 2f + 100f
            )

        builder.burst(1000)
        builder.streamFor(400, 3000L)
    }

    private fun setMovingAnimation() {
        var sourceView = layout_success?.image_success ?: return
        var targetView = layout_success?.image_gift ?: return

        var translationXValue = targetView.x - sourceView.x
        var translationYValue = targetView.y - sourceView.y - sourceView.height

        val translationXAnimator = ObjectAnimator.ofFloat(translationXValue).apply {
            duration = 500
            addUpdateListener {
                sourceView.translationX = it.animatedValue as Float
            }
        }
        val translationYAnimator = ObjectAnimator.ofFloat(translationYValue).apply {
            duration = 500
            addUpdateListener { animation ->
                sourceView.translationY = animation.animatedValue as Float
            }
        }
        val scaleYAnimator = ObjectAnimator.ofFloat(sourceView, "scaleY", 1f, 0.5f, 0f).apply {
            duration = 500
        }
        val scaleXAnimator = ObjectAnimator.ofFloat(sourceView, "scaleX", 1f, 0.5f, 0f).apply {
            duration = 500
        }

        var movingAnimatorSet = AnimatorSet()
        movingAnimatorSet.play(translationXAnimator).with(translationYAnimator).with(scaleXAnimator)
            .with(scaleYAnimator)
        movingAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                layout_success?.viewKonfetti?.stopGracefully()
                layout_success?.visibility = View.GONE
                sourceView.translationX = 0f
                sourceView.translationY = 0f
                sourceView.scaleX = 1f
                sourceView.scaleY = 1f
                emitPlane1Animation()
            }
        })
        movingAnimatorSet.start()
    }
}