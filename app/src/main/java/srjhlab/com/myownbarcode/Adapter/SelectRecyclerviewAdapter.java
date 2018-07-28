package srjhlab.com.myownbarcode.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import srjhlab.com.myownbarcode.Item.SelectDialogItem;
import srjhlab.com.myownbarcode.R;

public class SelectRecyclerviewAdapter extends RecyclerView.Adapter<SelectRecyclerviewAdapter.ViewHolder> implements View.OnClickListener {
    private final static String TAG = SelectRecyclerviewAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<SelectDialogItem> mItems = null;
    private IClickListener mListener = null;

    public interface IClickListener{
        void onClcik(int id);
    }

    public SelectRecyclerviewAdapter(Context context, List<SelectDialogItem> items, IClickListener listener) {
        Log.d(TAG, "##### SelectRecyclerviewAdapter #####");
        this.mContext = context;
        this.mItems = items;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mItems.get(position).getItemType() == SelectDialogItem.INPUT_SELF) {
            holder.mCardView.setBackgroundResource(R.drawable.btn_sel_key_event);
            holder.mCardView.setId(SelectDialogItem.INPUT_SELF);
        } else if (mItems.get(position).getItemType() == SelectDialogItem.INPUT_SCAN) {
            holder.mCardView.setBackgroundResource(R.drawable.btn_sel_scan_event);
            holder.mCardView.setId(SelectDialogItem.INPUT_SCAN);
        } else if (mItems.get(position).getItemType() == SelectDialogItem.INPUT_IMAGE) {
            holder.mCardView.setBackgroundResource(R.drawable.btn_sel_image_event);
            holder.mCardView.setId(SelectDialogItem.INPUT_IMAGE);
        }
    }

    @NonNull
    @Override
    public SelectRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_dialog, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CardView) {
            switch (v.getId()) {
                case SelectDialogItem.INPUT_SELF:
                    Log.d(TAG, "##### onClick ##### INPUT_SELF");
                    mListener.onClcik(SelectDialogItem.INPUT_SELF);
                    break;
                case SelectDialogItem.INPUT_SCAN:
                    Log.d(TAG, "##### onClick ##### INPUT_SCAN");
                    mListener.onClcik(SelectDialogItem.INPUT_SCAN);
                    break;
                case SelectDialogItem.INPUT_IMAGE:
                    Log.d(TAG, "##### onClick ##### INPUT_IMAGE");
                    mListener.onClcik(SelectDialogItem.INPUT_IMAGE);
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cardview_select_dialog);
            mCardView.setOnClickListener(SelectRecyclerviewAdapter.this);
        }
    }
}
