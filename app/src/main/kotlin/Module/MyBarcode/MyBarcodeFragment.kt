package srjhlab.com.myownbarcode.Module.MyBarcode

import android.Manifest
import android.app.Fragment
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.layout_fragment_my_barcode.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Adapter.RecyclerViewItemTouchHelper
import srjhlab.com.myownbarcode.Dialog.*
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.SelectDialogItem
import srjhlab.com.myownbarcode.MainActivity
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class MyBarcodeFragment : Fragment() {
    val TAG = this.javaClass.simpleName

    private val PERMISSIONS_REQUEST = 1

    private lateinit var mItem: BarcodeItem
    private lateinit var mItems: MutableList<BarcodeItem>
    private lateinit var mAdapter: BarcodeRecyclerviewAdapter
    private var mDefaultImage: Bitmap? = null
    private var mLongClickPosition: Int = 0

    companion object {
        fun newInstance(items : MutableList<BarcodeItem>) : MyBarcodeFragment{
            val fragment = MyBarcodeFragment()
            return fragment
        }
    }

    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mRecyclerViewItemTouchHelper: RecyclerViewItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onCreate #####")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.layout_fragment_my_barcode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    override fun onResume() {
        Log.d(TAG, "##### onResume #####")
        super.onResume()
    }

    override fun onDestroyOptionsMenu() {
        Log.d(TAG, "##### onDestroyOptionsMenu #####")
        super.onDestroyOptionsMenu()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        Log.d(TAG, "##### onRequestPermissionRequest #####")
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.size > 0 && grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    gotoScan()
                } else {
//                    Toast.makeText(this, R.string.string_require_camera_permission, Toast.LENGTH_SHORT).show()
                    toast(R.string.string_require_camera_permission)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "##### onActivityResult ##### value : " + result.contents + " format : " + result.formatName)
        when (result.formatName) {
            null -> return
            "CODE_39", "CODE_93", "CODE_128", "EAN_8", "EAN_13", "PDF_417", "UPC_A", "UPC_E", "CODABAR", "ITF", "QR_CODE", "AZTEC" ->
                AddBarcodeInfoDialog()
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .setBarcodeItem(BarcodeItem(result.contents, CommonUtils.convertBarcodeType(activity, result.formatName)))
                        .show(fragmentManager, this.javaClass.simpleName)
            else -> toast(R.string.string_not_supported_format)
        }
    }

    private val mIOnCLickListener: BarcodeRecyclerviewAdapter.IOnClick = object : BarcodeRecyclerviewAdapter.IOnClick {
        override fun onItemClick(item: BarcodeItem) {
            Log.d(TAG, "##### onItemClick #####")

            if ((activity as MainActivity).isDrawerOpen()) {
                return
            }

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
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")

        mItems = ArrayList<BarcodeItem>()
        mAdapter = BarcodeRecyclerviewAdapter(mIOnCLickListener)
        mRecyclerViewItemTouchHelper = RecyclerViewItemTouchHelper(mAdapter as RecyclerViewItemTouchHelper.IItemTouchHelperAdapter)
        mItemTouchHelper = ItemTouchHelper(mRecyclerViewItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        //textview_drawer_settings.setOnClickListener(this)

        //textview_drawer_license.setOnClickListener(this)
        mDefaultImage = null

        //img_drawer_icon.setOnClickListener(this)

        initListItem()
    }

    private fun initListItem() {
        Log.d(TAG, "##### initListItem #####")
        mItems = PreferencesManager.loadBarcodeItemList(activity)
        (recyclerView.adapter as BarcodeRecyclerviewAdapter).setItems(mItems)
    }

    private fun makeBarcodeFromDB(b: ByteArray): Bitmap {
        Log.d(TAG, "##### makeBarcodeFromDB")
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    private fun setBarcodeScan() {
        Log.d(TAG, "##### setBarcodeScan #####")

        var permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) run {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST)
                }
            }
        } else {
            gotoScan()
        }
    }

    private fun gotoScan() {
        Log.d(TAG, "##### gotoScan #####")
        var integrator = IntentIntegrator(activity)
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
                PreferencesManager.saveBarcodeItemList(activity, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_MODIFY_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_MODIFY_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                (recyclerView.adapter as BarcodeRecyclerviewAdapter).updateItem(mLongClickPosition, item)
                PreferencesManager.saveBarcodeItemList(activity, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_DELETE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_DELETE_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                (recyclerView.adapter as BarcodeRecyclerviewAdapter).deleteItem(mLongClickPosition, item)
                PreferencesManager.saveBarcodeItemList(activity, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
            ConstVariables.EVENTBUS_SHOW_BARCODE -> Log.d(TAG, "##### EVENTBUS_SHOW_BARCODE #####")
            ConstVariables.EVENTBUS_ADD_FROM_KEY -> {
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_KEY #####")
                /* val dialog = AddFromKeyDialog.newInstance()
                 dialog.show(fragmentManager, dialog.javaClass.simpleName)*/
                AddFromKeyDialog().show(fragmentManager, this.javaClass.simpleName)
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
                AddBarcodeInfoDialog()
                        .setBarcodeItem(busObject.`val` as BarcodeItem)
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS -> {
                //initListItem()
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE_SUCCESS #####")
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
                        .setItem(busObject.`val` as BarcodeItem)
                        .setType(FocusDialog.VIEW_TYPE_SHARE)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_ITEM_MOVE_FINISH -> {
                Log.d(TAG, "#####  EVENTBUS_ITEM_MOVE_FINISH  #####")
                PreferencesManager.saveBarcodeItemList(activity, (recyclerView.adapter as BarcodeRecyclerviewAdapter).getItems())
            }
        }
    }
}