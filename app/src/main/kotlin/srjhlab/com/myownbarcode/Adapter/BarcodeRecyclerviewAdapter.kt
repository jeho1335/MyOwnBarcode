package srjhlab.com.myownbarcode.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_barcode.view.*
import org.greenrobot.eventbus.EventBus
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.MakeBarcode
import java.util.*

class BarcodeRecyclerviewAdapter(listener: IOnItemClick) : RecyclerView.Adapter<BarcodeRecyclerviewAdapter.ViewHolder>(), RecyclerViewItemTouchHelper.IItemTouchHelperAdapter {
    private val TAG = this.javaClass.simpleName

    private var mListener: IOnItemClick = listener
    private var mItems: MutableList<BarcodeItem> = ArrayList()

    interface IOnItemClick {
        fun onItemClick(item: BarcodeItem)
        fun onItemLongCLick(item: BarcodeItem, position: Int)
        fun onStartDrag(viewHolder: BarcodeRecyclerviewAdapter.ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "##### onCreateViewHolder #####")

        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_barcode, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "##### onBindViewHolder #####")

        val item: BarcodeItem = mItems.get(position)
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_BARCODE -> {
                holder.itemView.img_body.visibility = View.VISIBLE
                holder.itemView.cardview_layout.visibility = View.VISIBLE
                holder.itemView.textview_empty_body.visibility = View.GONE
                val bmp = MakeBarcode.getInstance().makeBarcode(item.barcodeType.toInt(), item.barcodeValue)
                if(bmp != null) {
                    holder.itemView.img_body.setImageBitmap(bmp)
                    holder.itemView.txt_load_failed.visibility = View.GONE
                }else{
                    holder.itemView.txt_load_failed.visibility = View.VISIBLE
                }
                holder.itemView.txt_id.text = item.barcodeName
                holder.itemView.cardview.tag = position
                holder.itemView.cardview_layout.setBackgroundColor(item.barcodeCardColor.toInt())
                holder.itemView.imageview_move.setOnTouchListener { v, event ->
                    Log.d(TAG, "##### onTouch ##### action : ${event.action}")
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> mListener.onStartDrag(holder)
                    }
                    false
                }
            }
            ConstVariables.ITEM_TYPE_EMPTY -> {
                holder.itemView.img_body.visibility = View.GONE
                holder.itemView.cardview_layout.visibility = View.GONE
                holder.itemView.textview_empty_body.visibility = View.VISIBLE
                holder.itemView.textview_empty_body.setText(R.string.string_request_add_new_barcode)
                holder.itemView.cardview.tag = position
                holder.itemView.cardview_layout.setBackgroundColor(item.barcodeCardColor.toInt())
                holder.itemView.imageview_move.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "##### getItemCount #####")
        return this.mItems.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Log.d(TAG, "##### onItemMove ##### from $fromPosition  to $toPosition")
        if (ConstVariables.ITEM_TYPE_EMPTY.equals(mItems.get(toPosition).itemType)) {
            Log.d(TAG, "##### onItemMove ##### This is the end of List. return!!!")
            return
        }
        Collections.swap(mItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ITEM_MOVE_FINISH))
    }

    fun setItems(items: MutableList<BarcodeItem>) {
        Log.d(TAG, "##### setItems #####")
        this.mItems.clear()
        this.mItems.addAll(items)
//        notifyDataSetChanged()
    }

    fun addItem(item: BarcodeItem) {
        Log.d(TAG, "##### addItem #####")
        var size = mItems.size
        this.mItems.add(size - 1, item)
        notifyItemInserted(size - 1)
    }

    fun updateItem(position: Int, item: BarcodeItem) {
        Log.d(TAG, "##### updateItem ##### position : $position")
        this.mItems.set(position, item)
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int, item: BarcodeItem) {
        Log.d(TAG, "##### deleteItem ##### position : $position")
        this.mItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItems(): MutableList<BarcodeItem> {
        Log.d(TAG, "##### getItems #####")
        return this.mItems
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val TAG = this.javaClass.simpleName

        init {
            itemView.img_body.setOnClickListener(this)
            itemView.textview_empty_body.setOnClickListener(this)
            itemView.img_body.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d(TAG, "##### onClick #####")
            mListener.onItemClick(mItems.get(adapterPosition))
        }

        override fun onLongClick(v: View): Boolean {
            Log.d(TAG, "##### onLongClick #####")
            mListener.onItemLongCLick(mItems.get(adapterPosition), adapterPosition)
            return false
        }
    }
}