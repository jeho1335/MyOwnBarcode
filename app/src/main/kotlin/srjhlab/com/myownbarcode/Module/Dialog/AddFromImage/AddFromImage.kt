package srjhlab.com.myownbarcode.Module.Dialog.AddFromImage

import android.app.Activity
import android.content.Intent
import srjhlab.com.myownbarcode.Base.BasePresenter

interface AddFromImage {
    interface view{
        fun onResultScanFromImage(format : String, value : String)
        fun onResultErrorHandling()
    }
    interface presenter : BasePresenter{
        fun getImageFromGallery(activity : Activity)
        fun getBitmapFromImage(intent : Intent?)
    }
}