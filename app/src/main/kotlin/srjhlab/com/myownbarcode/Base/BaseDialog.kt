package srjhlab.com.myownbarcode.Base

import androidx.fragment.app.DialogFragment
import io.reactivex.disposables.CompositeDisposable

open class BaseDialog : DialogFragment() {

    val disposables by lazy {
        CompositeDisposable()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}