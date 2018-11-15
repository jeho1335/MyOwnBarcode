package Dialog.BarcodeFocus

import android.app.Activity
import android.view.View
import android.view.Window
import srjhlab.com.myownbarcode.Base.BasePresenter

interface BarcodeFocus {
    interface view {
        fun onResultSharedBarcode(result: Boolean, msg : Int)
    }

    interface presenter : BasePresenter {
        fun requestShareBarcode(activity: Activity, view: View)
        fun requestScreenBrightMax(activity: Activity, window: Window)
    }
}