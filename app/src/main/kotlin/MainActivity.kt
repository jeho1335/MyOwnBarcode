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
import android.support.v7.widget.helper.ItemTouchHelper
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
import srjhlab.com.myownbarcode.Adapter.RecyclerViewItemTouchHelper
import srjhlab.com.myownbarcode.CommonUi.*
import srjhlab.com.myownbarcode.Dialog.AddBarcodeInfo
import srjhlab.com.myownbarcode.Dialog.FocusDialog
import srjhlab.com.myownbarcode.Dialog.AddFromImageDialog
import srjhlab.com.myownbarcode.Dialog.SelectDialog
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.SelectDialogItem
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.PreferencesManager

//When create class in kotlin, not use extends, implements keyword
class MainActivity : Activity(), BarcodeRecyclerviewAdapter.IOnClick, View.OnClickListener {
    val TAG = this.javaClass.simpleName
    private val PERMISSIONS_REQUEST = 1

    private lateinit var mItem: BarcodeItem // ?= means this variable can be nullable
    private lateinit var mItems: MutableList<BarcodeItem> //lateinit is using non nullable variable requires initialize after onCreate()
    private lateinit var mAdapter: BarcodeRecyclerviewAdapter
    private var mDefaultImage: Bitmap? = null
    private var mLongClickPosition: Int = 0

    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mRecyclerViewItemTouchHelper: RecyclerViewItemTouchHelper

    //When override a method in kotlin, not use @annotation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        initUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "##### onActivityResult ##### value : " + result.contents + " format : " + result.formatName)
        when (result.formatName) {
            null -> return
            "CODE_39", "CODE_93", "CODE_128", "EAN_8", "EAN_13", "PDF_417", "UPC_A", "UPC_E", "CODABAR", "ITF", "QR_CODE", "AZTEC" ->
                AddBarcodeInfo()
                        .setCommandType(AddBarcodeInfo.MODE_ADD_BARCODE)
                        .setBarcodeItem(BarcodeItem(result.contents, CommonUtils.convertBarcodeType(this, result.formatName)))
                        .show(fragmentManager, this.javaClass.simpleName)
            else -> Toast.makeText(this, R.string.string_not_supported_format, Toast.LENGTH_SHORT).show()
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
        val viewId = v.id

        //when is like that Switch() in java
        when (viewId) {
            textview_license.id -> Log.d(TAG, "##### onItemClick ##### textview_lisence")
            else -> Log.d(TAG, "##### onCLick ##### another")
        }
    }

