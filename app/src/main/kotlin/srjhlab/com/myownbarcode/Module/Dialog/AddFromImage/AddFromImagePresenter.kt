package srjhlab.com.myownbarcode.Module.Dialog.AddFromImage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.Result
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.ScanImage

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
        Maybe.fromCallable<Bitmap> { ScanImage().getBitmap(intent) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is Bitmap) {
                        getBarcodeInfo(it)
                    } else {
                        mView.onResultErrorHandling()
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    it.printStackTrace()
                    mView.onResultErrorHandling()
                }, {
                    Log.d(TAG, "##### onComplete #####")
                    mView.onResultErrorHandling()
                })
    }

    @SuppressLint("CheckResult")
    fun getBarcodeInfo(bm: Bitmap) {
        Log.d(TAG, "##### getBarcodeInfo #####")
        Maybe.fromCallable<Result> { ScanImage().getBarcodeInfo(bm) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is Result) {
                        Log.d(TAG, "##### subscribe #####")
                        if(it.barcodeFormat == null) {
                            mView.onResultErrorHandling()
                        }else{
                            mView.onResultScanFromImage(it.barcodeFormat.toString(), it.text)
                        }
                    } else {
                        mView.onResultErrorHandling()
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    it.printStackTrace()
                    mView.onResultErrorHandling()
                }, {
                    Log.d(TAG, "##### onComplete #####")
                    mView.onResultErrorHandling()
                })
    }
}