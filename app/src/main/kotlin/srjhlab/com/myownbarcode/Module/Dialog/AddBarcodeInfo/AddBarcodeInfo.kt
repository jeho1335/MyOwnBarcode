package srjhlab.com.myownbarcode.Module.Dialog.AddBarcodeInfo

import android.graphics.Bitmap

interface AddBarcodeInfo {
    interface view {
        fun onResultOverviewBarcode(isSuccess : Boolean, bitmap : Bitmap?)
        fun onResultConvertBitmapToByte(isSucces : Boolean, arr : ByteArray?)
    }

    interface presenter {
        fun requestOverviewBarcode(type: Int, value : String)
        fun requestCovnertBitmapToByte(bitmap : Bitmap)
    }
}