package srjhlab.com.myownbarcode.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_select_dialog.view.*
import srjhlab.com.myownbarcode.Item.SelectDialogItem
import srjhlab.com.myownbarcode.R

class SelectRecyclerViewAdapter(items: MutableList<SelectDialogItem>, listener: SelectRecyclerViewAdapter.IClickListener) : RecyclerView.Adapter<SelectRecyclerViewAdapter.ViewHolder>(), View.OnClickListener {
    private val TAG = this.javaClass.simpleName

    private var mItems: MutableList<SelectDialogItem> = items
    private var mListener: IClickListener = listener

    interface IClickListener {
        fun onClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_select_dialog, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (mItems.get(position).itemType) {
            SelectDialogItem.INPUT_SELF -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_input_key)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_SELF
            }
            SelectDialogItem.INPUT_SCAN -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_input_scan)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_SCAN
            }
            SelectDialogItem.INPUT_IMAGE -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_input_image)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_IMAGE
            }
            SelectDialogItem.INPUT_MODIFTY -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_modify_barcode)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_MODIFTY
            }
            SelectDialogItem.INPUT_DELETE -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_delete_barcode)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_DELETE
            }
            SelectDialogItem.INPUT_SHARE -> {
                holder.itemView.textview_select_dialog_item.setText(R.string.string_share_barcode)
                holder.itemView.textview_select_dialog_item.id = SelectDialogItem.INPUT_SHARE
            }
        }
    }

    override fun getItemCount(): Int {
        return this.mItems.size
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onCLick #####")
        mListener.onClick(v.id)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.textview_select_dialog_item.setOnClickListener(this@SelectRecyclerViewAdapter)
        }

    }
}