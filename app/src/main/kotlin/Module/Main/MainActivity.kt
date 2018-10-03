package srjhlab.com.myownbarcode

import Model.ActivityResultEvent
import Module.Main.Main
import Module.Main.MainPresenter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Dialog.AddBarcodeInfoDialog
import srjhlab.com.myownbarcode.Dialog.AddFromImageDialog
import srjhlab.com.myownbarcode.Dialog.AddFromKeyDialog
import srjhlab.com.myownbarcode.Dialog.FocusDialog
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Module.MyBarcode.MyBarcodeFragment
import srjhlab.com.myownbarcode.Module.Settings.SettingsFragment
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables

class MainActivity : AppCompatActivity(), View.OnClickListener, Main.view {
    val TAG = this.javaClass.simpleName
    private lateinit var mAuth: FirebaseAuth
    private val PERMISSIONS_REQUEST = 1
    private val FRAGMENT_STATE_BARCODE_LIST = 1
    private val FRAGMENT_STATE_SETTINGS = 2
    private lateinit var mPresenter: MainPresenter
    private lateinit var mMyBarcodeFragment: MyBarcodeFragment
    private lateinit var mSettingsFragment: SettingsFragment
    private lateinit var mContentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        EventBus.getDefault().register(this)
        mPresenter = MainPresenter(this)
        mMyBarcodeFragment = MyBarcodeFragment()
        mSettingsFragment = SettingsFragment()
        initializeUI()
        hanleFragment(FRAGMENT_STATE_BARCODE_LIST)
    }

    private fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        mContentView = layout_content as View
        img_drawer_icon.setOnClickListener(this)
        textview_drawer_home.setOnClickListener(this)
        textview_drawer_settings.setOnClickListener(this)
        textview_drawer_license.setOnClickListener(this)
    }

    override fun onStart() {
        Log.d(TAG, "##### onStart #####")
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "##### onStart currentUserId : ${currentUser.email}#####")
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy ####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick #####")
        val viewId = v.id

        when (viewId) {
            img_drawer_icon.id -> layout_drawer.openDrawer(Gravity.LEFT)
            textview_drawer_home.id -> {
                hanleFragment(FRAGMENT_STATE_BARCODE_LIST)
                layout_drawer.closeDrawer(Gravity.LEFT)
            }
            textview_drawer_settings.id -> {
                hanleFragment(FRAGMENT_STATE_SETTINGS)
                layout_drawer.closeDrawer(Gravity.LEFT)
            }
            textview_drawer_license.id -> Log.d(TAG, "##### onItemCLick #####")
            else -> return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "##### onRequestPermissionRequest #####")
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    mPresenter.requestBarcodeScan()
                } else {
                    toast(R.string.string_require_camera_permission)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ConstVariables.RC_SIGN_IN) {
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ON_ACTIBITY_RESULT, ActivityResultEvent(requestCode, resultCode, data)))
            return
        }

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "##### onActivityResult ##### value : " + result.contents + " format : " + result.formatName)
        when (result.formatName) {
            null -> return
            "CODE_39", "CODE_93", "CODE_128", "EAN_8", "EAN_13", "PDF_417", "UPC_A", "UPC_E", "CODABAR", "ITF", "QR_CODE", "AZTEC" ->
                AddBarcodeInfoDialog()
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .setBarcodeItem(BarcodeItem(result.contents, CommonUtils.convertBarcodeType(this, result.formatName).toLong()))
                        .show(fragmentManager, this.javaClass.simpleName)
            else -> toast(R.string.string_not_supported_format)
        }
    }

    override fun onResultBackPressed(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultBackPressed #####")
        if (result) {
            this.finish()
        } else {
            toast(resources.getString(msg))
        }
    }

    private fun hanleFragment(state: Int) {
        Log.d(TAG, "##### hanleFragment ##### state : $state")
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        var fr = Fragment()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        when (state) {
            FRAGMENT_STATE_BARCODE_LIST -> {
                fr = MyBarcodeFragment()
            }

            FRAGMENT_STATE_SETTINGS -> {
                fr = SettingsFragment()
            }
        }
        fm.popBackStack()
        if (fr.isAdded) {
            ft.show(fr)
        } else {
            ft.add(mContentView.id, fr, fr.javaClass.simpleName)
        }
        ft.addToBackStack(null)


        ft.commit()
    }

    override fun onBackPressed() {
        Log.d(TAG, "##### onBackPressed #####")
        mPresenter.requestBackPressed()
    }

    @Subscribe
    fun onEVent(busObject: CommonEventbusObejct) {
        when (busObject.type) {
            ConstVariables.EVENTBUS_ADD_FROM_SCAN -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_SCAN #####")
                mPresenter.requestBarcodeScan()
            }
            ConstVariables.EVENTBUS_ADD_FROM_KEY -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_KEY #####")
                AddFromKeyDialog()
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_FROM_IMAGE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_IMAGE #####")
                AddFromImageDialog()
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE #####")
                AddBarcodeInfoDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_EDIT_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_EDIT_BARCODE #####")
                AddBarcodeInfoDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_EDIT_BARCODE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_SHARE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_SHARE_BARCODE #####")
                FocusDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(FocusDialog.VIEW_TYPE_SHARE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_CLICK_BARCODELIST -> {
                Log.d(TAG, "##### EVENTBUS_CLICK_BARCODELIST #####")
                if (layout_drawer.isDrawerOpen(Gravity.LEFT)) {
                    return
                }
                val item = busObject.`val` as BarcodeItem
                when (item.itemType) {
                    ConstVariables.ITEM_TYPE_EMPTY -> {
                        val menuList = listOf(getString(R.string.string_input_key), getString(R.string.string_input_scan), getString(R.string.string_input_image))
                        selector("", menuList) { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    AddFromKeyDialog().show(fragmentManager, this.javaClass.simpleName)
                                }
                                1 -> {
                                    mPresenter.requestBarcodeScan()
                                }
                                2 -> {
                                    AddFromImageDialog().show(fragmentManager, this.javaClass.simpleName)
                                }
                            }
                        }
                    }
                    ConstVariables.ITEM_TYPE_BARCODE -> {
                        FocusDialog().setBarcodeItem(item).setCommandType(FocusDialog.VIEW_TYPE_FOCUS).show(fragmentManager, this.javaClass.simpleName)
                    }
                }
            }
            ConstVariables.EVENTBUS_LONGCLICK_BARCODELIST -> {
                Log.d(TAG, "##### EVENTBUS_LONGCLICK_BARCODELIST #####")
                val item = busObject.`val` as BarcodeItem
                when (item.itemType) {
                    ConstVariables.ITEM_TYPE_EMPTY -> Log.d(TAG, "##### onItemLongClick ##### ITEM_TYPE_EMPTY")
                    ConstVariables.ITEM_TYPE_BARCODE -> {
                        Log.d(TAG, "##### onItemLongClick #####")
                        val menuList = listOf(getString(R.string.string_modify_barcode), getString(R.string.string_delete_barcode), getString(R.string.string_share_barcode))
                        selector("", menuList) { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    AddBarcodeInfoDialog()
                                            .setBarcodeItem(busObject.`val` as BarcodeItem)
                                            .setCommandType(AddBarcodeInfoDialog.MODE_EDIT_BARCODE)
                                            .show(fragmentManager, this.javaClass.simpleName)
                                }
                                1 -> {
                                    EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_DELETE_BARCODE, busObject.`val` as BarcodeItem))
                                }
                                2 -> {
                                    FocusDialog()
                                            .setBarcodeItem(busObject.`val` as BarcodeItem)
                                            .setCommandType(FocusDialog.VIEW_TYPE_SHARE)
                                            .show(fragmentManager, this.javaClass.simpleName)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}