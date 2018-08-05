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

import org.greenrobot.eventbus.EventBus;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.ConstVariables;
import srjhlab.com.myownbarcode.databinding.FragmentBarcodemodifyBinding;


/**
 * Created by user on 2018-04-09.
 */

public class BarcodeModifyDialog extends DialogFragment implements View.OnClickListener {
    private final String TAG = BarcodeModifyDialog.class.getSimpleName();
    private FragmentBarcodemodifyBinding mBinding;
    private BarcodeItem mItem;

    public static BarcodeModifyDialog newInstance() {
        return new BarcodeModifyDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcodemodify, container, false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                Log.d(TAG, "##### onClick ##### btn_edit");
                EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_EDIT_BARCODE, mItem));
                dismiss();
                break;
            case R.id.btn_delete:
                Log.d(TAG, "##### onClick ##### btn_delete");
                EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_DELETE_BARCODE, mItem));
                dismiss();
                break;
        }
    }

    private void initializeUi() {
        Log.d(TAG, "##### initializeUI #####");

        mBinding.btnEdit.setOnClickListener(this);
        mBinding.btnDelete.setOnClickListener(this);
    }

    public void setItem(BarcodeItem item) {
        Log.d(TAG, "##### setItem ######");

        this.mItem = item;
    }
}
