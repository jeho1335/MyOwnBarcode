package srjhlab.com.myownbarcode.Module.License

import android.app.Activity
import android.util.Log
import srjhlab.com.myownbarcode.R
import java.io.ByteArrayOutputStream
import java.io.IOException

class LicensePresenter(view : License.view) : License.presenter {
    val TAG = this.javaClass.simpleName
    val mView = view

    override fun requestLicense(activity: Activity) {
        Log.d(TAG, "##### requestLicense #####")
        var data: String? = null
        val inputStream = activity.resources.openRawResource(R.raw.apache_license)
        val byteArrayOutputStream = ByteArrayOutputStream()

        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            data = String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8)
            inputStream.close()
            mView.onResultLicense(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}