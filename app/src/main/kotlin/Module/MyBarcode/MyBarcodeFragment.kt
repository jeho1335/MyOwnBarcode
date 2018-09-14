package srjhlab.com.myownbarcode.Module.MyBarcode

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_fragment_my_barcode.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Adapter.RecyclerViewItemTouchHelper
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class MyBarcodeFragment : Fragment(), MyBarcode.view {
    val TAG = this.javaClass.simpleName

    private val PERMISSIONS_REQUEST = 1
    private lateinit var mPresenter: MyBarcodePresenter

    private lateinit var mItems: MutableList<BarcodeItem>
    private lateinit var mAdapter: BarcodeRecyclerviewAdapter
    private var mLongClickPosition: Int = -1

    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mRecyclerViewItemTouchHelper: RecyclerViewItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onCreate #####")
        mPresenter = MyBarcodePresenter(context, this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.layout_fragment_my_barcode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onResultBarcodeList(items: MutableList<BarcodeItem>) {
        Log.d(TAG, "##### onResultBarcodeList #####")
        mItems = PreferencesManager.loadBarcodeItemList(activity)
        (recyclerView.adapter as BarcodeRecyclerviewAdapter).setItems(mItems)
    }

    private val mIOnCLickListener: BarcodeRecyclerviewAdapter.IOnClick = object : BarcodeRecyclerviewAdapter.IOnClick {
        override fun onItemClick(item: BarcodeItem) {
            Log.d(TAG, "##### onItemClick #####")
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_CLICK_BARCODELIST, item))
        }

        override fun onItemLongCLick(item: BarcodeItem, position: Int) {
            Log.d(TAG, "##### onItemLongCLick #####")
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_LONGCLICK_BARCODELIST, item))
            mLongClickPosition = position
        }

        override fun onStartDrag(viewHolder: BarcodeRecyclerviewAdapter.ViewHolder) {
            Log.d(TAG, "##### onStartDrag #####")
            mItemTouchHelper.startDrag(viewHolder)
        }
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")

        mItems = ArrayList<BarcodeItem>()
        mAdapter = BarcodeRecyclerviewAdapter(mIOnCLickListener)
        mRecyclerViewItemTouchHelper = RecyclerViewItemTouchHelper(mAdapter as RecyclerViewItemTouchHelper.IItemTouchHelperAdapter)
        mItemTouchHelper = ItemTouchHelper(mRecyclerViewItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        mPresenter.requestBarcodeList()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(busObject: CommonEventbusObejct) {
        when (busObject.type) {
            ConstVariables.EVENTBUS_ADD_NEW_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_ADD_NEW_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                mPresenter.requestAddBarcode(recyclerView.adapter as BarcodeRecyclerviewAdapter, item)
            }
            ConstVariables.EVENTBUS_MODIFY_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_MODIFY_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                mPresenter.requestModifyBarcode(mLongClickPosition, recyclerView.adapter as BarcodeRecyclerviewAdapter, item)
            }
            ConstVariables.EVENTBUS_DELETE_BARCODE -> {
                Log.d(TAG, "##### EVENTBUS_DELETE_BARCODE #####")
                val item = busObject.`val` as BarcodeItem
                mPresenter.requestDeleteBarcode(mLongClickPosition, recyclerView.adapter as BarcodeRecyclerviewAdapter, item)
            }
            ConstVariables.EVENTBUS_ITEM_MOVE_FINISH -> {
                Log.d(TAG, "#####  EVENTBUS_ITEM_MOVE_FINISH  #####")
                mPresenter.requestSaveBarcodeList(recyclerView.adapter as BarcodeRecyclerviewAdapter)
            }

        }
    }
}