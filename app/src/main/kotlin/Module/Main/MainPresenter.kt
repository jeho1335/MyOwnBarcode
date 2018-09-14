package Module.Main

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import srjhlab.com.myownbarcode.R

class MainPresenter(activity: Activity) : Main.presenter {
    private val mActivity = activity
    private val TAG = this.javaClass.simpleName

    override fun requestBarcodeScan() {
        Log.d(TAG, "##### requestBarcodeScan #####")
        val PERMISSIONS_REQUEST = 1
        val permissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) run {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                } else {
                    ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                }
            }
        } else {
            val integrator = IntentIntegrator(mActivity)
            integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            integrator.addExtra("PROMPT_MESSAGE", mActivity.resources.getString(R.string.string_scan_guide))
            integrator.setWide()
            integrator.initiateScan()
        }
    }
}