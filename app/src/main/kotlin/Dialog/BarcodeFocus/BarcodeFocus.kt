package Dialog.BarcodeFocus

import android.app.Activity
import android.content.Context
import android.view.Window
import android.view.WindowManager

interface BarcodeFocus {
    interface presenter{
        fun requestScreenBrightMax(activity : Activity, window : Window)
    }
}