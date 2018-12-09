package srjhlab.com.myownbarcode.Module.License

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import org.greenrobot.eventbus.EventBus
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables
import java.io.ByteArrayOutputStream
import java.io.IOException

class LicensePresenter(private val mView : License.view) : License.presenter {
    val TAG = this.javaClass.simpleName!!

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

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

    override fun requestHandleKeyEvent(keyCode: Int) {
        Log.d(TAG, "##### requestHandleKeyEvent #####")
        if(keyCode == KeyEvent.KEYCODE_BACK){
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_GO_TO_BACKSTACK))
        }
    }
}