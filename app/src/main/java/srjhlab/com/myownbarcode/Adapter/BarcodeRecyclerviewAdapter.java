package srjhlab.com.myownbarcode.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.GetByteArrayFromDrawable;
import srjhlab.com.myownbarcode.R;

/**
 * Created by Administrator on 2017-02-08.
 */

public class BarcodeRecyclerviewAdapter extends RecyclerView.Adapter<BarcodeRecyclerviewAdapter.ViewHolder> {
    private final static String TAG = BarcodeRecyclerviewAdapter.class.getSimpleName();

    private final Context context;
    private IOnClick mListener;
    private List<BarcodeItem> mItems;
    private GetByteArrayFromDrawable GetByteArrayFromDrawable = new GetByteArrayFromDrawable();

    public BarcodeRecyclerviewAdapter(Context context) {
        this.context = context;
        this.mListener = (IOnClick)context;
        mItems = new ArrayList<>();
    }

    public interface IOnClick{
        void onClick(BarcodeItem item);
        void onLongCLick(BarcodeItem item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "##### onBindViewHolder ##### position : " + position);
        final BarcodeItem item = mItems.get(position);
        final Drawable drawable = new BitmapDrawable(item.getBarcodeBitmap());
        holder.body.setBackground(drawable);
        holder.cardid.setText(item.getBarcodeName());
        holder.cardview.setTag(position);
        holder.linearLayout.setBackgroundColor(item.getBarcodeCardColor());
        /*holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItems.get(position).getItemType() == ConstVariables.ITEM_TYPE_BARCODE) {
                    Activity activity = (Activity) v.getContext();
                    Intent intent = new Intent(activity, BarcodeFocusActivity.class);
                    intent.putExtra("body", GetByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
                    intent.putExtra("value", item.getBarcodeValue());
                    intent.putExtra("name", item.getBarcodeName());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.hold);
                } else if(mItems.get(position).getItemType() == ConstVariables.ITEM_TYPE_EMPTY) {
                    EventBus.getDefault().post(new CommonEventbusObejct(SelectDialogItem.INPUT_SELF));
                }
            }
        });
        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (position < mItems.size()) {
                    Activity activity = (Activity) v.getContext();
                    Intent intent = new Intent(activity, BarcodeModifiyActivity.class);
                    intent.putExtra("mid", item.getBarcodeId());
                    intent.putExtra("id", item.getBarcodeName());
                    intent.putExtra("body", GetByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
                    intent.putExtra("color", item.getBarcodeCardColor());
                    intent.putExtra("value", item.getBarcodeValue());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.hold);
                }
                return false;
            }
        });*/
    }

    public void setItems(List<BarcodeItem> items) {
        Log.d(TAG, "##### setItems #####");
        mItems.clear();
        mItems.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.mItems != null) {
            return this.mItems.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView body;
        private TextView cardid;
        private CardView cardview;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            body = (ImageView) itemView.findViewById(R.id.img_body);
            cardid = (TextView) itemView.findViewById(R.id.txt_id);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            cardview.setOnClickListener(this);
            cardview.setOnLongClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.cardview_layout);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "##### onCLick ##### getAdapterPosition : " + getAdapterPosition());
            mListener.onClick(mItems.get(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "##### onLongClick ##### getAdapterPosition : " + getAdapterPosition());
            mListener.onLongCLick(mItems.get(getAdapterPosition()));
            return false;
        }
    }
}