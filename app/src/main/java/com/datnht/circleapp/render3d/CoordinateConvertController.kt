package com.datnht.circleapp.render3d

import android.opengl.GLU
import android.util.DisplayMetrics
import org.rajawali3d.cameras.Camera
import org.rajawali3d.math.vector.Vector3

/**
 * Convert screen 2d point to 3d vector world
 */
fun screenToWorld(
    x: Float,
    y: Float,
    viewportWidth: Int,
    viewportHeight: Int,
    currentCamera: Camera
): Vector3 {
    val r1 = FloatArray(16)
    val viewport = intArrayOf(0, 0, viewportWidth, viewportHeight)

    GLU.gluUnProject(
        x,
        viewportHeight - y,
        0.0f,
        currentCamera.viewMatrix.floatValues,
        0,
        currentCamera.projectionMatrix.floatValues,
        0,
        viewport,
        0,
        r1,
        0
    )
    return Vector3((r1[0]).toDouble(), r1[1].toDouble(), r1[2].toDouble())
}

/**
 * Use current width screen to convert X from 2d -> 3d
 */
fun convertXCor2dTo3d(displayMetrics: DisplayMetrics?, x: Double): Double {
    displayMetrics?.let {
        return x * 2.0 / it.widthPixels - 1.0
    }
    return x
}

/**
 * Use current height screen to convert Y from 2d -> 3d
 */
fun convertYCor2dTo3d(displayMetrics: DisplayMetrics?, y: Double): Double {
    displayMetrics?.let {
        return 1.5 * (it.heightPixels - 2 * y) / it.heightPixels
    }
    return y
}