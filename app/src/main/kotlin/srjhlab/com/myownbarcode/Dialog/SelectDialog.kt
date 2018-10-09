package srjhlab.com.myownbarcode.Dialog

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_dialog.*
import org.greenrobot.eventbus.EventBus
import srjhlab.com.myownbarcode.Adapter.SelectRecyclerViewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.SelectDialogItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables

class SelectDialog : DialogFragment(), SelectRecyclerViewAdapter.IClickListener {
    val TAG = this.javaClass.simpleName

    private lateinit var mAdapter: SelectRecyclerViewAdapter
    private lateinit var mItems: MutableList<SelectDialogItem>
    private lateinit var mItem: BarcodeItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        return inflater.inflate(R.layout.layout_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        Log.d(TAG, "#####  initializeUi #####")
        mAdapter = SelectRecyclerViewAdapter(mItems, this)
        recyclerview_select_dialog_body.adapter = mAdapter
        recyclerview_select_dialog_body.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerview_select_dialog_body.setHasFixedSize(true)
    }

    fun setItems(items: MutableList<SelectDialogItem>): SelectDialog {
        Log.d(TAG, "##### setItems #####")
        this.mItems = items
        return this
    }

    fun setItem(item: BarcodeItem): SelectDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    override fun onClick(id: Int) {
        when (id) {
            SelectDialogItem.INPUT_SELF -> {
                Log.d(TAG, "##### onItemClick ##### INPUT_SELF")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_KEY))
            }
            SelectDialogItem.INPUT_SCAN -> {
                Log.d(TAG, "##### onItemClick ##### INPUT_SELF")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_SCAN))
            }
            SelectDialogItem.INPUT_IMAGE -> {
                Log.d(TAG, "##### oItemClick ##### INPUT_IMAGE")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_IMAGE))
            }
            SelectDialogItem.INPUT_MODIFTY -> {
                Log.d(TAG, "##### oItemClick ##### INPUT_MODIFTY")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_EDIT_BARCODE, mItem))
            }
            SelectDialogItem.INPUT_DELETE -> {
                Log.d(TAG, "##### oItemClick ##### INPUT_DELETE")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_DELETE_BARCODE, mItem))
            }
            SelectDialogItem.INPUT_SHARE -> {
                Log.d(TAG, "##### oItemClick ##### INPUT_SHARE")
                EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_SHARE_BARCODE, mItem))
            }
        }
        dismiss()
    }
}