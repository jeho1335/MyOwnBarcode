package srjhlab.com.myownbarcode.Base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment() {
    val disposables by lazy {
        CompositeDisposable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }
}