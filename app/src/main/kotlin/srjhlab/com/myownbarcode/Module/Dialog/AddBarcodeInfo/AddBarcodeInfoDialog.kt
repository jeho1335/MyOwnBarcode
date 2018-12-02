package srjhlab.com.myownbarcode.Module.Dialog.AddBarcodeInfo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.layout_dialog_addinfo.*
import org.apache.commons.lang.StringUtils
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables

@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

class AddBarcodeInfoDialog : DialogFragment(), View.OnClickListener, AddBarcodeInfo.view {
    private val TAG = this.javaClass.simpleName

    companion object {
        const val MODE_ADD_BARCODE = 0
        const val MODE_EDIT_BARCODE = 1
    }

    private lateinit var mPresenter: AddBarcodeInfoPresenter

    private lateinit var mItem: BarcodeItem
    private var mCommandType: Int = -1
    private lateinit var mColorArr: Array<Int>
    private lateinit var mColorPickerArr: Array<ImageView>
    private var mPicColor: Long? = -1
    private var mByteArr : ByteArray? = null

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        mColorArr = arrayOf(
                R.color.color_pic_1
                , R.color.color_pic_2
                , R.color.color_pic_3
                , R.color.color_pic_4
                , R.color.color_pic_5
                , R.color.color_pic_6
                , R.color.color_pic_7
                , R.color.color_pic_8
                , R.color.color_pic_9
                , R.color.color_pic_10
                , R.color.color_pic_11
                , R.color.color_pic_12)
        return inflater.inflate(R.layout.layout_dialog_addinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "##### onACtivityCReated #####")
        mPresenter = AddBarcodeInfoPresenter(this)
        initializeUI()
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        Log.d(TAG, "##### show #####")
        val ft = manager?.beginTransaction()
        ft?.add(this, tag)
        ft?.commitAllowingStateLoss()
    }

    fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        textview_dialog_ok.setOnClickListener(this)
        textview_dialog_cancel.setOnClickListener(this)

        mColorPickerArr = arrayOf(
                color_pic_1
                , color_pic_2
                , color_pic_3
                , color_pic_4
                , color_pic_5
                , color_pic_6
                , color_pic_7
                , color_pic_8
                , color_pic_9
                , color_pic_10
                , color_pic_11
                , color_pic_12)

        for ((index, i) in mColorPickerArr.withIndex()) {
            mColorPickerArr[index].setOnClickListener(this)
        }

        val strArr: Array<String> = CommonUtils.splitStringEvery(mItem.barcodeValue, 4)
        val str = StringUtils.join(strArr, " ")
        textview_value_dialog_add_barcode.text = str

        if (mItem.barcodeValue != null && mItem.barcodeType != null) {
            mPresenter.requestOverviewBarcode(mItem.barcodeType.toInt(), mItem.barcodeValue)
            textview_type_dialog_add_barcode.text = CommonUtils.convertBarcodeType(activity, mItem.barcodeType.toInt())
        }

        if (mCommandType == MODE_EDIT_BARCODE) {
            edittext_dialog_add_barcode.setText(mItem.barcodeName)
            setPreSelectedColor(mItem.barcodeCardColor.toInt())
            mPicColor = mItem.barcodeCardColor
        }
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick ##### v.id : ${v.id}")
        when (v.id) {
            textview_dialog_ok.id -> {
                saveBarcode()
                return
            }
            textview_dialog_cancel.id -> {
                dismiss()
                return
            }
        }
        for ((index, i) in mColorArr.withIndex()) {
            if (mColorPickerArr[index].id.equals(v.id)) {
                clearSelectedColor()
                this.mPicColor = activity?.getColor(mColorArr[index])?.toLong()
                mColorPickerArr[index].isSelected = true
                return
            }
        }
    }


    fun setCommandType(type: Int): AddBarcodeInfoDialog {
        Log.d(TAG, "##### setCommandType #####")
        this.mCommandType = type
        return this
    }

    fun setBarcodeItem(item: BarcodeItem): AddBarcodeInfoDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    override fun onResultOverviewBarcode(isSuccess: Boolean, bitmap: Bitmap?) {
        Log.d(TAG, "##### onResultOverviewBarcode #####")
        if(isSuccess) {
            imageview_barcode_dialog_add_barcode.setImageBitmap(bitmap)
            if(bitmap != null) {
                mPresenter.requestCovnertBitmapToByte(bitmap)
            }
        }else{
            // Handle err case
        }
    }

    override fun onResultConvertBitmapToByte(isSucces: Boolean, arr: ByteArray?) {
        Log.d(TAG, "##### onResultConvertBitmapToByte #####")
        if(isSucces){
            mByteArr = arr
        }else{
            // Handle err cace
        }
    }

    private fun saveBarcode() {
        Log.d(TAG, "##### saveBarcode #####")
        if (mPicColor == -1L) {
            activity?.toast(resources.getString(R.string.string_request_pic_color))
            return
        }

        if (edittext_dialog_add_barcode.text.toString() == "") {
            activity?.toast(resources.getString(R.string.string_request_write_name))
            return
        }

        when (mCommandType) {
            MODE_ADD_BARCODE -> EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_NEW_BARCODE, BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.barcodeType, mItem.barcodeId, edittext_dialog_add_barcode.text.toString(), mPicColor, mItem.barcodeValue, mByteArr)))
            MODE_EDIT_BARCODE -> EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_MODIFY_BARCODE, BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.barcodeType, mItem.barcodeId, edittext_dialog_add_barcode.text.toString(), mPicColor, mItem.barcodeValue, mByteArr)))
        }
        dismiss()

    }

    private fun setPreSelectedColor(color: Int) {
        Log.d(TAG, "##### setPreSelectedColor #####")
        when (color) {
            activity?.getColor(R.color.color_pic_1) -> mColorPickerArr.get(0).isSelected = true
            activity?.getColor(R.color.color_pic_2) -> mColorPickerArr.get(1).isSelected = true
            activity?.getColor(R.color.color_pic_3) -> mColorPickerArr.get(2).isSelected = true
            activity?.getColor(R.color.color_pic_4) -> mColorPickerArr.get(3).isSelected = true
            activity?.getColor(R.color.color_pic_5) -> mColorPickerArr.get(4).isSelected = true
            activity?.getColor(R.color.color_pic_6) -> mColorPickerArr.get(5).isSelected = true
            activity?.getColor(R.color.color_pic_7) -> mColorPickerArr.get(6).isSelected = true
            activity?.getColor(R.color.color_pic_8) -> mColorPickerArr.get(7).isSelected = true
            activity?.getColor(R.color.color_pic_9) -> mColorPickerArr.get(8).isSelected = true
            activity?.getColor(R.color.color_pic_10) -> mColorPickerArr.get(9).isSelected = true
            activity?.getColor(R.color.color_pic_11) -> mColorPickerArr.get(10).isSelected = true
            activity?.getColor(R.color.color_pic_12) -> mColorPickerArr.get(11).isSelected = true
        }
    }

    private fun clearSelectedColor() {
        Log.d(TAG, "#### clearSelectedColor #####")
        for ((index, i) in mColorPickerArr.withIndex()) {
            mColorPickerArr[index].isSelected = false
        }
    }
}