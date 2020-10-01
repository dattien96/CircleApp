package com.datnht.circleapp

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLU
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import org.rajawali3d.Object3D
import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.IAnimationListener
import org.rajawali3d.animation.SplineTranslateAnimation3D
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.materials.Material
import org.rajawali3d.materials.methods.DiffuseMethod
import org.rajawali3d.materials.textures.ATexture
import org.rajawali3d.materials.textures.Texture
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.PointSprite
import org.rajawali3d.renderer.Renderer
import org.rajawali3d.util.ObjectColorPicker
import org.rajawali3d.util.OnObjectPickedListener
import java.util.*

class FirePushRender constructor(context: Context) : Renderer(context), OnObjectPickedListener {

    private var config = DEFAULT_CONFIG

    private val cache = SparseArray<Material>()
    private val random = Random()

    init {
        mContext = context
        frameRate = 60.0
    }


    fun emitItem(
        bitmap: Bitmap,
        itemType: ItemType,
        width: Int,
        height: Int,
        id: Int,
        yMax: Float,
        startPoint: ArrayList<Float>,
        endPoint: ArrayList<Float>,
        flyEndCallBack: EndFlyAnimation? = null
    ) {
        if (startPoint.size < 2 || endPoint.size < 2) return

        val mat = cache[id] ?: initMaterial(bitmap, id)
        mat.textureList[0].influence = 1f
        val randSign = if (random.nextBoolean()) 1 else -1

        val startVector =
            screenToWorld(startPoint[0], startPoint[1], viewportWidth, viewportHeight).apply {
                z = 1.0
            }
        val endVector =
            screenToWorld(endPoint[0], endPoint[1], viewportWidth, viewportHeight).apply {

            }

        val cubicBezierCurve3D = get3DCoor(itemType, startPoint, endPoint)
//        val cubicBezierCurve3D = CubicBezierCurve3D(
//            startVector,
//            startVector,
//            Vector3(
//                (startVector.x + endVector.x) * 0.5,
//                (startVector.y + endVector.y) * 0.5,
//                2.0
//            ),
//            endVector.apply {
//                z = 2.5
//            }
//        )

        val w = config.sizeCoeff

        val dimenW: Float
        val dimenH: Float
        if (width >= height) {
            dimenW = w
            dimenH = height.toFloat() / width.toFloat() * dimenW
        } else {
            dimenH = w
            dimenW = width.toFloat() / height.toFloat() * dimenH
        }

        val pointSprite = PointSprite(dimenW, dimenH).apply {
            material = mat
            isTransparent = true
            this.name = itemType.name
        }

        mPicker?.registerObject(pointSprite)
        currentScene.addChild(pointSprite)

        AnimationGroup().apply {
//            addAnimation(ScaleAnimation3D(Vector3(0.3, 0.3, 0.0)).apply {
//                durationMilliseconds = (randF * 5000.0f * config.floatingTimeCoeff - 100).toLong()
//                transformable3D = pointSprite
//            })

            addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
                durationMilliseconds = config.floatingTimeCoeff.toLong()
                transformable3D = pointSprite
                interpolator = OvershootInterpolator()
                registerListener(object : IAnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) = Unit

                    override fun onAnimationEnd(animation: Animation?) {
                        currentScene.removeChild(pointSprite)
                        animation?.unregisterListener(this)
                        flyEndCallBack?.onEndFly()
                    }

                    override fun onAnimationStart(animation: Animation?) = Unit

                    override fun onAnimationUpdate(
                        animation: Animation?,
                        interpolatedTime: Double
                    ) {

                    }
                })
            })

            /*     // Use this anim for make transparent effect
                 addAnimation(ColorAnimation3D(
                     getRdColor(),
                     ContextCompat.getColor(context, R.color.transparent)
                 ).apply {
                     durationMilliseconds = (randF * 6500.0f * config.floatingTimeCoeff).toLong()
                     transformable3D = pointSprite
                 })*/
            currentScene.registerAnimation(this)

        }.play()
    }

    /**
     * Convert screen 2d point to 3d vector world
     */
    private fun screenToWorld(
        x: Float,
        y: Float,
        viewportWidth: Int,
        viewportHeight: Int
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
     * Clear fire object has been created
     */
    fun clearChildrenScene() {
        currentScene.clearChildren()
    }

    private fun getRdColor() = ContextCompat.getColor(context, R.color.colorAccent)

    fun applyConfig(newConfig: Config) {
        config = newConfig
    }

    fun getConfig() = config

    private fun initMaterial(bitmap: Bitmap, id: Int): Material {
        val mat = Material().apply {
            diffuseMethod = DiffuseMethod.Lambert()
            color = 0
        }
        try {
            // use AlphaMapTexture for change bitmap's color to single color with colorAnim
            // and use Texture use for keep origin color of bitmap
            mat.addTexture(Texture("Earth$id", Bitmap.createBitmap(bitmap)))
        } catch (e: ATexture.TextureException) {
            Log.e(TAG, e.toString())
        }

        cache.put(id, mat)
        return mat
    }

    /**
     * Use current width screen to convert X from 2d -> 3d
     */
    private fun convertXCor2dTo3d(x: Double): Double {
        config.displayMetrics?.let {
            return x * 2.0 / it.widthPixels - 1.0
        }
        return x
    }

    /**
     * Use current height screen to convert Y from 2d -> 3d
     */
    private fun convertYCor2dTo3d(y: Double): Double {
        config.displayMetrics?.let {
            return 1.5 * (it.heightPixels - 2 * y) / it.heightPixels
        }
        return y
    }

    private fun get3DCoor(
        itemType: ItemType, startPoint: ArrayList<Float>,
        endPoint: ArrayList<Float>
    ): CubicBezierCurve3D {

        val startX = convertXCor2dTo3d(startPoint[0].toDouble())
        val endX = convertYCor2dTo3d(startPoint[1].toDouble())
        val startY = convertXCor2dTo3d(endPoint[0].toDouble())
        val endY = convertYCor2dTo3d(endPoint[1].toDouble())
        return CubicBezierCurve3D(
            Vector3(
                startX,
                endX,
                1.0
            ),
            Vector3(
                convertXCor2dTo3d(startPoint[0].toDouble()),
                convertYCor2dTo3d(startPoint[1].toDouble()),
                1.0
            ),
            Vector3(
                (startX + startY) * 0.5,
                (endX + endY) * 0.5,
                2.0
            ),
            Vector3(
                startY,
                endY,
                2.5
            )
        )
    }

    override fun initScene() {
        currentCamera.z = 4.2
        mPicker = ObjectColorPicker(this)
        mPicker?.setOnObjectPickedListener(this)
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) = Unit
    private var mPicker: ObjectColorPicker? = null
    private var mSelectedObject: Object3D? = null

    fun getObjectAt(x: Float, y: Float) = mPicker?.getObjectAt(x, y)

    override fun onTouchEvent(motionEvent: MotionEvent) {


        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
//                Log.d("asdasd", "x = ${motionEvent.x}; y = ${motionEvent.y} num = ${currentScene.numChildren}")
                val x = getObjectAt(motionEvent.getX(), motionEvent.getY())
                val child = this.currentScene.childrenCopy
//                for (i in 0 until currentScene.numChildren) {
//                    val child = this.currentScene.childrenCopy
//                    val boundingSphere: BoundingSphere = child.getGeometry().getBoundingSphere()
//                    boundingSphere.transform(child.getModelMatrix())
//                    val position: Vector3 = boundingSphere.getPosition()
//                    val radius: Double = boundingSphere.getRadius()
//                    val hitPoint = Vector3()
//                    val found: Boolean =
//                        Intersector.intersectRaySphere(rayStart, rayEnd, position, radius, hitPoint)
//                    if (found) {
//                        Log.d(getLocalClassName(), "contact at $hitPoint")
//                    }
//                }
//                start.setX(motionEvent.x / viewportWidth.toDouble())
//                start.setY(motionEvent.y / viewportHeight.toDouble())
            }
        }
    }

    data class Config(
        val xMax: Float,
        val floatingTimeCoeff: Float,
        val sizeCoeff: Float,
        val displayMetrics: DisplayMetrics? = null
    )

    companion object {
        private const val TAG = "FIRE_RENDERER"
        private const val BOTTOM_RAJAWALY_Y_COR = -1.5
        val DEFAULT_CONFIG = Config(5f, 2f, 1f)
    }

    interface EndFlyAnimation {
        fun onEndFly()
    }

    override fun onNoObjectPicked() {

    }

    override fun onObjectPicked(`object`: Object3D) {
        mSelectedObject = `object`
        mPicker?.unregisterObject(`object`)
        currentScene.removeChild(`object`)
        Log.d("asdasd", "chon dung plane ${mSelectedObject?.name}")
    }
}

enum class ItemType {
    LEFT_PLANE, RIGHT_PLANE, SANTA
}