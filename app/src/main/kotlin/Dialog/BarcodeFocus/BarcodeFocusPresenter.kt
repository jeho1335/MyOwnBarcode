package Dialog.BarcodeFocus

import android.app.Activity
import android.util.Log
import android.view.Window
import srjhlab.com.myownbarcode.Utils.PreferencesManager


class BarcodeFocusPresenter : BarcodeFocus.presenter {
    private val TAG = this.javaClass.simpleName

    override fun requestScreenBrightMax(activity: Activity, window: Window) {
        Log.d(TAG, "##### requestScreenBrightMax #####")
        val currentState = PreferencesManager.loadAutoBrightState(activity)
        if (currentState) {
            val layout = window.attributes
            layout.screenBrightness = 1f
            window.attributes = layout
        }
    }
}