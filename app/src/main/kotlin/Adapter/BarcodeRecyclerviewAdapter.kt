package srjhlab.com.myownbarcode.Adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_cardview.view.*
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R

class BarcodeRecyclerviewAdapter(context : Context) : RecyclerView.Adapter<BarcodeRecyclerviewAdapter.ViewHolder>() {
    private val TAG = this.javaClass.simpleName

    private lateinit var mContext : Context
    private lateinit var mListener : IOnClick
    private lateinit var mItems : MutableList<BarcodeItem>

    interface IOnClick{
        fun onItemClick(item : BarcodeItem)
        fun onItemLongCLick(item : BarcodeItem)
    }

    init {
        this.mContext = context
        this.mListener = context as BarcodeRecyclerviewAdapter.IOnClick
        this.mItems = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "##### onCreateViewHolder #####")

        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "##### onBindViewHolder #####")

        val item : BarcodeItem = mItems.get(position)
        val drawable : Drawable = BitmapDrawable(item.barcodeBitmap)
        holder.itemView.img_body.background = drawable
        holder.itemView.txt_id.text = item.barcodeName
        holder.itemView.cardview.tag = position
        holder.itemView.cardview_layout.setBackgroundColor(item.barcodeCardColor)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "##### getItemCount ##### size : " + this.mItems.size)

        return this.mItems.size
    }

    fun setItems(items : MutableList<BarcodeItem>){
        Log.d(TAG, "##### setItems #####")
        this.mItems.clear()
        this.mItems.addAll(items)

        notifyDataSetChanged()
    }

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val TAG = this.javaClass.simpleName
        init {
            itemView.cardview.setOnClickListener(this)
            itemView.cardview.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d(TAG, "##### onClick #####")
            when(v.id){
                R.id.imageview_move -> Log.d(TAG, "##### onCLick move #####")
                else -> mListener.onItemClick(mItems.get(adapterPosition))
            }
        }

        override fun onLongClick(v: View?): Boolean {
            Log.d(TAG, "##### onLongClick #####")
            mListener.onItemLongCLick(mItems.get(adapterPosition))
            return false
        }
    }
}