package Dialog.BarcodeFocus

import android.app.Activity
import android.view.View
import android.view.Window

interface BarcodeFocus {
    interface view {
        fun onResultSharedBarcode(result: Boolean, msg : Int)
    }

    interface presenter {
        fun requestShareBarcode(activity: Activity, view: View)
        fun requestScreenBrightMax(activity: Activity, window: Window)
    }
}