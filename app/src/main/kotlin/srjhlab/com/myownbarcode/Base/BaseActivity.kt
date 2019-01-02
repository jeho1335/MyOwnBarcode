package srjhlab.com.myownbarcode.Base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity : AppCompatActivity() {
    val disposables by lazy {
        CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}