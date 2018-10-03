package srjhlab.com.myownbarcode.Dialog

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DialogFragment
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_dialog_addimage.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.ScanFromImage
import java.io.ByteArrayOutputStream

class AddFromImageDialog : DialogFragment() {
    private val TAG = this.javaClass.simpleName
    private val REQ_CODE = 200

    private val mScanImage = ScanFromImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullSizeDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.layout_dialog_addimage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startTask()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            try {
                val image = data?.getParcelableExtra<Bitmap>("data")
                val byteArrayOutputStream = ByteArrayOutputStream()
                image?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                if (image == null) {
                    Log.d(TAG, "##### image is null")
                } else {
                    Log.d(TAG, "##### image is not null #####")
                }

                imageview_dialog_addimage.setImageBitmap(image)
                mScanImage.putImage(image)
                Log.d(TAG, "##### format : ${mScanImage.format} value : ${mScanImage.value}")
                if (mScanImage.format == null || mScanImage.value == null) {
//                    Toast.makeText(activity, "바코드를 인식할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    toast("바코드를 인식할 수 없습니다")
                } else {
                    EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_BARCODE, BarcodeItem(mScanImage.value, CommonUtils.convertBarcodeType(activity, mScanImage.format).toLong())))
                }
                dismiss()
            } catch (e: Exception) {
//                Toast.makeText(activity, "이미지 처리 과정에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                toast("이미지 처리 과정에서 오류가 발생했습니다")
                dismiss()
            }

        } else if (resultCode == RESULT_CANCELED) {
            dismiss()
        }
    }

    fun startTask() {
        Log.d(TAG, "##### startTask #####")

        var intent = Intent(Intent.ACTION_PICK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("outputX", 600)
        intent.putExtra("outputY", 200)
        intent.putExtra("return-data", true)
        startActivityForResult(intent, REQ_CODE)
    }
}