package srjhlab.com.myownbarcode.Adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_barcode.view.*
import org.greenrobot.eventbus.EventBus
import srjhlab.com.myownbarcode.Animations.SimpleViewFadeAnimation
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.BitmapByteConverter
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.MakeBarcode
import java.util.*

class BarcodeRecyclerviewAdapter(listener: IOnItemClick) : RecyclerView.Adapter<BarcodeRecyclerviewAdapter.ViewHolder>(), RecyclerViewItemTouchHelper.IItemTouchHelperAdapter {
    private val TAG = this.javaClass.simpleName

    private var mListener: IOnItemClick = listener
    private var mItems: MutableList<BarcodeItem> = ArrayList()
    private lateinit var mSimpleAnimation: SimpleViewFadeAnimation
    private var mIsRequestItemMove = false

    interface IOnItemClick {
        fun onItemClick(item: BarcodeItem)
        fun onItemLongCLick(item: BarcodeItem, position: Int)
        fun onStartDrag(viewHolder: BarcodeRecyclerviewAdapter.ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "##### onCreateViewHolder #####")

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_barcode, parent, false)
        mSimpleAnimation = SimpleViewFadeAnimation()
        return ViewHolder(view)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "##### onBindViewHolder #####")

        val item: BarcodeItem = mItems[position]
        when (item.itemType) {
            ConstVariables.ITEM_TYPE_BARCODE -> {
                holder.itemView.img_body.visibility = View.GONE
                holder.itemView.cardview_layout.visibility = View.VISIBLE
                holder.itemView.textview_empty_body.visibility = View.GONE
                holder.itemView.txt_id.text = item.barcodeName
                holder.itemView.cardview.tag = position
                holder.itemView.cardview_layout.setBackgroundColor(item.barcodeCardColor.toInt())
                holder.itemView.imageview_move.setOnTouchListener { v, event ->
                    Log.d(TAG, "##### onTouch ##### action : ${event.action}")
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> mListener.onStartDrag(holder)
                    }
                    true
                }

                if (item.barcodeBitmapArr != null) {
                    Log.d(TAG, "test it will search from bytearr")
                    Maybe.fromCallable<Bitmap> {
                        BitmapByteConverter().byteToBitmap(item.barcodeBitmapArr)
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.d(TAG, "##### ${item.barcodeName} onSuccess #####")
                                if (it is Bitmap) {
                                    holder.itemView.img_body.setImageBitmap(it)
                                    holder.itemView.img_body.visibility = View.VISIBLE
//                                    mSimpleAnimation.startAnimation(mSimpleAnimation.FADE_IN, holder.itemView.img_body)
                                    holder.itemView.txt_load_failed.visibility = View.GONE
                                } else {
                                    holder.itemView.txt_load_failed.visibility = View.VISIBLE
                                }
                            }, {
                                Log.d(TAG, "##### ${item.barcodeName} onError #####")
                                holder.itemView.txt_load_failed.visibility = View.VISIBLE

                            }, {
                                Log.d(TAG, "##### ${item.barcodeName} onCompletion #####")
                                holder.itemView.txt_load_failed.visibility = View.VISIBLE
                            })
                } else {
                    Log.d(TAG, "test it will search from metadata")
                    Maybe.fromCallable<Bitmap> {
                        MakeBarcode().makeBarcode(item.barcodeType.toInt(), item.barcodeValue)
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.d(TAG, "##### ${item.barcodeName} onSuccess #####")
                                if (it is Bitmap) {
                                    holder.itemView.img_body.setImageBitmap(it)
                                    mSimpleAnimation.startAnimation(mSimpleAnimation.FADE_IN, holder.itemView.img_body)
                                    holder.itemView.txt_load_failed.visibility = View.GONE
                                } else {
                                    holder.itemView.txt_load_failed.visibility = View.VISIBLE
                                }
                            }, {
                                Log.d(TAG, "##### ${item.barcodeName} onError #####")
                                holder.itemView.txt_load_failed.visibility = View.VISIBLE

                            }, {
                                Log.d(TAG, "##### ${item.barcodeName} onCompletion #####")
                                holder.itemView.txt_load_failed.visibility = View.VISIBLE
                            })
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
        mIsRequestItemMove = true
        if (ConstVariables.ITEM_TYPE_EMPTY.equals(mItems.get(toPosition).itemType)) {
            Log.d(TAG, "##### onItemMove ##### This is the end of List. return!!!")
            return
        }
        Collections.swap(mItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemTrackingStart() {
        Log.d(TAG, "##### onItemTrackingStart #####")
    }

    override fun onItemTrackingEnd() {
        Log.d(TAG, "##### onItemTrackingEnd #####")
        if (mIsRequestItemMove) {
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ITEM_MOVE_FINISH))
            mIsRequestItemMove = false
        }
    }

    fun setItems(items: MutableList<BarcodeItem>) {
        Log.d(TAG, "##### setItems #####")
        for (i in 0 until items.size) {
            Log.d(TAG, " ##### test arr ##### ${items[i].barcodeBitmapArr}")
        }
        this.mItems.clear()
        this.mItems.addAll(items)
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

    inner class ViewHolder(itemView: View) : AndroidExtensionViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
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

    abstract class AndroidExtensionViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    }
}