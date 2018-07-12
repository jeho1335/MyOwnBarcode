package srjhlab.com.myownbarcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFromKeyActivity extends AppCompatActivity {
    public static Context context;
    ViewPager viewPager;
    String format, value;
    Bitmap bmp;
    EditText editText;
    Button btn_confirm, btn_cancel;
    MakeBarcode makeBarcode = new MakeBarcode();
    GetByteArrayFromDrawable convert = new GetByteArrayFromDrawable();
    ValidityCheck validityCheck = new ValidityCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_key);
        convertFormat(0);
        context = this;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                Log.d("TAG" , "현재 페이지 : " + position);
                convertFormat(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        editText = (EditText)findViewById(R.id.edtxt_value);
        btn_confirm = (Button)findViewById(R.id.btn_Confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = editText.getText().toString();
                //bmp = makeBarcode.MakeBarcode(value, format);
                if(validityCheck.check(value, format) == true) {
                    bmp = makeBarcode.MakeBarcode(value, format);
                    Drawable drawable = new BitmapDrawable(bmp);
                    Intent intent = new Intent(AddFromKeyActivity.this, AddInfoActivity.class);
                    intent.putExtra("barcode", convert.getByteArrayFromDrawable(drawable));
                    intent.putExtra("type", format);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    AddFromKeyActivity.this.finish();
                }
            }
        });

        btn_cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFromKeyActivity.this.finish();
            }
        });

    }
    void convertFormat(int pos){
        if(pos == 0)
            format = "CODE_39";
        if (pos == 1)
            format = "CODE_93";
        if (pos == 2)
            format = "CODE_128";
        if (pos == 3)
            format = "EAN_8";
        if (pos == 4)
            format = "EAN_13";
        if (pos == 5)
            format = "PDF_417";
        if (pos == 6)
            format = "UPC_A";
        if (pos == 7)
            format = "CODABAR";
        if (pos == 8)
            format = "ITF";
        if (pos == 9)
            format = "QR_CODE";
        if (pos == 10)
            format = "DATA_MATRIX";
        if (pos == 11)
            format = "AZTEC";
    }

    View.OnClickListener movePageListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int tag = (int)v.getTag();
            viewPager.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new Frag_CODE_39();
                case 1:
                    return new Frag_CODE_93();
                case 2:
                    return new Frag_CODE_128();
                case 3:
                    return new Frag_EAN_8();
                case 4:
                    return new Frag_EAN_13();
                case 5:
                    return new Frag_PDF_417();
                case 6:
                    return new Frag_UPC_A();
                case 7:
                    return new Frag_CODABAR();
                case 8:
                    return new Frag_ITF();
                case 9:
                    return new Frag_QRCODE();
                case 10:
                    return new Frag_MAXICODE();
                case 11:
                    return new Frag_AZTEC();

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 12;
        }
    }

}
