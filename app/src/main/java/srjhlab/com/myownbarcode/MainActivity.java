package srjhlab.com.myownbarcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter;
import srjhlab.com.myownbarcode.CommonUi.AddBarcodeInfoDialog;
import srjhlab.com.myownbarcode.CommonUi.AddFromImageDialog;
import srjhlab.com.myownbarcode.CommonUi.AddFromKeyDialog;
import srjhlab.com.myownbarcode.CommonUi.BarcodeFocusDialog;
import srjhlab.com.myownbarcode.CommonUi.BarcodeModifyDialog;
import srjhlab.com.myownbarcode.CommonUi.SelectDialog;
import srjhlab.com.myownbarcode.Item.BarcodeItem;
import srjhlab.com.myownbarcode.Item.SelectDialogItem;
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct;
import srjhlab.com.myownbarcode.Utils.CommonUtils;
import srjhlab.com.myownbarcode.Utils.ConstVariables;
import srjhlab.com.myownbarcode.Utils.DbHelper;

public class MainActivity extends AppCompatActivity implements BarcodeRecyclerviewAdapter.IOnClick {
    private final static String TAG = MainActivity.class.getSimpleName();

    private final int MY_PERMISSIONS_REQUEST = 1;

    private BarcodeItem mItem;
    private List<BarcodeItem> mItems;
    private RecyclerView mRecyclerview;
    private BarcodeRecyclerviewAdapter mAdapter;
    private DbHelper mDBHelper;
    private Bitmap mDefaultImage;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "##### oncreate #####");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mDBHelper = new DbHelper(getApplicationContext(), "barcodeimg.db", null, 1);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mItems = new ArrayList<>();
        mAdapter = new BarcodeRecyclerviewAdapter(this);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.img_ref);
        mDefaultImage = drawable.getBitmap();

        mTextView = (TextView) findViewById(R.id.textview_license);
        String content = "오픈소스 라이센스";
        mTextView.setText(Html.fromHtml("<u>" + content + "</u>"));
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LicenseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
            }
        });
        initListItem();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy #####");
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "##### onActivityResult #####");
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "value : " + result.getContents() + "\n" + "format : " + result.getFormatName());

        if (result.getFormatName() == null) {
            return;
        }

        if (result.getFormatName().equals("CODE_39")
                || result.getFormatName().equals("CODE_93")
                || result.getFormatName().equals("CODE_128")
                || result.getFormatName().equals("EAN_8")
                || result.getFormatName().equals("EAN_13")
                || result.getFormatName().equals("PDF_417")
                || result.getFormatName().equals("UPC_A")
                || result.getFormatName().equals("UPC_E")
                || result.getFormatName().equals("CODABAR")
                || result.getFormatName().equals("ITF")
                || result.getFormatName().equals("QR_CODE")
                || result.getFormatName().equals("AZTEC")) {
            AddBarcodeInfoDialog.newInstance()
                    .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                    .setBarcodeItem(new BarcodeItem(result.getContents(), CommonUtils.convertBarcodeType(MainActivity.this, result.getFormatName())))
                    .show(getFragmentManager(), AddBarcodeInfoDialog.class.getSimpleName());

        } else {
            Toast.makeText(this, "지원하지 않는 포맷입니다", Toast.LENGTH_SHORT).show();
        }
    }

    void initListItem() {
        Log.d(TAG, "##### initListItem #####");
        if (mItems != null) {
            mItems.clear();
        }
        for (int i = 0; i <= mDBHelper.getLength(); i++) {
            if (mDBHelper.getBarcode()[i] != null) {
                mItem = new BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mDBHelper.getId()[i], mDBHelper.getName()[i], mDBHelper.getColor()[i], mDBHelper.getValue()[i], makeBarcodeFromDB(mDBHelper.getBarcode()[i]));
            } else {
                mItem = new BarcodeItem(ConstVariables.ITEM_TYPE_EMPTY, 0, "새 바코드 추가", 0, " ", mDefaultImage);
            }
            mItems.add(mItem);
        }
        ((BarcodeRecyclerviewAdapter) mRecyclerview.getAdapter()).setItems(mItems);
    }

    void setBarcodeScan() {
        Log.d(TAG, "##### setBarcodeScan ######");

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                }
            }

        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            integrator.addExtra("PROMPT_MESSAGE", "바코드를 사각형 안에 비춰주세요. \n"
                    + "스캔이 잘 안될 경우 주위 배경을 깔끔하게 해주세요. \n"
                    + "볼륨 UP/DOWN 버튼으로 플래시를 켜고 끌 수 있습니다.");
            integrator.setWide();
            integrator.initiateScan();
        }
    }

    public Bitmap makeBarcodeFromDB(byte[] b) {
        Log.d(TAG, "##### getBarcodeFromDb ######");

        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    @Override
    public void onItemClick(BarcodeItem item) {
        switch (item.getItemType()) {
            case ConstVariables.ITEM_TYPE_EMPTY:
                Log.d(TAG, "##### onItemClick ##### ITEM_TYPE_EMPTY");
                List<SelectDialogItem> mItems = new ArrayList<>();
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_SELF));
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_SCAN));
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_IMAGE));

                if (mItems != null) {
                    final SelectDialog selectDialog = SelectDialog.newInstance();
                    selectDialog.setItems(mItems);
                    selectDialog.show(getFragmentManager(), selectDialog.getClass().getSimpleName());
                }
                break;
            case ConstVariables.ITEM_TYPE_BARCODE:
                Log.d(TAG, "##### onItemClick ##### ITEM_TYPE_BARCODE");
                if (item != null) {
                    final BarcodeFocusDialog barcodeFocusDiaolg = BarcodeFocusDialog.newInstance();
                    barcodeFocusDiaolg
                            .setItem(item)
                            .setType(barcodeFocusDiaolg.VIEW_TYPE_FOCUS)
                            .show(getFragmentManager(), barcodeFocusDiaolg.getClass().getSimpleName());
                }

                break;
        }
    }

    @Override
    public void onItemLongClick(BarcodeItem item) {
        switch (item.getItemType()) {
            case ConstVariables.ITEM_TYPE_EMPTY:
                Log.d(TAG, "##### onItemLongClick ##### ITEM_TYPE_EMPTY");
                break;
            case ConstVariables.ITEM_TYPE_BARCODE:
                Log.d(TAG, "##### onItemLongClick ##### ITEM_TYPE_BARCODE");
                if (item != null) {
                    final BarcodeModifyDialog barcodeModifyDialog = BarcodeModifyDialog.newInstance();
                    barcodeModifyDialog.setItem(item);
                    barcodeModifyDialog.show(getFragmentManager(), barcodeModifyDialog.getClass().getSimpleName());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    integrator.addExtra("PROMPT_MESSAGE", "바코드를 사각형 안에 비춰주세요. \n" + "스캔이 잘 안될 경우 주위 배경을 깔끔하게 해주세요.");
                    integrator.initiateScan();
                } else {
                    Toast.makeText(this, "카메라 사용 권한을 허용해야 카메라 스캔을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    /*
     * START EVENTBUS RECEIVE
     * */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonEventbusObejct object) {
        switch (object.getType()) {
            case ConstVariables.EVENTBUS_ADD_NEW_BARCODE:
                Log.d(TAG, "##### EVENTBUS_ADD_NEW_BARCODE #####");
                /*List<SelectDialogItem> mItems = new ArrayList<>();
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_SELF));
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_SCAN));
                mItems.add(new SelectDialogItem(SelectDialogItem.INPUT_IMAGE));

                if (mItems != null) {
                    final SelectDialog selectDialog = SelectDialog.newInstance();
                    selectDialog.setItems(mItems);
                    selectDialog.show(getFragmentManager(), selectDialog.getClass().getSimpleName());
                }*/
                break;
            case ConstVariables.EVENTBUS_SHOW_BARCODE:
                Log.d(TAG, "##### EVENTBUS_SHOW_BARCODE #####");
                break;
            case ConstVariables.EVENTBUS_MODIFY_BARCODE:
                Log.d(TAG, "##### EVENTBUS_MODIFY_BARCODE #####");
                break;
            case ConstVariables.EVENTBUS_ADD_FROM_KEY:
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_KEY #####");
                AddFromKeyDialog dialog = AddFromKeyDialog.newInstance();
                dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
                break;
            case ConstVariables.EVENTBUS_ADD_FROM_CCAN:
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_CCAN #####");
                setBarcodeScan();
                break;
            case ConstVariables.EVENTBUS_ADD_FROM_IMAGE:
                Log.d(TAG, "##### EVENTBUS_ADD_FROM_IMAGE #####");
                AddFromImageDialog addFromImageDialog = AddFromImageDialog.newInstance();
                addFromImageDialog.show(getFragmentManager(), addFromImageDialog.getClass().getSimpleName());
                break;
            case ConstVariables.EVENTBUS_ADD_BARCODE:
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE #####");
                AddBarcodeInfoDialog.newInstance()
                        .setBarcodeItem((BarcodeItem) object.getVal())
                        .setCommandType(AddBarcodeInfoDialog.MODE_ADD_BARCODE)
                        .show(getFragmentManager(), AddBarcodeInfoDialog.class.getSimpleName());
                break;
            case ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS:
                initListItem();
                Log.d(TAG, "##### EVENTBUS_ADD_BARCODE_SUCCESS #####");
                break;
            case ConstVariables.EVENTBUS_EDIT_BARCODE:
                Log.d(TAG, "##### EVENTBUS_EDIT_BARCODE #####");
                AddBarcodeInfoDialog.newInstance()
                        .setBarcodeItem((BarcodeItem) object.getVal())
                        .setCommandType(AddBarcodeInfoDialog.MODE_EDIT_BARCODE)
                        .show(getFragmentManager(), AddBarcodeInfoDialog.class.getSimpleName());
                break;
            case ConstVariables.EVENTBUS_DELETE_BARCODE:
                Log.d(TAG, "##### EVENTBUS_DELETE_BARCODE #####");
                mDBHelper.delete(((BarcodeItem)object.getVal()).getBarcodeId());
                initListItem();
                break;
            case ConstVariables.EVENTBUS_SHARE_BARCODE:
                Log.d(TAG, "##### EVENTBUS_SHARE_BARCODE #####");

                final BarcodeFocusDialog barcodeFocusDiaolg = BarcodeFocusDialog.newInstance();
                barcodeFocusDiaolg
                        .setItem(((BarcodeItem)object.getVal()))
                        .setType(barcodeFocusDiaolg.VIEW_TYPE_SHARE)
                        .show(getFragmentManager(), barcodeFocusDiaolg.getClass().getSimpleName());
        }
    }
    /*
     * END EVENTBUS RECEIVE
     * */
}
