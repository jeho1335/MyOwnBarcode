package srjhlab.com.myownbarcode.Adapter

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemTouchHelper(adapter: IItemTouchHelperAdapter) : ItemTouchHelper.Callback() {
    val TAG = this.javaClass.simpleName
    private var mListener: IItemTouchHelperAdapter = adapter

    interface IItemTouchHelperAdapter {
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemTrackingStart()
        fun onItemTrackingEnd()
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        Log.d(TAG, "##### getMovementFlags #####")
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        Log.d(TAG, "##### onSelectedChanged #####")
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            2 -> mListener.onItemTrackingStart()
            0 -> mListener.onItemTrackingEnd()
        }
    }
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        Log.d(TAG, "##### onMove ##### firstPosition : ${viewHolder.adapterPosition}, secondPosition : ${target.adapterPosition}")
        mListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }
}