package srjhlab.com.myownbarcode.Module.Dialog

import Dialog.BarcodeFocus.BarcodeFocus
import Dialog.BarcodeFocus.BarcodeFocusPresenter
import android.app.Activity
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

class BarcodeFocusDialog : androidx.fragment.app.DialogFragment(), BarcodeFocus.view {
    private val TAG = this.javaClass.simpleName
    private var mViewType = VIEW_TYPE_FOCUS
    private lateinit var mPresenter: BarcodeFocusPresenter
    private lateinit var mItem: BarcodeItem

    companion object {
        const val VIEW_TYPE_FOCUS = 1
        const val VIEW_TYPE_SHARE = 2
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.layout_dialog_barcodefocus, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter = BarcodeFocusPresenter(this)
        if (savedInstanceState != null) {
            val bundle = savedInstanceState.getSerializable("KEY")
            if(bundle is BarcodeItem){
                mItem = bundle
            }
        }
        initializeUi()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putSerializable("KEY", mItem)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onResume() {
        Log.d(TAG, "##### onResume #####")
        super.onResume()
        val params = dialog.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as android.view.WindowManager.LayoutParams

        val layoutParams = layout_barcode_focus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        if (mViewType.equals(VIEW_TYPE_SHARE)) {
            mPresenter.requestShareBarcode(activity as Activity, layout_barcode_focus)
            dismiss()

        } else {
            mPresenter.requestScreenBrightMax(activity as Activity, dialog.window)
        }
    }

    fun setBarcodeItem(item: BarcodeItem): BarcodeFocusDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    fun setCommandType(type: Int): BarcodeFocusDialog {
        Log.d(TAG, "##### setCommandType ##### type : $type")
        this.mViewType = type
        return this
    }

    override fun onResultSharedBarcode(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultSharedBarcode #####")
    }

    private fun initializeUi() {
        text_name.text = mItem.barcodeName
        val strArr = CommonUtils.splitStringEvery(mItem.barcodeValue, 4);
        val str = StringUtils.join(strArr, " ")
        text_value.text = str
        text_value.invalidate()
        img_barcode.setImageBitmap((MakeBarcode().makeBarcode(mItem.barcodeType.toInt(), mItem.barcodeValue)))
    }

}