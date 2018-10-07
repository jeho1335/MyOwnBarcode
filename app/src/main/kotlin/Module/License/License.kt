package srjhlab.com.myownbarcode.Module.License

import android.app.Activity

interface License {
    interface view{
        fun onResultLicense(txt : String)
    }
    interface presenter{
        fun requestLicense(activity : Activity)
    }
}