package srjhlab.com.myownbarcode.Utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream


class BitmapByteConverter {
    val TAG = this.javaClass.simpleName

    fun bitmapToByte(bitmap: Bitmap): ByteArray {
        Log.d(TAG, "##### bitmapToByte #####")
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteToBitmap(byteArray: ByteArray): Bitmap?
    {
        Log.d(TAG, "##### byteToBitmap #####")
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}