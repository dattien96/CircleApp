package com.datnht.circleapp.newsource

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.view.OneShotPreDrawListener
import com.datnht.circleapp.R
import com.datnht.circleapp.utils.buildLoopMediaSource
import com.datnht.circleapp.utils.buildMediaSource
import com.datnht.circleapp.utils.initPlayer
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun PlayerView.initNormalPlayerList(
    context: Context,
    @RawRes rawVideo: Int,
    parentView: View? = null,
    isPlayWhenReady: Boolean = false,
    isLoopVideo: Boolean = false
): SimpleExoPlayer {
    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    val player = this.initPlayer(context)
    player.playWhenReady = isPlayWhenReady
    val gameUri = RawResourceDataSource.buildRawResourceUri(rawVideo)

    val gameMediaSource =
        if (isLoopVideo) buildLoopMediaSource(context, gameUri)
        else buildMediaSource(context, gameUri)


    player.apply {
        parentView?.let {
            addVideoListener(object : VideoListener {
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) {
                    val p: ViewGroup.LayoutParams = it.layoutParams
                    val currWidth: Int = this@initNormalPlayerList.width

                    // Set new width/height of view
                    // height or width must be cast to float as int/int will give 0
                    // and distort view, e.g. 9/16 = 0 but 9.0/16 = 0.5625.
                    // p.height is int hence the final cast to int.

                    // Set new width/height of view
                    // height or width must be cast to float as int/int will give 0
                    // and distort view, e.g. 9/16 = 0 but 9.0/16 = 0.5625.
                    // p.height is int hence the final cast to int.
                    p.width = currWidth
                    p.height = (height.toFloat() / width * currWidth).toInt()

                    // Redraw myView

                    // Redraw myView
                    this@initNormalPlayerList.requestLayout()
                }
            })
        }
        prepare(gameMediaSource, false, false)

    }
    return player
}

suspend fun SimpleExoPlayer.awaitVideoReady() = suspendCancellableCoroutine<Unit> { cont ->
    val listener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            Log.d("asdasd", "state = $playbackState")
            if (playbackState == Player.STATE_READY) {
                removeListener(this)
                cont.resume(Unit)
            }
        }
    }
    cont.invokeOnCancellation { this.removeListener(listener) }
    addListener(listener)
}
