package top.ntutn.floatcompose

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import top.ntutn.floatcompose.util.AppContextUtil
import top.ntutn.floatcompose.util.dp

class FloatingWindow @JvmOverloads constructor(
    context: Context,
    defStyleAttr: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, defStyleAttr, defStyleRes) {
    companion object {
        private const val TAG = "FloatingWindow"
    }

    private var isShowing = false
    private var layoutParams: WindowManager.LayoutParams? = null
    private var mView: View? = null
    private var windowManager: WindowManager? = null

    private fun createView(context: Context): View {
        val view = View(context).also {
            it.layoutParams = LayoutParams(100.dp, 100.dp)
            it.setBackgroundColor(Color.RED)
        }
        return FrameLayout(context).also { it.addView(view) }
    }

    fun show() {
        if (isShowing) {
            return
        }
        val context = AppContextUtil.getTopValidActivity() ?: return
        val view = createView(context)

        val layoutParam = WindowManager.LayoutParams()
        layoutParam.apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            format = PixelFormat.RGBA_8888
            gravity = Gravity.START or Gravity.TOP
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            x = layoutParams?.x?.takeIf { it >= 0 } ?: 0
            y = layoutParams?.y?.takeIf { it >= 0 } ?: 0
        }
        layoutParams = layoutParam
        mView = view

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager?.addView(view, layoutParams)

        isShowing = true
    }

    fun dismiss() {
        if (!isShowing) {
            return
        }
        try {
            windowManager?.removeView(mView)
            mView = null
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        } finally {
            isShowing = false
        }
    }
}