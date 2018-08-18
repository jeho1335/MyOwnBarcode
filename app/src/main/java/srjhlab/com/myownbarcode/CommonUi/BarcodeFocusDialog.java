package srjhlab.com.myownbarcode.CommonUi;

import android.Manifest;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.apache.commons.lang.StringUtils;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.Utils.CommonUtils;
import srjhlab.com.myownbarcode.databinding.FragmentBarcodefocusBinding;


/**
 * Created by user on 2018-04-09.
 */

public class BarcodeFocusDialog extends DialogFragment {
    private final String TAG = BarcodeFocusDialog.class.getSimpleName();
    public final static int VIEW_TYPE_FOCUS = 1;
    public final static int VIEW_TYPE_SHARE = 2;
    private FragmentBarcodefocusBinding mBinding;
    private int mViewType = VIEW_TYPE_FOCUS;
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
        initializeUI();

        return mBinding.getRoot();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initializeUI() {
        Log.d(TAG, "##### initializeUI #####");
        if(mItem != null) {
            mBinding.textName.setText(mItem.getBarcodeName());
            String strArr[] = CommonUtils.splitStringEvery(mItem.getBarcodeValue(), 4);
            String str = StringUtils.join(strArr, " ");
            mBinding.textValue.setText(str);
            mBinding.textValue.invalidate();
            mBinding.imgBarcode.setImageBitmap(mItem.getBarcodeBitmap());
            if(mViewType == VIEW_TYPE_SHARE){
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    CommonUtils.shareBitmapToApps(getActivity(), CommonUtils.viewToBitmap(getActivity(), mBinding.layoutBarcodeFocus));
                    dismiss();
                }
            }
        }
    }

    public BarcodeFocusDialog setItem(BarcodeItem item) {
        Log.d(TAG, "##### setItem ######");
        this.mItem = item;
        return this;
    }

    public BarcodeFocusDialog setType(int type){
        Log.d(TAG, "##### setType #####");
        mViewType = type;
        return this;
    }
}
