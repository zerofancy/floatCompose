package top.ntutn.floatcompose.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * Created by shenjun on 2020/10/12.
 */

/**
 * convert dp to px
 */
fun Context.px2dp(pxValue: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (pxValue / scale).roundToInt()
}

/**
 * convert px to dp
 */
val Number.px2dp get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).roundToInt()


/**
 * convert dp to px
 */
fun dp2px(dpValue: Float): Float {
    return dpValue * Resources.getSystem().displayMetrics.density
}

/**
 * convert dp to px
 */
fun dp2px(dpValue: Int): Int {
    return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
}

/**
 * convert sp to px
 */
fun sp2px(spValue: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spValue.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()
}

inline val <reified T : Number> T.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

inline val <reified T : Number> T.dpFloat
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val <reified T : Number> T.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

inline val <reified T : Number> T.spFloat
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

inline val <reified T : Number> T.px
    get() = this.toInt()

inline val <reified T : Number> T.fontScale
    get() = this.toFloat() * Resources.getSystem().configuration.fontScale


fun Context.getScreenWidth(): Int {
    val dm = resources.displayMetrics
    return dm?.widthPixels ?: 0
}

fun Context.getScreenHeight(): Int {
    val dm = resources.displayMetrics
    return dm?.heightPixels ?: 0
}

fun Context.getScreenHeightDp(): Int {
    return (resources.displayMetrics.heightPixels / resources.displayMetrics.density).roundToInt()
}

val String.color: Int
    get() = Color.parseColor(this)

internal fun Boolean.toInt() = if (this) 1 else 0