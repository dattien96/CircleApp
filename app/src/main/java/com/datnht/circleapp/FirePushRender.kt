package com.datnht.circleapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLU
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.datnht.circleapp.`object`.*
import org.rajawali3d.Object3D
import org.rajawali3d.animation.*
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
import kotlin.collections.HashMap

class FirePushRender constructor(context: Context) : Renderer(context), OnObjectPickedListener {
    companion object {
        private const val TAG = "FIRE_RENDERER"
        private const val BOTTOM_RAJAWALY_Y_COR = -1.5
        val DEFAULT_CONFIG = Config(5f, 2f, 1f)

    }

    private var config = DEFAULT_CONFIG

    private val cache = SparseArray<Material>()
    private val random = Random()
    private var mPicker: ObjectColorPicker? = null
    private var mSelectedObject: Object3D? = null
    private val objectIDs = mutableListOf(
        BaseObject3D.ITEM_PLANE_LEFT_ID,
        BaseObject3D.ITEM_PLANE_RIGHT_ID,
        BaseObject3D.ITEM_REINDEER_ID,
        BaseObject3D.ITEM_REINDEER_INTREE_ID,
        BaseObject3D.ITEM_SANTA_ID,
        BaseObject3D.ITEM_SNOWMAN_INTREE_ID,
        BaseObject3D.ITEM_SNOWMAN_LEFT_ID,
        BaseObject3D.ITEM_SNOWMAN_RIGHT_ID,
        BaseObject3D.ITEM_TREE_ID
    )
    private val matMap = HashMap<Int, Material>()
    private val objectMap = HashMap<Int, BaseObject3D>()
    var onRenderListener: OnRenderListener? = null

    init {
        mContext = context
        frameRate = 60.0
    }

