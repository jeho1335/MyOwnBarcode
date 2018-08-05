package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.Item.BarcodePagerItem;
import srjhlab.com.myownbarcode.Item.SelectDialogItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.CommonUtils;
import srjhlab.com.myownbarcode.Utils.ConstVariables;
import srjhlab.com.myownbarcode.Utils.ValidityCheck;
import srjhlab.com.myownbarcode.databinding.FragmentAddfromkeyBinding;


/**
 * Created by user on 2018-04-09.
 */

public class AddFromKeyDialog extends DialogFragment implements View.OnClickListener {
    private final String TAG = AddFromKeyDialog.class.getSimpleName();
    private FragmentAddfromkeyBinding mBinding;

    private int mSelectBarcodeType = -1;

    private List<BarcodePagerItem> mItems = null;

    public static AddFromKeyDialog newInstance() {
        return new AddFromKeyDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_addfromkey, container, false);
        EventBus.getDefault().register(this);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.SelectDialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().setCanceledOnTouchOutside(true);
        initializeSampleBarcode();
        initializeUi();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "##### onDestropy #####");
        if (EventBus.getDefault().isRegistered(this)) {
            Log.d(TAG, "##### onDestropy ##### unregister EventByus");
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initializeSampleBarcode() {
        Log.d(TAG, "##### initializeSampleBarcode #####");
        if (mItems != null) {
            mItems.clear();
        } else {
            mItems = new ArrayList<>();
        }
        mItems.add(new BarcodePagerItem(ConstVariables.CODE_39));
        mItems.add(new BarcodePagerItem(ConstVariables.CODE_93));
        mItems.add(new BarcodePagerItem(ConstVariables.CODE_128));
        mItems.add(new BarcodePagerItem(ConstVariables.EAN_8));
        mItems.add(new BarcodePagerItem(ConstVariables.EAN_13));
        mItems.add(new BarcodePagerItem(ConstVariables.PDF_417));
        mItems.add(new BarcodePagerItem(ConstVariables.UPC_A));
        mItems.add(new BarcodePagerItem(ConstVariables.CODABAR));
        mItems.add(new BarcodePagerItem(ConstVariables.ITF));
        mItems.add(new BarcodePagerItem(ConstVariables.QR_CODE));
        mItems.add(new BarcodePagerItem(ConstVariables.MAXI_CODE));
        mItems.add(new BarcodePagerItem(ConstVariables.AZTEC));
    }

    private void initializeUi() {
        mBinding.dialogViewpager.setAdapter(new BarcodePAgerAdapter(getActivity()));
        mBinding.dialogViewpager.addOnPageChangeListener(mPagerChangeListener);
        mBinding.dialogViewpager.setOffscreenPageLimit(1);
        mBinding.dialogViewpager.setClipChildren(true);

        mBinding.imageviewDialogOk.setOnClickListener(this);
        mBinding.imageviewDialogCancel.setOnClickListener(this);
    }

    public void setItems(List<SelectDialogItem> items) {
        Log.d(TAG, "##### setmItems ######");
    }

    private class BarcodePAgerAdapter extends android.support.v4.view.PagerAdapter {
        private LayoutInflater mLayoutInflater = null;

        public BarcodePAgerAdapter(Context context) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.d(TAG, "##### instantiateItem ##### position : " + position);
            View pagerLayout = mLayoutInflater.inflate(R.layout.layout_viewpager_item, container, false);

            final ImageView imageview = pagerLayout.findViewById(R.id.imageview_pager);
            imageview.setBackground(CommonUtils.getSampleBarcode(getActivity(), position));

            pagerLayout.setTag(position);

            imageview.requestLayout();
            imageview.invalidate();
            pagerLayout.requestLayout();
            pagerLayout.invalidate();

            container.addView(pagerLayout, 0);
            return pagerLayout;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Log.d(TAG, "##### destroyItem ##### position : " + position);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            int countSize;
            if (mItems == null) {
                countSize = 1;
            } else {
                countSize = mItems.size();
            }
            Log.d(TAG, "##### getCount ##### getCount :" + countSize);
            return countSize;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            //return super.getItemPosition(object);
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }
    }

    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "##### onPagerSelected ##### position : " + position);
            mSelectBarcodeType = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_dialog_ok:
                Log.d(TAG, "##### onCLick ##### imageview_dialog_ok");
                onClickOk();
                break;
            case R.id.imageview_dialog_cancel:
                Log.d(TAG, "##### onCLick ##### imageview_dialog_cancel");
                onClickCancel();
                break;
        }
    }

    private void onClickOk() {
        Log.d(TAG, "##### on ClickOk #####");
        String value = null;
        if (mBinding.edittextDialog != null) {
            value = mBinding.edittextDialog.getText().toString();
        }

        if (ValidityCheck.getInstance(getActivity()).check(value, mSelectBarcodeType)) {
            EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_BARCODE, new BarcodeItem(value, mSelectBarcodeType)));
        } else {
            //do nothing
        }
    }

    private void onClickCancel() {
        Log.d(TAG, "##### onClickCancel #####");
        dismiss();
    }

    /*
     * Start EventBus
     * */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonEventbusObejct obj) {
        switch (obj.getType()) {
            case ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS:
                Log.d(TAG, "##### onEVent ##### EVENTBUS_ADD_BARCODE_SUCCESS");
                dismiss();
                break;
        }
    }

    /*
     * End EvsutBus
     * */
}