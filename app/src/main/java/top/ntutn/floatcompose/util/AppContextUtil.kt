package top.ntutn.floatcompose.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

object AppContextUtil: Application.ActivityLifecycleCallbacks {
    lateinit var applicationContext: Context

    private val activityStack = ArrayDeque<Activity>()

    fun bind(context: Application) {
        applicationContext = context
        context.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityStack.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityStack.remove(activity)
    }

    fun getTopActivity(): Activity? {
        return activityStack.lastOrNull()
    }

    fun getTopValidActivity(): Activity? {
        return activityStack.lastOrNull { !it.isFinishing }
    }
}