    fun emitItem() {

//        val w = config.sizeCoeff
//
//        val dimenW: Float
//        val dimenH: Float
//        if (width >= height) {
//            dimenW = w
//            dimenH = height.toFloat() / width.toFloat() * dimenW
//        } else {
//            dimenH = w
//            dimenW = width.toFloat() / height.toFloat() * dimenH
//        }


        var santas = objectMap[BaseObject3D.ITEM_SANTA_ID]
        var santaPoint = getPointSprite(BaseObject3D.ITEM_SANTA_ID)

        var planeLeft = objectMap[BaseObject3D.ITEM_PLANE_LEFT_ID]
        var planeLeftPoint = getPointSprite(BaseObject3D.ITEM_PLANE_LEFT_ID)

        var planeRight = objectMap[BaseObject3D.ITEM_PLANE_RIGHT_ID]
        var planeRightPoint = getPointSprite(BaseObject3D.ITEM_PLANE_RIGHT_ID)

        var reindeer = objectMap[BaseObject3D.ITEM_REINDEER_ID]
        var reindeerPoint = getPointSprite(BaseObject3D.ITEM_REINDEER_ID)

        var reindeerInTree = objectMap[BaseObject3D.ITEM_REINDEER_INTREE_ID]
        var reindeerInTreePoint = getPointSprite(BaseObject3D.ITEM_REINDEER_INTREE_ID)

        var snowManInTree = objectMap[BaseObject3D.ITEM_SNOWMAN_INTREE_ID]
        var snowManInTreePoint = getPointSprite(BaseObject3D.ITEM_SNOWMAN_INTREE_ID)

        var snowManLeft = objectMap[BaseObject3D.ITEM_SNOWMAN_LEFT_ID]
        var snowManLeftPoint = getPointSprite(BaseObject3D.ITEM_SNOWMAN_LEFT_ID)

        var snowManRight = objectMap[BaseObject3D.ITEM_SNOWMAN_RIGHT_ID]
        var snowManRightPoint = getPointSprite(BaseObject3D.ITEM_SNOWMAN_RIGHT_ID)

        currentScene.addChild(snowManInTreePoint.apply { isVisible = false })
        currentScene.addChild(reindeerInTreePoint.apply { isVisible = false })
        currentScene.addChild(santaPoint.apply { isVisible = false })
        currentScene.addChild(planeLeftPoint.apply { isVisible = false })
        currentScene.addChild(planeRightPoint.apply { isVisible = false })
        currentScene.addChild(reindeerPoint.apply { isVisible = false })
        currentScene.addChild(snowManLeftPoint.apply { isVisible = false })
        currentScene.addChild(snowManRightPoint.apply { isVisible = false })

        AnimationGroup().apply {

            addAnimation(
                santas?.getAnimation(santaPoint, {
                    currentScene.removeChild(santaPoint)
                }, get3DCoor(santas.itemType, santas.startPoint, santas.endPoint))
            )

//            addAnimation(
//                getPlaneAnimation(
//                    planeLeft?.getAnimation(planeLeftPoint, {
//                        currentScene.removeChild(planeLeftPoint)
//                    }, get3DCoor(planeLeft.itemType, planeLeft.startPoint, planeLeft.endPoint)),
//                    planeRight?.getAnimation(planeRightPoint, {
//                        currentScene.removeChild(planeRightPoint)
//                    }, get3DCoor(planeRight.itemType, planeRight.startPoint, planeRight.endPoint))
//                )
//            )
//
//            addAnimation(
//                getReindeerAnimation(
//                    reindeer?.getAnimation(reindeerPoint, {
//                        currentScene.removeChild(reindeerPoint)
//                    }, get3DCoor(reindeer.itemType, reindeer.startPoint, reindeer.endPoint)),
//                    reindeerInTree?.getAnimation(reindeerInTreePoint, {
//                        currentScene.removeChild(reindeerInTreePoint)
//                    }, get3DCoor(reindeerInTree.itemType, reindeerInTree.startPoint, reindeerInTree.endPoint))
//                )
//            )
//
            addAnimation(
                getSnowManAnimation(
                    snowManInTree?.getAnimation(snowManInTreePoint, {
                        currentScene.removeChild(snowManInTreePoint)
                    }, get3DCoor(snowManInTree.itemType, snowManInTree.startPoint, snowManInTree.endPoint)),
                    snowManLeft?.getAnimation(snowManLeftPoint, {
                        currentScene.removeChild(snowManLeftPoint)
                    }, get3DCoor(snowManLeft.itemType, snowManLeft.startPoint, snowManLeft.endPoint)),
                    snowManRight?.getAnimation(snowManRightPoint, {
                        currentScene.removeChild(snowManRightPoint)
                    }, get3DCoor(snowManRight.itemType, snowManRight.startPoint, snowManRight.endPoint))
                )
            )

            /*     // Use this anim for make transparent effect
                 addAnimation(ColorAnimation3D(
                     getRdColor(),
                     ContextCompat.getColor(context, R.color.transparent)
                 ).apply {
                     durationMilliseconds = (randF * 6500.0f * config.floatingTimeCoeff).toLong()
                     transformable3D = pointSprite
                 })*/
            currentScene.registerAnimation(this)
            play()
        }
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

        var z1 = 0.0
        var z2 = 0.0
        var z3 = 0.0

        when (itemType) {
            ItemType.LEFT_PLANE, ItemType.RIGHT_PLANE -> {
                z1 = 1.0
                z2 = 2.0
                z3 = 2.5
            }
            ItemType.REINDEER -> {
                z3 = 0.0
                z2 = 2.0
                z1 = 2.5
            }
            ItemType.REINDEER_IN_TREE -> {}
            ItemType.SANTA -> {}
            ItemType.SNOWMAN_INTREE -> {}
            ItemType.SNOWMAN_RIGHT -> {}
            ItemType.SNOWMAN_LEFT -> {}
            else -> {}
        }
        return CubicBezierCurve3D(
            Vector3(
                startX,
                endX,
                z1
            ),
            Vector3(
                convertXCor2dTo3d(startPoint[0].toDouble()),
                convertYCor2dTo3d(startPoint[1].toDouble()),
                z1
            ),
            Vector3(
                (startX + startY) * 0.5,
                (endX + endY) * 0.5,
                z2
            ),
            Vector3(
                startY,
                endY,
                z3
            )
        )
    }

    override fun initScene() {
        currentCamera.z = 4.2
        mPicker = ObjectColorPicker(this)
        mPicker?.setOnObjectPickedListener(this)

        objectIDs.forEach {
            var obj = getBaseItemObject(it)
            objectMap[it] = obj
            val mat = cache[it] ?: initMaterial(obj.getBitmap(config.resource!!), it)
            mat.textureList[0].influence = 1f
            matMap[it] = mat
        }
//        addTreeItem()
        onRenderListener?.onInitSenceSuccess()
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) = Unit

