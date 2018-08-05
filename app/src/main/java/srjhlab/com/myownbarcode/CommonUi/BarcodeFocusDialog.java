package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.databinding.FragmentBarcodefocusBinding;


/**
 * Created by user on 2018-04-09.
 */

public class BarcodeFocusDialog extends DialogFragment {
    private final String TAG = BarcodeFocusDialog.class.getSimpleName();
    private FragmentBarcodefocusBinding mBinding;
    private BarcodeItem mItem;

    public static BarcodeFocusDialog newInstance() {
        return new BarcodeFocusDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcodefocus, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.SelectDialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().setCanceledOnTouchOutside(true);
        initializeUi();

        return mBinding.getRoot();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initializeUi() {
        mBinding.textName.setText(mItem.getBarcodeName());
        mBinding.textValue.setText(mItem.getBarcodeValue());
        mBinding.imgBarcode.setImageBitmap(mItem.getBarcodeBitmap());
    }

    public void setItem(BarcodeItem item) {
        Log.d(TAG, "##### setItem ######");
        this.mItem = item;
    }
}
