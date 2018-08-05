package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.ScanFromImage;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.CommonUtils;
import srjhlab.com.myownbarcode.Utils.ConstVariables;
import srjhlab.com.myownbarcode.databinding.FragmentAddimageBinding;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Created by user on 2018-04-09.
 */

public class AddFromImageDialog extends DialogFragment {
    private final String TAG = AddFromImageDialog.class.getSimpleName();

    private FragmentAddimageBinding mBinding = null;
    private ScanFromImage mScanImage = null;
    final int REQ_CODE = 200;

    public static AddFromImageDialog newInstance() {
        return new AddFromImageDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_addimage, container, false);
        mScanImage = new ScanFromImage();

        getDialog().getWindow().getAttributes().windowAnimations = R.style.SelectDialogAnimation;
        getDialog().setCanceledOnTouchOutside(true);
        startTask();
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullSizeDialog);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "##### onActivityResult #####");

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            try {
                Bitmap image = data.getParcelableExtra("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                mBinding.imageviewDialogAddimage.setImageBitmap(image);
                mScanImage.putImage(image);
                if (mScanImage.getFormat() == null || mScanImage.getValue() == null) {
                    Toast.makeText(getActivity(), "바코드를 인식할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_BARCODE, new BarcodeItem(mScanImage.getValue(), CommonUtils.convertBarcodeType(getActivity(), mScanImage.getFormat()))));
                }
                dismiss();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "이미지 처리 과정에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        } else if (resultCode == RESULT_CANCELED) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void startTask() {
        Log.d(TAG, "##### startTask #####");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQ_CODE);

    }
}
