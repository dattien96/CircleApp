package com.datnht.circleapp.render3d

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import com.datnht.circleapp.`object`.type.AppMaterialIdLocator.objectIDs
import com.datnht.circleapp.`object`.type.AppMaterialIdLocator.objectIDsWithTree
import com.datnht.circleapp.newsource.MainActivity
import com.datnht.circleapp.`object`.*
import com.datnht.circleapp.`object`.type.ItemType
import org.rajawali3d.Object3D
import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.IAnimationListener
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.materials.Material
import org.rajawali3d.materials.methods.DiffuseMethod
import org.rajawali3d.materials.plugins.AlphaMaskMaterialPlugin
import org.rajawali3d.materials.textures.*
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.*
import org.rajawali3d.renderer.Renderer
import org.rajawali3d.util.ObjectColorPicker
import org.rajawali3d.util.OnObjectPickedListener
import java.util.*
import kotlin.collections.HashMap

class FirePushRender constructor(context: Context) : Renderer(context), OnObjectPickedListener {
    companion object {
        private const val TAG = "FIRE_RENDERER"
        private const val BOTTOM_RAJAWALY_Y_COR = -1.5
        val DEFAULT_CONFIG =
            Config(5f, 2f, 1f)

    }

    private var config = DEFAULT_CONFIG

    private val cache = SparseArray<Material>()
    private var mPicker: ObjectColorPicker? = null
    private var mSelectedObject: Object3D? = null
    private val matMap = HashMap<Int, Material>()
    private val objectMap = HashMap<Int, BaseObject3D>()
    var onRenderListener: OnRenderListener? = null
    var currentAnimation: AnimationGroup? = null

    init {
        mContext = context
        frameRate = 60.0
    }

    override fun initScene() {
        currentCamera.z = 4.2
        mPicker = ObjectColorPicker(this)
        mPicker?.setOnObjectPickedListener(this)

        objectIDsWithTree.forEach {
            var obj = getBaseItemObject(it)
            objectMap[it] = obj
            val mat = initMaterial(obj.bitMapRes, it)
            mat.textureList[0].influence = 1f
            matMap[it] = mat
        }

        onRenderListener?.onInitSenceSuccess()
    }

    override fun onNoObjectPicked() {
    }

    override fun onObjectPicked(`object`: Object3D) {
        mSelectedObject = `object`
        mPicker?.unregisterObject(`object`)
        currentScene.clearChildren()
        currentScene.unregisterAnimation(currentAnimation)
        onRenderListener?.onCatch(convertNameToItemType(`object`.name))
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) = Unit

    override fun onTouchEvent(motionEvent: MotionEvent) {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                getObjectAt(motionEvent.getX(), motionEvent.getY())
            }
        }
    }

    fun emitItem() {
        currentAnimation?.let {
            currentScene.unregisterAnimation(currentAnimation)
            currentAnimation = null
        }

        var animResult = mutableListOf<HashMap<Int, AnimationGroup>>()
        var animMap = HashMap<Int, AnimationGroup>()
        val pointMap = HashMap<Int, Object3D>()

        objectIDs.forEach {
            var obj = objectMap[it] ?: return@forEach
            var point = getPointSprite(it)
            pointMap[it] = point
            var anim = obj.getAnimation(point, {
                currentScene.removeChild(point)
            }, get3DCoor(obj.itemType, obj.startPoint, obj.endPoint))
            animMap[it] = anim
            currentScene.addChild(pointMap[it]?.apply {
                isVisible = false })
        }
        animResult.add(animMap)

        animMap = HashMap<Int, AnimationGroup>()
        objectIDsWithTree.forEach {
            var obj = objectMap[it] ?: return@forEach
            var point = getPointSprite(it)
            pointMap[it] = point
            var anim = obj.getAnimation(point, {
                if (it != BaseObject3D.ITEM_TREE_ID) currentScene.removeChild(point)
            }, get3DCoor(obj.itemType, obj.startPoint, obj.endPoint))
            animMap[it] = anim
            currentScene.addChild(pointMap[it]?.apply { isVisible = false })
        }
        animResult.add(animMap)

        currentAnimation = composeAnimation(
            animResult,
            objectIDs
        ).apply {
            currentScene.registerAnimation(this)
            registerListener(object: IAnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    onRenderListener?.onEndGame()
                    unregisterListener(this)
                }

                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationUpdate(animation: Animation?, interpolatedTime: Double) {

                }

            })
