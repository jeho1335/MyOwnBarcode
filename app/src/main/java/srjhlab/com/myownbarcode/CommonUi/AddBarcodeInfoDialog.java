package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.apache.commons.lang.StringUtils;
import org.greenrobot.eventbus.EventBus;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.CommonUtils;
import srjhlab.com.myownbarcode.Utils.ConstVariables;
import srjhlab.com.myownbarcode.Utils.MakeBarcode;
import srjhlab.com.myownbarcode.Utils.ToastUtil;
import srjhlab.com.myownbarcode.databinding.FragmentAddinfoBinding;

public class AddBarcodeInfoDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddBarcodeInfoDialog.class.getSimpleName();
    public final static int MODE_ADD_BARCODE = 0;
    public final static int MODE_EDIT_BARCODE = 1;

    private FragmentAddinfoBinding mBinding;
    private BarcodeItem mItem = null;

    private int mPicColor = -1;
    private Drawable mDrawsable = null;
    private Bitmap mBitmap = null;
    private int mCommandType;

    public static AddBarcodeInfoDialog newInstance() {
        return new AddBarcodeInfoDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "##### onCreateVIew #####");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_addinfo, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.SelectDialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().setCanceledOnTouchOutside(true);

        initializeUI();
        return mBinding.getRoot();
    }

    @Override
    public void dismiss() {
        Log.d(TAG, "##### dismiss #####");
        super.dismiss();
    }

    private void initializeUI() {
        Log.d(TAG, "##### initializeUI #####");

        if (mItem != null) {
            mBinding.textviewTypeDialogAddBarcode.setText(CommonUtils.convertBarcodeType(getActivity(), mItem.getBarcodeType()));
            String strArr[] = CommonUtils.splitStringEvery(mItem.getBarcodeValue(), 4);
            String str = StringUtils.join(strArr, " ");
            mBinding.textviewValueDialogAddBarcode.setText(str);
        }

        mBinding.imageviewOkDialogAddBarcode.setOnClickListener(this);
        mBinding.imageviewCancelDialogAddBarcode.setOnClickListener(this);
        mBinding.colorPic1.setOnClickListener(this);
        mBinding.colorPic2.setOnClickListener(this);
        mBinding.colorPic3.setOnClickListener(this);
        mBinding.colorPic4.setOnClickListener(this);
        mBinding.colorPic5.setOnClickListener(this);
        mBinding.colorPic6.setOnClickListener(this);
        mBinding.colorPic7.setOnClickListener(this);
        mBinding.colorPic8.setOnClickListener(this);
        mBinding.colorPic9.setOnClickListener(this);
        mBinding.colorPic10.setOnClickListener(this);
        mBinding.colorPic11.setOnClickListener(this);
        mBinding.colorPic12.setOnClickListener(this);

        if (mItem.getBarcodeValue() != null) {
            setOverviewBarcode(mItem.getBarcodeType(), mItem.getBarcodeValue());
        }

        if (mCommandType == MODE_EDIT_BARCODE) {
            if (mItem != null) {
                mBinding.edittextDialogAddBarcode.setText(mItem.getBarcodeName());
                setPreSelecColor(mItem.getBarcodeCardColor());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_ok_dialog_add_barcode:
                Log.d(TAG, "##### onCLick ##### imageview_ok_dialog_add_barcode");
                saveBarcode();
                break;
            case R.id.imageview_cancel_dialog_add_barcode:
                Log.d(TAG, "##### onCLick ##### imageview_cancel_dialog_add_barcode");
                dismiss();
                break;
            case R.id.color_pic_1:
                Log.d(TAG, "##### onCLick ##### color_pic_1");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_1);
                mBinding.colorPic1.setSelected(true);
                break;
            case R.id.color_pic_2:
                Log.d(TAG, "##### onCLick ##### color_pic_2");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_2);
                mBinding.colorPic2.setSelected(true);
                break;
            case R.id.color_pic_3:
                Log.d(TAG, "##### onCLick ##### color_pic_3");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_3);
                mBinding.colorPic3.setSelected(true);
                break;
            case R.id.color_pic_4:
                Log.d(TAG, "##### onCLick ##### color_pic_4");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_4);
                mBinding.colorPic4.setSelected(true);
                break;
            case R.id.color_pic_5:
                Log.d(TAG, "##### onCLick ##### color_pic_5");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_5);
                mBinding.colorPic5.setSelected(true);
                break;
            case R.id.color_pic_6:
                Log.d(TAG, "##### onCLick ##### color_pic_6");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_6);
                mBinding.colorPic6.setSelected(true);
                break;
            case R.id.color_pic_7:
                Log.d(TAG, "##### onCLick ##### color_pic_7");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_7);
                mBinding.colorPic7.setSelected(true);
                break;
            case R.id.color_pic_8:
                Log.d(TAG, "##### onCLick ##### color_pic_8");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_8);
                mBinding.colorPic8.setSelected(true);
                break;
            case R.id.color_pic_9:
                Log.d(TAG, "##### onCLick ##### color_pic_9");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_9);
                mBinding.colorPic9.setSelected(true);
                break;
            case R.id.color_pic_10:
                Log.d(TAG, "##### onCLick ##### color_pic_10");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_10);
                mBinding.colorPic10.setSelected(true);
                break;
            case R.id.color_pic_11:
                Log.d(TAG, "##### onCLick ##### color_pic_11");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_11);
                mBinding.colorPic11.setSelected(true);
                break;
            case R.id.color_pic_12:
                Log.d(TAG, "##### onCLick ##### color_pic_12");
                clearSelectedColor();
                mPicColor = getResources().getColor(R.color.color_pic_12);
                mBinding.colorPic12.setSelected(true);
                break;
        }
    }

    private void saveBarcode() {
        Log.d(TAG, "##### saveBarcode #####");

        if (mPicColor == -1) {
            ToastUtil.getInstance(getActivity()).showToast(getResources().getString(R.string.string_request_pic_color));
            return;
        }

        if (mBinding.edittextDialogAddBarcode.getText().toString() == null
                && mBinding.edittextDialogAddBarcode.getText().toString() == "") {
            ToastUtil.getInstance(getActivity()).showToast(getResources().getString(R.string.string_request_write_name));
            return;
        }

        //    public BarcodeItem(int type, int barcodeType, int barcodeId, String barcodeName, int barcodeColor, String barcodeValue, Bitmap barcodeBitmap) {
        if (mCommandType == MODE_ADD_BARCODE) {
            //mDbHelper.insert(mBinding.edittextDialogAddBarcode.getText().toString(), mPicColor, mItem.getBarcodeValue(), mDrawsable);
            EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_NEW_BARCODE, new BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.getBarcodeType(), mItem.getBarcodeId(), mBinding.edittextDialogAddBarcode.getText().toString(), mPicColor, mItem.getBarcodeValue(), mBitmap)));
        } else if (mCommandType == MODE_EDIT_BARCODE) {
            //mDbHelper.update(mItem.getBarcodeId(), mBinding.edittextDialogAddBarcode.getText().toString(), mPicColor);
            EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_MODIFY_BARCODE, new BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.getBarcodeType(), mItem.getBarcodeId(), mBinding.edittextDialogAddBarcode.getText().toString(), mPicColor, mItem.getBarcodeValue(), mItem.getBarcodeBitmap())));
        }
        dismiss();
    }

    private void setPreSelecColor(int color) {
        Log.d(TAG, "##### setPreSelectColor ##### color : " + color);
        if(getActivity() == null){
            return;
        }
        if(color == getActivity().getColor(R.color.color_pic_1)){
            mBinding.colorPic1.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_2)){
            mBinding.colorPic2.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_3)){
            mBinding.colorPic3.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_4)){
            mBinding.colorPic4.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_5)){
            mBinding.colorPic5.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_6)){
            mBinding.colorPic6.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_7)){
            mBinding.colorPic7.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_8)){
            mBinding.colorPic8.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_9)){
            mBinding.colorPic9.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_10)){
            mBinding.colorPic10.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_11)){
            mBinding.colorPic11.setSelected(true);
        }else if(color == getActivity().getColor(R.color.color_pic_12)){
            mBinding.colorPic12.setSelected(true);
        }
    }

    private void clearSelectedColor() {
        Log.d(TAG, "##### clearSelectColor #####");

        mBinding.colorPic1.setSelected(false);
        mBinding.colorPic2.setSelected(false);
        mBinding.colorPic3.setSelected(false);
        mBinding.colorPic4.setSelected(false);
        mBinding.colorPic5.setSelected(false);
        mBinding.colorPic6.setSelected(false);
        mBinding.colorPic7.setSelected(false);
        mBinding.colorPic8.setSelected(false);
        mBinding.colorPic9.setSelected(false);
        mBinding.colorPic10.setSelected(false);
        mBinding.colorPic11.setSelected(false);
        mBinding.colorPic12.setSelected(false);
    }

    public AddBarcodeInfoDialog setCommandType(int type) {
        Log.d(TAG, "##### setCommandType #####");
        this.mCommandType = type;
        return this;
    }

    public AddBarcodeInfoDialog setBarcodeItem(BarcodeItem item) {
        Log.d(TAG, "##### setBarcodeItem ##### type : " + item.getBarcodeType() + " value : " + item.getBarcodeValue());
        this.mItem = item;
        return this;
    }

    private void setOverviewBarcode(int type, String value) {
        Log.d(TAG, "##### setOverviewBarcode #####");
        Bitmap imageBitmap = MakeBarcode.getInstance().makeBarcode(type, value);
        mBitmap = imageBitmap;
        mBinding.imageviewBarcodeDialogAddBarcode.setImageBitmap(imageBitmap);
        mDrawsable = new BitmapDrawable(imageBitmap);
    }
}
