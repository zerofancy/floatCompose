package top.ntutn.floatcompose

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager

// https://juejin.cn/post/6951608145537925128
class ItemViewTouchListener(
    private val wl: WindowManager.LayoutParams, private val windowManager: WindowManager?
) : View.OnTouchListener {
    private var startRawX = 0f
    private var startRawY = 0f

    private var x = 0
    private var y = 0
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x = motionEvent.rawX.toInt()
                y = motionEvent.rawY.toInt()

                startRawX = motionEvent.rawX
                startRawY = motionEvent.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = motionEvent.rawX.toInt()
                val nowY = motionEvent.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY
                wl.apply {
                    x += movedX
                    y += movedY
                }
                //更新悬浮球控件位置
                windowManager?.updateViewLayout(view, wl)
            }
            MotionEvent.ACTION_UP -> {
                val dx = motionEvent.rawX - startRawX
                val dy = motionEvent.rawY - startRawY
                val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop
                if (dx * dx + dy * dy < touchSlop * touchSlop) {
                    view.performClick()
                    return true
                }
            }
            else -> {

            }
        }
        return false
    }
}
