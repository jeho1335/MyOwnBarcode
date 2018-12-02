package srjhlab.com.myownbarcode.Module.Dialog.AddBarcodeInfo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import srjhlab.com.myownbarcode.Utils.BitmapByteConverter
import srjhlab.com.myownbarcode.Utils.MakeBarcode

class AddBarcodeInfoPresenter(val mView: AddBarcodeInfo.view) : AddBarcodeInfo.presenter {
    val TAG = this.javaClass.simpleName

    @SuppressLint("CheckResult")
    override fun requestOverviewBarcode(type: Int, value: String) {
        Log.d(TAG, "##### requestOverviewBarcode #####")
        Maybe.fromCallable<Bitmap> {
            MakeBarcode().makeBarcode(type, value)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is Bitmap) {
                        mView.onResultOverviewBarcode(true, it)
                    } else {
                        mView.onResultOverviewBarcode(false, null)
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    mView.onResultOverviewBarcode(false, null)
                }, {
                    Log.d(TAG, "##### onCompletion #####")
                    mView.onResultOverviewBarcode(false, null)
                })
    }

    @SuppressLint("CheckResult")
    override fun requestCovnertBitmapToByte(bitmap: Bitmap) {
        Log.d(TAG, "##### requestCovnertBitmapToByte #####")
        Maybe.fromCallable<ByteArray> {
            BitmapByteConverter().bitmapToByte(bitmap)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is ByteArray) {
                        mView.onResultConvertBitmapToByte(true, it)
                    } else {
                        mView.onResultConvertBitmapToByte(false, null)
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    mView.onResultConvertBitmapToByte(false, null)
                }, {
                    Log.d(TAG, "##### onCompletion #####")
                    mView.onResultConvertBitmapToByte(false, null)
                })
    }
}
