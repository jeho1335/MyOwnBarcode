package srjhlab.com.myownbarcode

import Model.ActivityResultEvent
import Module.Main.Main
import Module.Main.MainPresenter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Module.Dialog.AddBarcodeInfo.AddBarcodeInfoDialog
import srjhlab.com.myownbarcode.Module.Dialog.AddFromImage.AddFromImageDialog
import srjhlab.com.myownbarcode.Module.Dialog.AddFromKeyDialog
import srjhlab.com.myownbarcode.Module.Dialog.BarcodeFocusDialog
import srjhlab.com.myownbarcode.Module.License.LicenseFragment
import srjhlab.com.myownbarcode.Module.MyBarcode.MyBarcodeFragment
import srjhlab.com.myownbarcode.Module.Settings.SettingsFragment
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables

class MainActivity : AppCompatActivity(), View.OnClickListener, Main.view {
    val TAG = this.javaClass.simpleName
    private val PERMISSIONS_REQUEST = 1
    private lateinit var mPresenter: MainPresenter
    private lateinit var mMyBarcodeFragment: MyBarcodeFragment
    private lateinit var mSettingsFragment: SettingsFragment
    private lateinit var mContentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mPresenter = MainPresenter(this)
        mMyBarcodeFragment = MyBarcodeFragment()
        mSettingsFragment = SettingsFragment()
        initializeUI()
        handleFragment(ConstVariables.FRAGMENT_STATE_BARCODE_LIST)
    }

    private fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        mContentView = layout_content as View
        img_list_toolbar.setOnClickListener(this)
        img_settings_toolbar.setOnClickListener(this)
        img_back_toolbar.setOnClickListener(this)
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy ####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        mPresenter.onDestroy()
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick #####")
        val viewId = v.id

        when (viewId) {
            img_list_toolbar.id -> {
                handleFragment(ConstVariables.FRAGMENT_STATE_BARCODE_LIST)
            }
            img_settings_toolbar.id -> {
                handleFragment(ConstVariables.FRAGMENT_STATE_SETTINGS)
            }
            img_back_toolbar.id -> {
                handleFragment(ConstVariables.GOTO_BACKSTACK)
            }
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

        if (requestCode == ConstVariables.RC_SIGN_IN) {
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ON_ACTIBITY_RESULT, ActivityResultEvent(requestCode, resultCode, data)))
            return
        }else if(requestCode == ConstVariables.RC_FROM_IMAGE){
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
                        .show(supportFragmentManager, this.javaClass.simpleName)
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

    private fun handleFragment(state: Int) {
        Log.d(TAG, "##### handleFragment ##### state : $state")
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        var fr = Fragment()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        when (state) {
            ConstVariables.GOTO_BACKSTACK -> {
                txt_title_toolbar.text = getString(R.string.string_menu_settings)
                img_list_toolbar.visibility = View.VISIBLE
                img_settings_toolbar.visibility = View.GONE
                img_back_toolbar.visibility = View.GONE
                fm.popBackStack()
                return
            }
            ConstVariables.FRAGMENT_STATE_BARCODE_LIST -> {
                txt_title_toolbar.text = getString(R.string.string_menu_home)
                img_settings_toolbar.visibility = View.VISIBLE
                img_list_toolbar.visibility = View.GONE
                img_back_toolbar.visibility = View.GONE
                fr = MyBarcodeFragment()
            }

            ConstVariables.FRAGMENT_STATE_SETTINGS -> {
                txt_title_toolbar.text = getString(R.string.string_menu_settings)
                img_list_toolbar.visibility = View.VISIBLE
                img_settings_toolbar.visibility = View.GONE
                img_back_toolbar.visibility = View.GONE
                fr = SettingsFragment()
            }

            ConstVariables.FRAGMENT_STATE_LICENSE ->{
                txt_title_toolbar.text = getString(R.string.string_open_source_licensee)
                img_back_toolbar.visibility = View.VISIBLE
                img_settings_toolbar.visibility = View.GONE
                img_list_toolbar.visibility = View.GONE
                fr = LicenseFragment()
            }
        }
        if (fr.isAdded) {
            ft.show(fr)
        } else {
            ft.replace(mContentView.id, fr, fr.javaClass.simpleName)
        }
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onResultBarcodeScan(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultBarcodeScan #####")
        if(result){

        }else{
            toast(getString(msg))
        }
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
                        .show(supportFragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_EDIT_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_EDIT_BARCODE #####")
                AddBarcodeInfoDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_EDIT_BARCODE)
                        .show(supportFragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_SHARE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_SHARE_BARCODE #####")
                BarcodeFocusDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(BarcodeFocusDialog.VIEW_TYPE_SHARE)
                        .show(supportFragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_CLICK_BARCODELIST -> {
                Log.d(TAG, "##### EVENTBUS_CLICK_BARCODELIST #####")
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
                        BarcodeFocusDialog().setBarcodeItem(item).setCommandType(BarcodeFocusDialog.VIEW_TYPE_FOCUS).show(supportFragmentManager, this.javaClass.simpleName)
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
                                            .show(supportFragmentManager, this.javaClass.simpleName)
                                }
                                1 -> {
                                    EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_DELETE_BARCODE, busObject.`val` as BarcodeItem))
                                }
                                2 -> {
                                    BarcodeFocusDialog()
                                            .setBarcodeItem(busObject.`val` as BarcodeItem)
                                            .setCommandType(BarcodeFocusDialog.VIEW_TYPE_SHARE)
                                            .show(supportFragmentManager, this.javaClass.simpleName)
                                }
                            }
                        }
                    }
                }
            }
            ConstVariables.EVENTBUS_GO_TO_LICENSE -> {
                Log.d(TAG, "##### EVENTBUS_GO_TO_LICENSE #####")
                handleFragment(ConstVariables.FRAGMENT_STATE_LICENSE)
            }
            ConstVariables.EVENTBUS_GO_TO_BACKSTACK -> {
                Log.d(TAG, "##### EVENTBUS_GO_TO_BACKSTACK #####")
                handleFragment(ConstVariables.GOTO_BACKSTACK)

            }
        }
    }
}