package srjhlab.com.myownbarcode.Module.Utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer

class ScanImage(listener : ImageScanResult) {
    val TAG = this.javaClass.simpleName
    val mListener = listener
    interface ImageScanResult{
        fun onImageScanResult(result : Result?)
    }

    fun setImage(bMap: Bitmap) {
        Log.d(TAG, "##### putImage #####")
        ImageScanAsync(bMap).execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class ImageScanAsync(source : Bitmap) : AsyncTask<Bitmap, Bitmap, Result?>(){
        private val mSourceBitmap = source


        override fun doInBackground(vararg p0: Bitmap): Result? {
            val intArray = IntArray(mSourceBitmap.width * mSourceBitmap.height)
            mSourceBitmap.getPixels(intArray, 0, mSourceBitmap.width, 0, 0, mSourceBitmap.width, mSourceBitmap.height)

            val source = RGBLuminanceSource(mSourceBitmap.width, mSourceBitmap.height, intArray)
            val bitmap = BinaryBitmap(HybridBinarizer(source))

            val reader = MultiFormatReader()
            return try {
                val result = reader.decode(bitmap)
                result
            } catch (e: Exception) {
                Log.e("test", "Error decoding barcode", e)
                null
            }
        }

        override fun onPostExecute(result: Result?) {
            super.onPostExecute(result)
            this@ScanImage.mListener.onImageScanResult(result)
        }
    }
}