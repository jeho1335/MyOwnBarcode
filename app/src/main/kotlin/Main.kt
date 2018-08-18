package srjhlab.com.myownbarcode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.CommonUi.*
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.SelectDialogItem
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.DbHelper

//When create class in kotlin, not use extends, implements keyword
class Main : Activity(), BarcodeRecyclerviewAdapter.IOnClick, View.OnClickListener {
    val TAG = this.javaClass.simpleName
    val MY_PERMISSIONS_REQUEST = 1

    lateinit var mItem: BarcodeItem // ?= means this variable can be nullable
    lateinit var mItems: MutableList<BarcodeItem> //lateinit is using non nullable variable requires initialize after onCreate()
    var mAdapter: BarcodeRecyclerviewAdapter? = null
    lateinit var mDbHelper: DbHelper
    var mDefaultImage: Bitmap? = null

    //When override a method in kotlin, not use @annotation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDbHelper = DbHelper(applicationContext, "barcodeimg.db", null, 1)
        EventBus.getDefault().register(this)
        initUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "##### onActivityResult ##### value : " + result.contents + " format : " + result.formatName)
        if (result.formatName.equals("CODE_39")
                || result.formatName.equals("CODE_93")
                || result.formatName.equals("CODE_128")
                || result.formatName.equals("EAN_8")
                || result.formatName.equals("EAN_13")
                || result.formatName.equals("PDF_417")
                || result.formatName.equals("UPC_A")
                || result.formatName.equals("UPC_E")
                || result.formatName.equals("CODABAR")
                || result.formatName.equals("ITF")
                || result.formatName.equals("QR_CODE")
                || result.formatName.equals("AZTEC")) {
            AddBarcodeInfoDialog
                    .newInstance()
                    .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                    .setBarcodeItem(BarcodeItem(result.contents, CommonUtils.convertBarcodeType(this, result.formatName)))
                    .show(fragmentManager, this.javaClass.simpleName)

        } else {
            Toast.makeText(this, R.string.string_not_supported_format, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy ####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onClick(v: View?) {
        val viewId: View? = v

        //when is like that Switch() in java
        when (viewId) {
            textview_license -> Log.d(TAG, "##### onItemClick ##### textview_lisence")
            else -> Log.d(TAG, "##### onCLick ##### another")
        }
    }

    override fun onItemClick(item: BarcodeItem) {
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_EMPTY -> {
                Log.d(TAG, "##### onItemClick ##### ITEM_TYPE_EMPTY")
                var items: MutableList<SelectDialogItem> = ArrayList<SelectDialogItem>()
                items.add(SelectDialogItem(SelectDialogItem.INPUT_SELF))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_SCAN))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_IMAGE))
                SelectDialog.newInstance().setItems(items).show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.ITEM_TYPE_BARCODE -> {
                Log.d(TAG, "#### onItemCLick ##### ITEM_TYPE_BARCODE")
                BarcodeFocusDialog.newInstance().setItem(item).setType(BarcodeFocusDialog.VIEW_TYPE_FOCUS).show(fragmentManager, this.javaClass.simpleName)
            }
        }
    }

    override fun onItemLongClick(item: BarcodeItem) {
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_EMPTY -> Log.d(TAG, "##### onItemLongClick ##### ITEM_TYPE_EMPTY")
            ConstVariables.ITEM_TYPE_BARCODE -> {
                Log.d(TAG, "##### onItemLongClick #####")
                BarcodeModifyDialog.newInstance().setItem(item).show(fragmentManager, this.javaClass.simpleName)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        Log.d(TAG, "##### onRequestPermissionRequest #####")
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if (grantResults.size > 0 && grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    gotoScan()
                } else {
                    Toast.makeText(this, R.string.string_require_camera_permission, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun initUi() {
        Log.d(TAG, "##### initUi #####")

        mItems = ArrayList<BarcodeItem>()
        mAdapter = BarcodeRecyclerviewAdapter(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        //textview_license.setText(Html.fromHtml("<u>" + R.string.string_open_source_licensee + "</u>"))
        textview_license.setOnClickListener(this)
        val bitmapDrawable = getDrawable(R.drawable.img_ref) as BitmapDrawable
        mDefaultImage = bitmapDrawable.bitmap
        initListItem()
    }

    fun initListItem() {
        Log.d(TAG, "##### initListItem #####")
        var count = 0
        var dbLength = mDbHelper.length

        with(mItems) {
            clear()
        }

        while (count <= dbLength) {
            if (mDbHelper.barcode[count] != null) {
                mItem = BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mDbHelper.id[count], mDbHelper.name[count], mDbHelper.color[count], mDbHelper.value[count], makeBarcodeFromDB(mDbHelper.barcode[count]))
            } else {
                mItem = BarcodeItem(ConstVariables.ITEM_TYPE_EMPTY, 0, "새 바코드 추가", 0, " ", mDefaultImage)
            }
            mItems.add(mItem)
            ++count
        }
        (recyclerView.adapter as BarcodeRecyclerviewAdapter).setItems(mItems)
    }

    fun makeBarcodeFromDB(b: ByteArray): Bitmap {
        Log.d(TAG, "##### makeBarcodeFromDB")
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    fun setBarcodeScan() {
        Log.d(TAG, "##### setBarcodeScan #####")

        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) run {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST)
                }
            }
        } else {
            gotoScan()
        }
    }

    fun gotoScan() {
        Log.d(TAG, "##### gotoScan #####")
        var integrator = IntentIntegrator(this)
        integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        integrator.addExtra("PROMPT_MESSAGE", resources.getString(R.string.string_scan_guide))
        integrator.setWide()
        integrator.initiateScan()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(busObject: CommonEventbusObejct) {
        when (busObject.type) {
            ConstVariables.EVENTBUS_ADD_NEW_BARCODE -> Log.d(TAG, "##### EVENTBUS_ADD_NEW_BARCODE #####")
            ConstVariables.EVENTBUS_SHOW_BARCODE -> Log.d(TAG, "##### EVENTBUS_SHOW_BARCODE #####")
            ConstVariables.EVENTBUS_MODIFY_BARCODE -> Log.d(TAG, "##### EVENTBUS_MODIFY_BARCODE #####")
            ConstVariables.EVENTBUS_ADD_FROM_KEY -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_KEY #####")
                val dialog = AddFromKeyDialog.newInstance()
                dialog.show(fragmentManager, dialog.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_FROM_CCAN -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_CCAN #####")
                setBarcodeScan()
            }
            ConstVariables.EVENTBUS_ADD_FROM_IMAGE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_IMAGE #####")
                val addFromImageDialog = AddFromImageDialog.newInstance()
                addFromImageDialog.show(fragmentManager, addFromImageDialog.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE #####")
                AddBarcodeInfoDialog.newInstance()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .show(fragmentManager, AddBarcodeInfoDialog::class.java.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS -> {
                initListItem()
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE_SUCCESS #####")
            }
            ConstVariables.EVENTBUS_EDIT_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_EDIT_BARCODE #####")
                AddBarcodeInfoDialog.newInstance()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_EDIT_BARCODE)
                        .show(fragmentManager, AddBarcodeInfoDialog::class.java.simpleName)
            }
            ConstVariables.EVENTBUS_DELETE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_DELETE_BARCODE #####")
                mDbHelper.delete((busObject.`val` as BarcodeItem).barcodeId)
                initListItem()
            }
            ConstVariables.EVENTBUS_SHARE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_SHARE_BARCODE #####")
                BarcodeFocusDialog.newInstance()
                        .setItem(busObject.`val` as BarcodeItem)
                        .setType(BarcodeFocusDialog.VIEW_TYPE_SHARE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
        }
    }
}