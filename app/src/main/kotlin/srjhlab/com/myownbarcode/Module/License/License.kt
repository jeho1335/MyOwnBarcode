package srjhlab.com.myownbarcode.Module.License

import android.app.Activity
import srjhlab.com.myownbarcode.Base.BasePresenter

interface License {
    interface view{
        fun onResultLicense(txt : String)
    }
    interface presenter : BasePresenter{
        fun requestLicense(activity : Activity)
        fun requestHandleKeyEvent(keyCode : Int)
    }
}