//            repeatMode = Animation.RepeatMode.INFINITE
            play()
        }
    }

    /**
     * Clear fire object has been created
     */
    fun clearChildrenScene() {
        currentScene.clearChildren()
    }

    fun applyConfig(newConfig: Config) {
        config = newConfig
    }

    fun getConfig() = config

    private fun initMaterial(res: Int, id: Int): Material {
        val plugin = AlphaMaskMaterialPlugin(0.125f)
        val mat = Material().apply {
            diffuseMethod = DiffuseMethod.Lambert()
            enableLighting(true)
            colorInfluence = 0f
            color = 0
            addPlugin(plugin)
        }
        try {
            // use AlphaMapTexture for change bitmap's color to single color with colorAnim
            // and use Texture use for keep origin color of bitmap
            mat.addTexture(Texture("Item$id", res))
        } catch (e: ATexture.TextureException) {
            Log.e(TAG, e.toString())
        }

        cache.put(id, mat)
        return mat
    }

    private fun get3DCoor(
        itemType: ItemType, startPoint: ArrayList<Float>,
        endPoint: ArrayList<Float>
    ): CubicBezierCurve3D {

        val startX = convertXCor2dTo3d(config.displayMetrics, startPoint[0].toDouble())
        val endX = convertYCor2dTo3d(config.displayMetrics, startPoint[1].toDouble())
        val startY = convertXCor2dTo3d(config.displayMetrics, endPoint[0].toDouble())
        val endY = convertYCor2dTo3d(config.displayMetrics, endPoint[1].toDouble())

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
            ItemType.TREE -> {
                z3 = 0.5
                z2 = 0.5
                z1 = 0.5
            }
            else -> {
            }
        }
        return CubicBezierCurve3D(
            Vector3(
                startX,
                endX,
                z1
            ),
            Vector3(
                startX,
                endX,
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

    private fun getObjectAt(x: Float, y: Float) {
        mPicker?.getObjectAt(x, y)
    }

    private fun getBaseItemObject(id: Int): BaseObject3D {
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
            BaseObject3D.ITEM_BIRD_LEFT_ID -> BirdLeft(displayMetrics)
            BaseObject3D.ITEM_BIRD_RIGHT_ID -> BirdRight(displayMetrics)
            BaseObject3D.ITEM_BIRD_INTREE_ID -> BirdInTree(displayMetrics)
            BaseObject3D.ITEM_PLAY_TITLE_ID -> PlayTitle(displayMetrics)
            else -> Tree(displayMetrics)
        }
    }

     private fun getPointSprite(id: Int): Object3D {

        val pointSprite = Plane(0.5f, 1f, 1,1).apply {
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

    private fun convertNameToItemType(name: String) = when (name) {
        ItemType.LEFT_PLANE.name -> ItemType.LEFT_PLANE
        ItemType.RIGHT_PLANE.name -> ItemType.RIGHT_PLANE
        ItemType.SANTA.name -> ItemType.SANTA
        ItemType.REINDEER_IN_TREE.name -> ItemType.REINDEER_IN_TREE
        ItemType.REINDEER.name -> ItemType.REINDEER
        ItemType.SNOWMAN_LEFT.name -> ItemType.SNOWMAN_LEFT
        ItemType.SNOWMAN_RIGHT.name -> ItemType.SNOWMAN_RIGHT
        ItemType.SNOWMAN_INTREE.name -> ItemType.SNOWMAN_INTREE
        ItemType.BIRD_LEFT.name -> ItemType.BIRD_LEFT
        ItemType.BIRD_RIGHT.name -> ItemType.BIRD_RIGHT
        ItemType.BIRD_INTREE.name -> ItemType.BIRD_INTREE
        ItemType.PLAY_TITLE.name -> ItemType.PLAY_TITLE
        else -> ItemType.TREE
    }

    interface EndFlyAnimation {
        fun onEndFly()
    }

    interface OnRenderListener {
        fun onCatch(itemType: ItemType)
        fun onInitSenceSuccess()
        fun onEndGame()
    }
}
