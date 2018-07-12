package srjhlab.com.myownbarcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST = 1;
    public static Context context;
    CardviewItem[] item;
    static List<CardviewItem> items;
    static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    public static int cardLength;
    DbHelper dbHelper;
    byte[][] barcodeImg;
    String[] barcodeName;
    String[] barcodeValue;
    int[] barcodeColor;
    int[] barcodeId;
    Bitmap img_ref;
    GetByteArrayFromDrawable getByteArrayFromDrawable = new GetByteArrayFromDrawable();
    MakeBarcode makeBarcode;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        dbHelper = new DbHelper(getApplicationContext(), "barcodeimg.db", null, 1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        makeBarcode = new MakeBarcode();

        textView = (TextView)findViewById(R.id.textview_license);
        String content = "오픈소스 라이센스";
        textView.setText(Html.fromHtml("<u>" + content + "</u>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LicenseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardLength = dbHelper.getLength();
        Log.d("TAG" , "현 DB 컬럼 개수 : " + cardLength);
        item = new CardviewItem[cardLength + 1];
        items = new ArrayList<>();
        barcodeImg = new byte[cardLength + 1][];
        barcodeName = new String[cardLength + 1];
        barcodeValue = new String[cardLength + 1];
        barcodeColor = new int[cardLength + 1];
        barcodeId = new int[cardLength + 1];
        BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.img_ref);
        img_ref = drawable.getBitmap();
        setItem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
         Log.d("TAG" , "value : " + result.getContents() + "\n" + "format : " + result.getFormatName());

        if(result.getFormatName() == null){
            return;
        }

        if(result.getFormatName().equals("CODE_39") || result.getFormatName().equals("CODE_93") || result.getFormatName().equals("CODE_128") || result.getFormatName().equals("EAN_8") ||
                result.getFormatName().equals("EAN_13") || result.getFormatName().equals("PDF_417") || result.getFormatName().equals("UPC_A") || result.getFormatName().equals("UPC_E") ||
                result.getFormatName().equals("CODABAR") || result.getFormatName().equals("ITF") || result.getFormatName().equals("QR_CODE") || result.getFormatName().equals("AZTEC")){
            Bitmap bm = makeBarcode.MakeBarcode(result.getContents(), result.getFormatName());
            Drawable drawable = new BitmapDrawable(bm);
            Intent intent = new Intent(MainActivity.this, AddInfoActivity.class);
            intent.putExtra("barcode", getByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
            intent.putExtra("type", result.getFormatName());
            intent.putExtra("value", result.getContents());
            startActivity(intent);
            overridePendingTransition(R.anim.fade, R.anim.hold);
        }
        else {
            Toast.makeText(context, "지원하지 않는 포맷입니다", Toast.LENGTH_SHORT).show();
        }
    }

    void setItem(){
        barcodeId = dbHelper.getId();
        barcodeName = dbHelper.getName();
        barcodeValue = dbHelper.getValue();
        barcodeColor = dbHelper.getColor();
        barcodeImg = dbHelper.getBarcode();
        for(int i = 0; i <= cardLength; i++){
           if(barcodeImg[i] != null){
               item[i] = new CardviewItem(barcodeId[i], barcodeName[i], barcodeColor[i], barcodeValue[i], makeBarcodeFromDB(barcodeImg[i]));
               Log.d("TAG" , "추가되는 아이템 : " + barcodeId[i] + " " + barcodeName[i] + " " +  barcodeColor[i] +  " " + barcodeValue[i] + " " + makeBarcodeFromDB(barcodeImg[i]));
            }
            else {
                item[i] = new CardviewItem(0, "새 바코드 추가", 0, " ", img_ref);
            }
            items.add(item[i]);
            }
            recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(), items, R.layout.activity_main));
        }

    void setScan(int position){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

                }
            }

        }else{
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            integrator.addExtra("PROMPT_MESSAGE", "바코드를 사각형 안에 비춰주세요. \n"
                    + "스캔이 잘 안될 경우 주위 배경을 깔끔하게 해주세요. \n"
                    + "볼륨 UP/DOWN 버튼으로 플래시를 켜고 끌 수 있습니다. ");
            integrator.initiateScan();
            //ew IntentIntegrator(MainActivity.this).initiateScan();
        }

    }

    public Bitmap makeBarcodeFromDB(byte[] b){
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
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
                }
                else {
                    Toast.makeText(context, "카메라 사용 권한을 허용해야 카메라 스캔을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
