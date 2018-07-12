package srjhlab.com.myownbarcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017-02-08.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private final Context context;
    List<CardviewItem> items;
    int item_layout;
    GetByteArrayFromDrawable GetByteArrayFromDrawable = new GetByteArrayFromDrawable();

    public RecyclerViewAdapter(Context context, List<CardviewItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CardviewItem item = items.get(position);
        final Drawable drawable = new BitmapDrawable(item.getBody());
        holder.body.setBackground(drawable);
        holder.cardid.setText(item.getId());
        holder.cardview.setTag(position);
        holder.linearLayout.setBackgroundColor(item.getColor());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < MainActivity.cardLength){
                    Activity activity = (Activity)v.getContext();
                    Intent intent = new Intent(activity, BarcodeFocusActivity.class);
                    intent.putExtra("body", GetByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
                    intent.putExtra("value", item.getValue());
                    intent.putExtra("name", item.getId());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade, R.anim.hold);

                }
                else{
                    Activity activity = (Activity)v.getContext();
                    Intent intent  = new Intent(activity, AddSelectActivity.class);
                    intent.putExtra("position" , position);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade, R.anim.hold);
                }
            }
        });
        holder.cardview.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if(position < MainActivity.cardLength){
                    Activity activity = (Activity)v.getContext();
                    Intent intent  = new Intent(activity, BarcodeModifiyActivity.class);
                    intent.putExtra("mid" , item.getMid());
                    intent.putExtra("id", item.getId());
                    intent.putExtra("body", GetByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
                    intent.putExtra("color", item.getColor());
                    intent.putExtra("value", item.getValue());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView body;
        TextView cardid;
        CardView cardview;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            body = (ImageView) itemView.findViewById(R.id.img_body);
            cardid = (TextView) itemView.findViewById(R.id.txt_id);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.cardview_layout);
        }
    }
}
