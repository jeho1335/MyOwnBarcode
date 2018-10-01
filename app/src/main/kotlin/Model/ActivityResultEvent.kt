package Model

import android.content.Intent

class ActivityResultEvent(requestCode : Int, resultCode : Int, data : Intent?) {
    val requestCode = requestCode
    val resultCode = resultCode
    val data = data
}