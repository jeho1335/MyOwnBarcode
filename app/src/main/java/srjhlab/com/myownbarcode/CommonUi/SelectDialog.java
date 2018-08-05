package srjhlab.com.myownbarcode.CommonUi;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import srjhlab.com.myownbarcode.Adapter.SelectRecyclerviewAdapter;
import srjhlab.com.myownbarcode.Item.SelectDialogItem;
import srjhlab.com.myownbarcode.R;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.ConstVariables;


/**
 * Created by user on 2018-04-09.
 */

public class SelectDialog extends DialogFragment {
    private final String TAG = SelectDialog.class.getSimpleName();

    private RecyclerView mRecyclerView = null;
    private SelectRecyclerviewAdapter mAdapter = null;
    private List<SelectDialogItem> mItems = null;

    public static SelectDialog newInstance() {
        return new SelectDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_select_dialog, container, false);

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

    private void initializeUi(View v){
        mRecyclerView = v.findViewById(R.id.recyclerview_select_dialog_body);
        mAdapter = new SelectRecyclerviewAdapter(getActivity(), mItems, mListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    public void setItems(List<SelectDialogItem> items){
        Log.d(TAG, "##### setmItems ######");
        this.mItems = items;
    }

    private SelectRecyclerviewAdapter.IClickListener mListener = new SelectRecyclerviewAdapter.IClickListener() {
        @Override
        public void onClcik(int id) {
            Log.d(TAG, "##### pnClick ##### id : " + id);
            switch (id) {
                case SelectDialogItem.INPUT_SELF:
                    Log.d(TAG, "##### onClick ##### INPUT_SELF");
                    EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_KEY));
                    break;
                case SelectDialogItem.INPUT_SCAN:
                    Log.d(TAG, "##### onClick ##### INPUT_SCAN");
                    EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_CCAN));
                    dismiss();
                    break;
                case SelectDialogItem.INPUT_IMAGE:
                    Log.d(TAG, "##### onClick ##### INPUT_IMAGE");
                    EventBus.getDefault().post(new CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_FROM_IMAGE));
                    break;
            }
        }
    };
}
