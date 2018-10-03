package srjhlab.com.myownbarcode.Dialog

import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_dialog_barcodefocus.*
import org.apache.commons.lang.StringUtils
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.MakeBarcode

class ProgressDialog : DialogFragment() {
    private val TAG = this.javaClass.simpleName
    private var mViewType = VIEW_TYPE_FOCUS
    private lateinit var mItem: BarcodeItem

    companion object {
        const val VIEW_TYPE_FOCUS = 1
        const val VIEW_TYPE_SHARE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.layout_dialog_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initializeUi()
    }

    override fun onResume() {
        super.onResume()
        var params = dialog.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as android.view.WindowManager.LayoutParams
    }

    fun setBarcodeItem(item: BarcodeItem): ProgressDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    fun setCommandType(type: Int): ProgressDialog {
        Log.d(TAG, "##### setCommandType ##### type : $type")
        this.mViewType = type
        return this
    }

    private fun initializeUi() {
        text_name.text = mItem.barcodeName
        var strArr = CommonUtils.splitStringEvery(mItem.barcodeValue, 4);
        var str = StringUtils.join(strArr, " ")
        text_value.text = str
        text_value.invalidate()
        img_barcode.setImageBitmap((MakeBarcode.getInstance().makeBarcode(mItem.barcodeType.toInt(), mItem.barcodeValue)))
    }

}