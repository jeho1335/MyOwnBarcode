package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import srjhlab.com.myownbarcode.Item.CommonBarcodeItem;
import srjhlab.com.myownbarcode.MakeBarcode;
import srjhlab.com.myownbarcode.R;

public class AddBarcodeInfoDialog extends DialogFragment implements View.OnClickListener {
    private final static String TAG = AddBarcodeInfoDialog.class.getSimpleName();

    private ImageView mBarcodeImageview = null;
    private TextView mBarcodeTypeTextview, mBarcodeValueTextview = null;
    private EditText mBarcdeTitleEdittext = null;
    private CommonBarcodeItem mItem = null;

    private ImageView mOkImageView, mCancelImageView = null;

    private ImageView col_1, col_2, col_3, col_4, col_5, col_6, col_7, col_8, col_9, col_10, col_11, col_12 = null;

    public static AddBarcodeInfoDialog newInstance() {
        return new AddBarcodeInfoDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_add_barcode_info_dialog, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.SelectDialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().setCanceledOnTouchOutside(true);

        initializeUi(v);
        return v;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initializeUi(View v) {
        Log.d(TAG, "##### inititializeUi #####");

        mBarcodeImageview = v.findViewById(R.id.imageview_barcode_dialog_add_barcode);
        mBarcodeTypeTextview = v.findViewById(R.id.textview_type_dialog_add_barcode);
        mBarcodeValueTextview = v.findViewById(R.id.textview_value_dialog_add_barcode);
        mBarcdeTitleEdittext = v.findViewById(R.id.edittext_dialog_add_barcode);
        mOkImageView = v.findViewById(R.id.imageview_ok_dialog_add_barcode);
        mCancelImageView = v.findViewById(R.id.imageview_cancel_dialog_add_barcode);

        col_1 = v.findViewById(R.id.color_pic_1);
        col_2 = v.findViewById(R.id.color_pic_2);
        col_3 = v.findViewById(R.id.color_pic_3);
        col_4 = v.findViewById(R.id.color_pic_4);
        col_5 = v.findViewById(R.id.color_pic_5);
        col_6 = v.findViewById(R.id.color_pic_6);
        col_7 = v.findViewById(R.id.color_pic_7);
        col_8 = v.findViewById(R.id.color_pic_8);
        col_9 = v.findViewById(R.id.color_pic_9);
        col_10 = v.findViewById(R.id.color_pic_10);
        col_11 = v.findViewById(R.id.color_pic_11);
        col_12 = v.findViewById(R.id.color_pic_12);
        if (mItem.getValue() != null) {
            setOverviewBarcode(mItem.getType(), mItem.getValue());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_ok_dialog_add_barcode:
                break;
            case R.id.imageview_cancel_dialog_add_barcode:
                break;
        }
    }

    public void setBarcodeItem(CommonBarcodeItem item) {
        Log.d(TAG, "##### setBarcodeItem #####");
        this.mItem = item;
    }

    private void setOverviewBarcode(int type, String value) {
        Log.d(TAG, "##### setOverviewBarcode #####");
        if (mBarcodeImageview != null) {
            mBarcodeImageview.setImageBitmap(MakeBarcode.getInstance().makeBarcode(type, value));
        }
    }

}
