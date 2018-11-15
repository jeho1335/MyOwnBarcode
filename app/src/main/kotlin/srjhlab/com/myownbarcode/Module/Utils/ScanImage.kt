package srjhlab.com.myownbarcode.Module.Utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import java.io.ByteArrayOutputStream

class ScanImage() {
    val TAG = this.javaClass.simpleName

    fun getBarcodeInfo(bm: Bitmap): Result {
        Log.d(TAG, "##### getBarcodeInfo #####")
        val intArray = IntArray(bm.width * bm.height)
        bm.getPixels(intArray, 0, bm.width, 0, 0, bm.width, bm.height)

        val source = RGBLuminanceSource(bm.width, bm.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val reader = MultiFormatReader()
        return try {
            return reader.decode(bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding barcode", e)
            Result(null, null, null, null)
        }
    }

     fun getBitmap(intent : Intent?) : Bitmap?{
         Log.d(TAG, "##### getBitmap #####")
         try {
            val image = intent?.getParcelableExtra<Bitmap>("data")
            val byteArrayOutputStream = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            return if (image == null) {
                Log.d(TAG, "##### image is null")
                null
            } else {
                Log.d(TAG, "##### image is not null #####")
                image
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}