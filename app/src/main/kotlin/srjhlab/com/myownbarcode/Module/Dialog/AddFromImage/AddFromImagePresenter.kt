package srjhlab.com.myownbarcode.Module.Dialog.AddFromImage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.Result
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import srjhlab.com.myownbarcode.Module.Utils.ScanImage
import srjhlab.com.myownbarcode.Utils.ConstVariables

class AddFromImagePresenter(view: AddFromImage.view) : AddFromImage.presenter {
    val mView = view
    val TAG = this.javaClass.simpleName

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

    override fun getImageFromGallery(activity: Activity) {
        Log.d(TAG, "##### getImageFromGallery")
        val intent = Intent(Intent.ACTION_PICK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.type = "image/*"
        intent.putExtra("crop", "true")
        intent.putExtra("outputX", 600)
        intent.putExtra("outputY", 200)
        intent.putExtra("return-data", true)
        activity.startActivityForResult(intent, ConstVariables.RC_FROM_IMAGE)
    }

    @SuppressLint("CheckResult")
    override fun getBitmapFromImage(intent: Intent?) {
        Log.d(TAG, "##### getBitmapFromImage #####")
        Flowable.fromCallable<Bitmap> { ScanImage().getBitmap(intent) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({ result ->
                    if (result is Bitmap) {
                        Log.d(TAG, "##### subscribe #####")
                        getBarcodeInfo(result)
                    } else {
                        mView.onResultErrorHandling()
                    }
                }, {
                    it.printStackTrace()
                    mView.onResultErrorHandling()
                })
    }

    @SuppressLint("CheckResult")
    fun getBarcodeInfo(bm: Bitmap) {
        Log.d(TAG, "##### getBarcodeInfo #####")
        Flowable.fromCallable<Result> { ScanImage().getBarcodeInfo(bm) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({ result ->
                    if (result is Result) {
                        Log.d(TAG, "##### subscribe #####")
                        if(result.barcodeFormat == null) {
                            mView.onResultErrorHandling()
                        }else{
                            mView.onResultScanFromImage(result.barcodeFormat.toString(), result.text)
                        }
                    } else {
                        mView.onResultErrorHandling()
                    }
                }, {
                    it.printStackTrace()
                    mView.onResultErrorHandling()
                })
    }
}