package Module.Main

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import srjhlab.com.myownbarcode.R

class MainPresenter(activity: Activity) : Main.presenter {
    private val mActivity = activity
    private val mView = activity as Main.view
    private val TAG = this.javaClass.simpleName
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var mBackPressedTime: Long = 0

    override fun requestBarcodeScan() {
        Log.d(TAG, "##### requestBarcodeScan #####")

        val listener : PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d(TAG, "##### requestShareBarcode onPermissionGranted #####")
                mView.onResultBarcodeScan(true, -1)
                val integrator = IntentIntegrator(mActivity)
                integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                integrator.addExtra("PROMPT_MESSAGE", mActivity.resources.getString(R.string.string_scan_guide))
                integrator.setWide()
                integrator.initiateScan()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Log.d(TAG, "##### requestShareBarcode onPermissionDenied #####")
                mView.onResultBarcodeScan(false, R.string.string_require_camera_permission)
            }
        }

        TedPermission
                .with(mActivity)
                .setPermissionListener(listener)
                .setPermissions(Manifest.permission.CAMERA)
                .check()
    }

    override fun requestBackPressed() {
        Log.d(TAG, "##### requestBackPressed #####")
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - mBackPressedTime
        var result = false
        var msg = -1;

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            result = true

        } else {
            mBackPressedTime = tempTime
            msg = R.string.string_request_backkey
        }
        mView.onResultBackPressed(result, msg)
    }
}