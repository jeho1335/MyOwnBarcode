package Module.Main

import Model.FirebaseDatabaseReference
import android.Manifest
import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import srjhlab.com.myownbarcode.Model.PreferencesManager
import srjhlab.com.myownbarcode.Model.RealmClient
import srjhlab.com.myownbarcode.R

class MainPresenter(val activity: Activity) : Main.presenter {
    private val mView = activity as Main.view
    private val TAG = this.javaClass.simpleName
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var mBackPressedTime: Long = 0

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

    override fun requestBarcodeScan() {
        Log.d(TAG, "##### requestBarcodeScan #####")

        val listener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d(TAG, "##### requestShareBarcode onPermissionGranted #####")
                mView.onResultBarcodeScan(true, -1)
                IntentIntegrator(activity).run {
                    this.setPrompt(activity.resources.getString(R.string.string_scan_guide))
                    this.setBarcodeImageEnabled(true)
                    this.initiateScan()
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Log.d(TAG, "##### requestShareBarcode onPermissionDenied #####")
                mView.onResultBarcodeScan(false, R.string.string_require_camera_permission)
            }
        }

        TedPermission
                .with(activity)
                .setPermissionListener(listener)
                .setPermissions(Manifest.permission.CAMERA)
                .check()
    }

    override fun requestBackPressed() {
        Log.d(TAG, "##### requestBackPressed #####")
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - mBackPressedTime
        var result = false
        var msg = -1
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            result = true
        } else {
            mBackPressedTime = tempTime
            msg = R.string.string_request_backkey
        }
        mView.onResultBackPressed(result, msg)
    }

    override fun requestNewNotice() {
        Log.d(TAG, "##### requestNewNotice #####")
        FirebaseDatabaseReference.getNewNotice.orderByValue().limitToLast(1).let { query ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "##### requestNewNotice onCancelled ##### ${databaseError.message}")
                    mView.onResultNewNotice(false)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG, "##### onDataChange onDataChange #####")
                    dataSnapshot.children.first().run {
                        if (PreferencesManager.loadRealLastNotice(activity) == this.key!!.toInt()) {
                            mView.onResultNewNotice(false)
                        } else {
                            mView.onResultNewNotice(true, this.child("body").value.toString().replace("\n", "\n"))
                            PreferencesManager.saveReadLastNotice(activity, this.key!!.toInt())
                        }
                    }
                }
            })
        }
    }
}
