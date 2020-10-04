package com.datnht.circleapp.utils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

fun PlayerView.initPlayer(context: Context): SimpleExoPlayer {
    val player = SimpleExoPlayer.Builder(context).build()
    this.player = player
    return player
}

/**
 * 1 source sẽ đc chia thành các source có chất lượng khác nhau tùy vào bandwidth (như youtube )
 * Thằng trackSeletor này sẽ có trách nhiệm chọn ra source quality phù hợp trong đó.
 *
 * setMaxVideoSizeSd() sd = standard def - chi cho chat luong tieu chuan hojac thap
 */
fun PlayerView.initPlayerWithAdapStream(context: Context): SimpleExoPlayer {
    val trackSelector = DefaultTrackSelector(context)
    trackSelector.setParameters(
        trackSelector.buildUponParameters().setMaxVideoSizeSd())
    val player = SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build()
    this.player = player
    return player
}

/**
 * agent: use for make HTTP req
 * Fun nay chi play 1 uri duy nhat
 */
fun buildMediaSource(context: Context, uri: Uri): MediaSource = ProgressiveMediaSource.Factory(
    DefaultDataSourceFactory(
        context,
        "agent-dattien"
    )
).createMediaSource(uri)

/**
 * Fun nay pass vafo list uri. Choi het uri nay se next cai tiep
 * ConcatenatingMediaSource chỉ nhận vararg nen ta phải convert list sang array. và convert array
 * sang vararg = kí tự *
 */
fun buildConcatMediaSource(context: Context, uris: List<Uri>): MediaSource {
    val mediaSourceFactory = ProgressiveMediaSource.Factory(
        DefaultDataSourceFactory(
            context,
            "agent-dattien"
        )
    )
    val sourceList = mutableListOf<ProgressiveMediaSource>()
    uris.forEach {
        sourceList.add(mediaSourceFactory.createMediaSource(it))
    }
    return ConcatenatingMediaSource(*sourceList.toTypedArray())
}


fun buildLoopMediaSource(context: Context, uri: Uri): MediaSource {
    val mediaSourceFactory = ProgressiveMediaSource.Factory(
        DefaultDataSourceFactory(
            context,
            "agent-dattien"
        )
    )
    return LoopingMediaSource(mediaSourceFactory.createMediaSource(uri))
}