    fun getObjectAt(x: Float, y: Float) {
        mPicker?.getObjectAt(x, y)
    }

    override fun onTouchEvent(motionEvent: MotionEvent) {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                getObjectAt(motionEvent.getX(), motionEvent.getY())
            }
        }
    }

    fun addTreeItem() {
        var mat: Material? = null
        if (cache[BaseObject3D.ITEM_TREE_ID] == null) {
            val bitmap = BitmapFactory.decodeResource(
                config.resource,
                R.drawable.tree
            )
            mat = initMaterial(bitmap, BaseObject3D.ITEM_TREE_ID)
            cache.put(BaseObject3D.ITEM_TREE_ID, mat)
        } else {
            mat = cache[BaseObject3D.ITEM_TREE_ID]
        }

        val pointSprite = PointSprite(1f, 1f).apply {
            material = mat
            this.name = ItemType.TREE.name
            this.position = Vector3(
                convertXCor2dTo3d(200.0),
                convertYCor2dTo3d(1800.0),
                0.0
            )
        }
        currentScene.addChild(pointSprite)
    }

    fun getBaseItemObject(id: Int): BaseObject3D {
        val displayMetrics = DisplayMetrics()
        (context as? MainActivity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return when (id) {
            BaseObject3D.ITEM_PLANE_LEFT_ID -> PlaneLeft(displayMetrics)
            BaseObject3D.ITEM_PLANE_RIGHT_ID -> PlaneRight(displayMetrics)
            BaseObject3D.ITEM_SANTA_ID -> Santas(displayMetrics)
            BaseObject3D.ITEM_SNOWMAN_RIGHT_ID -> SnowManRight(displayMetrics)
            BaseObject3D.ITEM_SNOWMAN_LEFT_ID -> SnowManLeft(displayMetrics)
            BaseObject3D.ITEM_SNOWMAN_INTREE_ID -> SnowManInTree(displayMetrics)
            BaseObject3D.ITEM_REINDEER_ID -> Reindeer(displayMetrics)
            BaseObject3D.ITEM_REINDEER_INTREE_ID -> ReindeerInTree(displayMetrics)
            else -> Tree()
        }
    }

    fun getPointSprite(id: Int): PointSprite {
        val pointSprite = PointSprite(config.sizeCoeff, config.sizeCoeff).apply {
            material = matMap[id]
            isTransparent = false
            this.name = getBaseItemObject(id).itemType.name
        }

        if (id != BaseObject3D.ITEM_SANTA_ID && id != BaseObject3D.ITEM_TREE_ID) {
            mPicker?.registerObject(pointSprite)
        }

        return pointSprite
    }

    data class Config(
        val xMax: Float,
        val floatingTimeCoeff: Float,
        val sizeCoeff: Float,
        val resource: Resources? = null,
        val displayMetrics: DisplayMetrics? = null
    )

    interface EndFlyAnimation {
        fun onEndFly()
    }

    interface OnRenderListener {
        fun onCatch(itemType: ItemType)
        fun onInitSenceSuccess()
    }

    override fun onNoObjectPicked() {
    }

    override fun onObjectPicked(`object`: Object3D) {
        mSelectedObject = `object`
        mPicker?.unregisterObject(`object`)
        currentScene.clearChildren()
        onRenderListener?.onCatch(convertNameToItemType(`object`.name))
    }

    fun convertNameToItemType(name: String) = when(name) {
        ItemType.LEFT_PLANE.name -> ItemType.LEFT_PLANE
        ItemType.RIGHT_PLANE.name -> ItemType.RIGHT_PLANE
        ItemType.SANTA.name -> ItemType.SANTA
        ItemType.REINDEER_IN_TREE.name -> ItemType.REINDEER_IN_TREE
        ItemType.REINDEER.name -> ItemType.REINDEER
        ItemType.SNOWMAN_LEFT.name -> ItemType.SNOWMAN_LEFT
        ItemType.SNOWMAN_RIGHT.name -> ItemType.SNOWMAN_RIGHT
        ItemType.SNOWMAN_INTREE.name -> ItemType.SNOWMAN_INTREE
        else -> ItemType.TREE
    }
}

enum class ItemType {
    LEFT_PLANE, RIGHT_PLANE, SANTA, TREE, REINDEER, REINDEER_IN_TREE, SNOWMAN_LEFT, SNOWMAN_RIGHT, SNOWMAN_INTREE
}