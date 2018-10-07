package Dialog.BarcodeFocus

import android.app.Activity
import android.view.Window

interface BarcodeFocus {
    interface presenter{
        fun requestScreenBrightMax(activity : Activity, window : Window)
    }
}