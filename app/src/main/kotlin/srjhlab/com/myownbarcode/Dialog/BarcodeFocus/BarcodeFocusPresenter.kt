package Dialog.BarcodeFocus

import android.Manifest
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.Window
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.layout_dialog_barcodefocus.*
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.R.id.layout_barcode_focus
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.PreferencesManager


class BarcodeFocusPresenter(view : BarcodeFocus.view) : BarcodeFocus.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view

    override fun requestShareBarcode(activity: Activity, view : View) {
        Log.d(TAG, "##### requestShareBarcode #####")
        val listener : PermissionListener = object : PermissionListener{
            override fun onPermissionGranted() {
                Log.d(TAG, "##### requestShareBarcode onPermissionGranted #####")
                CommonUtils.shareBitmapToApps(activity, CommonUtils.viewToBitmap(activity, view))
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Log.d(TAG, "##### requestShareBarcode onPermissionDenied #####")
                activity.toast(activity.resources.getString(R.string.string_require_camera_permission))
            }
        }
        TedPermission
                .with(activity)
                .setPermissionListener(listener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }

    override fun requestScreenBrightMax(activity: Activity, window: Window) {
        Log.d(TAG, "##### requestScreenBrightMax #####")
        val currentState = PreferencesManager.loadAutoBrightState(activity)
        if (currentState) {
            val layout = window.attributes
            layout.screenBrightness = 1f
            window.attributes = layout
        }
    }
}