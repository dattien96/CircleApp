package com.datnht.circleapp.newsource

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.datnht.circleapp.R
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

enum class VideoType {
    INTRO, GAME, BIRD, PLANE, SANTA, SNOW, REINDEER
}

class MainActivity : AppCompatActivity() {

    var introPlayer: SimpleExoPlayer? = null
    var gamePlayer: SimpleExoPlayer? = null
    var birdPlayer: SimpleExoPlayer? = null
    var snowPlayer: SimpleExoPlayer? = null
    var reindeerPlayer: SimpleExoPlayer? = null
    var santaPlayer: SimpleExoPlayer? = null
    var planePlayer: SimpleExoPlayer? = null
    var currentVideoType = VideoType.INTRO

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNormalPlayerList()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, RecordSuccessScreen())
//            .commitNow()
//        return
        touch_view?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
                if (p1.actionMasked == 0) {
                    handleTouch(p1.x, p1.y)
                }
                return true
            }

        })
    }

    private fun initNormalPlayerList() {
        lifecycleScope.launch {
            introPlayer = intro_video_view.initNormalPlayerList(
                this@MainActivity,
                R.raw.intro,
                video_container,
                isLoopVideo = true
            )
            introPlayer?.awaitVideoReady()
            game_video_view?.visibility = View.INVISIBLE
            bird_video_view?.visibility = View.INVISIBLE
            reindeer_video_view?.visibility = View.INVISIBLE
            snow_video_view?.visibility = View.INVISIBLE
            santa_video_view?.visibility = View.INVISIBLE
            plane_video_view?.visibility = View.INVISIBLE

            gamePlayer = game_video_view.initNormalPlayerList(
                this@MainActivity,
                R.raw.game,
                isLoopVideo = true
            )
            gamePlayer?.awaitVideoReady()

            birdPlayer = bird_video_view.initNormalPlayerList(this@MainActivity, R.raw.bird_success)
            birdPlayer?.awaitVideoReady()

            planePlayer =
                plane_video_view.initNormalPlayerList(this@MainActivity, R.raw.bird_success)
            planePlayer?.awaitVideoReady()

            reindeerPlayer =
                reindeer_video_view.initNormalPlayerList(this@MainActivity, R.raw.reindeer_succees)
            reindeerPlayer?.awaitVideoReady()

            snowPlayer =
                snow_video_view.initNormalPlayerList(this@MainActivity, R.raw.snowman_sucess)
            snowPlayer?.awaitVideoReady()

            santaPlayer =
                santa_video_view.initNormalPlayerList(this@MainActivity, R.raw.santa_success)
            santaPlayer?.awaitVideoReady()

            introPlayer!!.playWhenReady = true
            introPlayer!!.playbackState
            loading_container?.visibility = View.GONE
        }
    }

    private fun handleTouch(xTouch: Float, yTouch: Float) {
        val ms = getCurrentVideoMs()
        val result = when (currentVideoType) {
            VideoType.INTRO -> {
                IntroJsonLocator.checkPointInRectF(xTouch, yTouch)
            }
            VideoType.GAME -> {
                GameJsonLocator.checkPointInRectF(xTouch, yTouch, ms)
            }
            else -> {
                SuccessJsonLocator.checkPointInRectF(xTouch, yTouch, ms)
            }
        }
        handleTouchResult(result)
    }

    private fun handleTouchResult(result: ItemJsonType?) {
        if (result == null) return
        when (result) {
            ItemJsonType.START -> {
                currentVideoType = VideoType.GAME
                pausePlayer(introPlayer ?: return)
                startPlayer(gamePlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.START_BACK -> {
                onBackPressed()
            }
            ItemJsonType.START_GIFT -> {
                // click gift icon
            }
            ItemJsonType.SANTA -> {
                currentVideoType = VideoType.SANTA
                pausePlayer(gamePlayer ?: return)
                startPlayer(santaPlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.BIRD -> {
                currentVideoType = VideoType.BIRD
                pausePlayer(gamePlayer ?: return)
                startPlayer(birdPlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.REINDEER -> {
                currentVideoType = VideoType.REINDEER
                pausePlayer(gamePlayer ?: return)
                startPlayer(reindeerPlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.SNOW_MAN -> {
                currentVideoType = VideoType.SNOW
                pausePlayer(gamePlayer ?: return)
                startPlayer(snowPlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.PLANE -> {
                currentVideoType = VideoType.PLANE
                pausePlayer(gamePlayer ?: return)
                startPlayer(planePlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.PLAY_AGAIN -> {
                currentVideoType = VideoType.GAME
                pausePlayer(birdPlayer ?: return)
                pausePlayer(snowPlayer ?: return)
                pausePlayer(reindeerPlayer ?: return)
                pausePlayer(santaPlayer ?: return)
                pausePlayer(planePlayer ?: return)
                startPlayer(gamePlayer ?: return)
                updateVideoDisplay()
            }
            ItemJsonType.CHANGE_GIFT -> {
                // handle change gift
            }
        }
    }

    private fun getCurrentVideoMs(): Long {
        var video = when (currentVideoType) {
            VideoType.INTRO -> introPlayer
            VideoType.GAME -> gamePlayer
            VideoType.BIRD -> birdPlayer
            VideoType.SANTA -> santaPlayer
            VideoType.SNOW -> snowPlayer
            VideoType.REINDEER -> reindeerPlayer
            VideoType.PLANE -> planePlayer
        }
        return video?.currentPosition ?: 0
    }

    private fun updateVideoDisplay() {
        intro_video_view?.visibility =
            if (currentVideoType == VideoType.INTRO) View.VISIBLE else View.INVISIBLE
        game_video_view?.visibility =
            if (currentVideoType == VideoType.GAME) View.VISIBLE else View.INVISIBLE
        bird_video_view?.visibility =
            if (currentVideoType == VideoType.BIRD) View.VISIBLE else View.INVISIBLE
        snow_video_view?.visibility =
            if (currentVideoType == VideoType.SNOW) View.VISIBLE else View.INVISIBLE
        reindeer_video_view?.visibility =
            if (currentVideoType == VideoType.REINDEER) View.VISIBLE else View.INVISIBLE
        santa_video_view?.visibility =
            if (currentVideoType == VideoType.SANTA) View.VISIBLE else View.INVISIBLE
        plane_video_view?.visibility =
            if (currentVideoType == VideoType.PLANE) View.VISIBLE else View.INVISIBLE
    }

    private fun pausePlayer(player: SimpleExoPlayer) {
        player.apply {
            playWhenReady = false
            playbackState
            seekTo(0)
        }
    }

    private fun startPlayer(player: SimpleExoPlayer) {
        player.apply {
            playWhenReady = true
            playbackState
        }
    }
}