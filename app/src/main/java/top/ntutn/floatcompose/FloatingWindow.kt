package top.ntutn.floatcompose

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import top.ntutn.floatcompose.util.AppContextUtil
import top.ntutn.floatcompose.util.dp

class FloatingWindow @JvmOverloads constructor(
    context: Context,
    defStyleAttr: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, defStyleAttr, defStyleRes) {
    class FloatingWindowLifecycleOwner : LifecycleOwner, SavedStateRegistryOwner {
        private val registry = LifecycleRegistry(this)
        private val savedStateRegistryController = SavedStateRegistryController.create(this)

        override val lifecycle: Lifecycle = registry
        override val savedStateRegistry: SavedStateRegistry
            get() = savedStateRegistryController.savedStateRegistry

        fun onShow() {
            savedStateRegistryController.performRestore(null)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        fun onDismiss() {
            savedStateRegistry.performSave(Bundle())
            registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
    }

    companion object {
        private const val TAG = "FloatingWindow"
    }

    private var isShowing = false
    private var layoutParams: WindowManager.LayoutParams? = null
    private var mView: View? = null
    private var windowManager: WindowManager? = null

    private var viewTreeLifecycleOwner: FloatingWindowLifecycleOwner? = null

    private fun createView(context: Context): View {
        val lifecycleOwner = FloatingWindowLifecycleOwner()
        viewTreeLifecycleOwner = lifecycleOwner

        val view = ComposeView(context).also {
            it.layoutParams = LayoutParams(300.dp, 200.dp)
            it.setContent {
                Button(onClick = {}) {
                    Text("Hello World!")
                }
            }
        }
        return FrameLayout(context).also {
            it.addView(view)
            // dirty hack, ComposeView need this
            it.id = android.R.id.content
            it.setViewTreeLifecycleOwner(lifecycleOwner)
            it.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        }
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

        view.setOnTouchListener(ItemViewTouchListener(layoutParam, windowManager))
        viewTreeLifecycleOwner?.onShow()
        isShowing = true
    }

    fun dismiss() {
        if (!isShowing) {
            return
        }
        viewTreeLifecycleOwner?.onDismiss()
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