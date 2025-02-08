package top.ntutn.floatcompose

import android.app.Application
import top.ntutn.floatcompose.util.AppContextUtil

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextUtil.bind(this)
    }
}