    override fun onItemClick(item: BarcodeItem) {
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_EMPTY -> {
                Log.d(TAG, "##### onItemClick ##### ITEM_TYPE_EMPTY")
                var items: MutableList<SelectDialogItem> = ArrayList()
                items.add(SelectDialogItem(SelectDialogItem.INPUT_SELF))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_SCAN))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_IMAGE))
                SelectDialog()
                        .setItems(items)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.ITEM_TYPE_BARCODE -> {
                Log.d(TAG, "#### onItemCLick ##### ITEM_TYPE_BARCODE")
                //BarcodeFocusDialog.newInstance().setItem(item).setType(BarcodeFocusDialog.VIEW_TYPE_FOCUS).show(fragmentManager, this.javaClass.simpleName)
                FocusDialog().setItem(item).setType(FocusDialog.VIEW_TYPE_FOCUS).show(fragmentManager, this.javaClass.simpleName)
            }
        }
    }

    override fun onItemLongCLick(item: BarcodeItem, position: Int) {
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_EMPTY -> Log.d(TAG, "##### onItemLongClick ##### ITEM_TYPE_EMPTY")
            ConstVariables.ITEM_TYPE_BARCODE -> {
                Log.d(TAG, "##### onItemLongClick #####")
                var items: MutableList<SelectDialogItem> = ArrayList()
                items.add(SelectDialogItem(SelectDialogItem.INPUT_MODIFTY))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_DELETE))
                items.add(SelectDialogItem(SelectDialogItem.INPUT_SHARE))
                SelectDialog()
                        .setItems(items)
                        .setItem(item)
                        .show(fragmentManager, this.javaClass.simpleName)
                mLongClickPosition = position
            }
        }
    }

    override fun onStartDrag(viewHolder: BarcodeRecyclerviewAdapter.ViewHolder) {
        Log.d(TAG, "##### onStartDrag #####")
        mItemTouchHelper.startDrag(viewHolder)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        Log.d(TAG, "##### onRequestPermissionRequest #####")
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.size > 0 && grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    gotoScan()
                } else {
                    Toast.makeText(this, R.string.string_require_camera_permission, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initUi() {
        Log.d(TAG, "##### initUi #####")

        mItems = ArrayList<BarcodeItem>()
        mAdapter = BarcodeRecyclerviewAdapter(this)

        mRecyclerViewItemTouchHelper = RecyclerViewItemTouchHelper(mAdapter as RecyclerViewItemTouchHelper.IItemTouchHelperAdapter)
        mItemTouchHelper = ItemTouchHelper(mRecyclerViewItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        textview_license.text = Html.fromHtml("<u>" + getString(R.string.string_open_source_licensee) + "</u>")
        textview_license.setOnClickListener(this)
        val bitmapDrawable = getDrawable(R.drawable.img_ref) as BitmapDrawable
        mDefaultImage = bitmapDrawable.bitmap
        initListItem()
    }

    private fun initListItem() {
        Log.d(TAG, "##### initListItem #####")
        mItems = PreferencesManager.loadBarcodeItemList(this)
        (recyclerView.adapter as BarcodeRecyclerviewAdapter).setItems(mItems)
    }

    private fun makeBarcodeFromDB(b: ByteArray): Bitmap {
        Log.d(TAG, "##### makeBarcodeFromDB")
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    private fun setBarcodeScan() {
        Log.d(TAG, "##### setBarcodeScan #####")

        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) run {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                }
            }
        } else {
            gotoScan()
        }
    }

    private fun gotoScan() {
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
            ConstVariables.EVENTBUS_ADD_NEW_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_NEW_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                (recyclerView.adapter as BarcodeRecyclerviewAdapter).addItem(item)
                PreferencesManager.saveBarcodeItemList(this, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_MODIFY_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_MODIFY_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                (recyclerView.adapter as BarcodeRecyclerviewAdapter).updateItem(mLongClickPosition, item)
                PreferencesManager.saveBarcodeItemList(this, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_DELETE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_DELETE_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                (recyclerView.adapter as BarcodeRecyclerviewAdapter).deleteItem(mLongClickPosition, item)
                PreferencesManager.saveBarcodeItemList(this, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_SHOW_BARCODE -> Log.d(TAG, "##### EVENTBUS_SHOW_BARCODE #####")
            ConstVariables.EVENTBUS_ADD_FROM_KEY -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_KEY #####")
                val dialog = AddFromKeyDialog.newInstance()
                dialog.show(fragmentManager, dialog.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_FROM_SCAN -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_SCAN #####")
                setBarcodeScan()
            }
            ConstVariables.EVENTBUS_ADD_FROM_IMAGE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_IMAGE #####")
                AddFromImageDialog().show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE #####")
                AddBarcodeInfo()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfo.MODE_ADD_BARCODE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS -> {
                //initListItem()
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE_SUCCESS #####")
            }
            ConstVariables.EVENTBUS_EDIT_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_EDIT_BARCODE #####")
                AddBarcodeInfo()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfo.MODE_EDIT_BARCODE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_SHARE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_SHARE_BARCODE #####")
                FocusDialog()
                        .setItem(busObject.`val` as BarcodeItem)
                        .setType(FocusDialog.VIEW_TYPE_SHARE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ITEM_MOVE_FINISH -> {
                Log.d(TAG, "#####  EVENTBUS_ITEM_MOVE_FINISH  #####")
                PreferencesManager.saveBarcodeItemList(this, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
        }
    }
}