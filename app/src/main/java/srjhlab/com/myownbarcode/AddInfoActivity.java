package srjhlab.com.myownbarcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddInfoActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ImageView col_1, col_2, col_3, col_4, col_5, col_6, col_7, col_8, col_9, col_10, col_11, col_12;
    public static Context context;
    String format;
    String value;
    String name;
    int color = 0;
    byte[] img;
    GetBitmapFromByteArray getBitmapFromByteArray;
    public int key = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        context = this;
        dbHelper = new DbHelper(getApplicationContext(), "barcodeimg.db", null, 1);
        getBitmapFromByteArray = new GetBitmapFromByteArray();
        key = 0;

        final GetBitmapFromByteArray getBitmapFromByteArray = new GetBitmapFromByteArray();
        Intent intent = getIntent();
        format = intent.getExtras().getString("type");
        value = intent.getExtras().getString("value");
        img = intent.getExtras().getByteArray("barcode");
        ImageView imageView_barcode = (ImageView) findViewById(R.id.img_info_barcode);
        imageView_barcode.setImageBitmap(getBitmapFromByteArray.convertImg(img));

        TextView textView_format = (TextView) findViewById(R.id.txt_type);
        textView_format.setText("바코드 포맷 : " + format);

        TextView textView_value = (TextView) findViewById(R.id.txt_value);
        textView_value.setText("바코드 번호 : " + value);


        col_1 = (ImageView) findViewById(R.id.color_pic_1);
        col_2 = (ImageView) findViewById(R.id.color_pic_2);
        col_3 = (ImageView) findViewById(R.id.color_pic_3);
        col_4 = (ImageView) findViewById(R.id.color_pic_4);
        col_5 = (ImageView) findViewById(R.id.color_pic_5);
        col_6 = (ImageView) findViewById(R.id.color_pic_6);
        col_7 = (ImageView) findViewById(R.id.color_pic_7);
        col_8 = (ImageView) findViewById(R.id.color_pic_8);
        col_9 = (ImageView) findViewById(R.id.color_pic_9);
        col_10 = (ImageView) findViewById(R.id.color_pic_10);
        col_11 = (ImageView) findViewById(R.id.color_pic_11);
        col_12 = (ImageView) findViewById(R.id.color_pic_12);

        col_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_1.setSelected(true);
                color = Color.parseColor("#ef9da2");
            }
        });

        col_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_2.setSelected(true);
                color = Color.parseColor("#9787be");
            }
        });

        col_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_3.setSelected(true);
                color = Color.parseColor("#88d2f4");
            }
        });

        col_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_4.setSelected(true);
                color = Color.parseColor("#d4e29b");
            }
        });

        col_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_5.setSelected(true);
                color = Color.parseColor("#f9cc8e");
            }
        });

        col_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_6.setSelected(true);
                color = Color.parseColor("#f19a7b");
            }
        });

        col_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_7.setSelected(true);
                color = Color.parseColor("#f3ebdb");
            }
        });

        col_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_8.setSelected(true);
                color = Color.parseColor("#c692bd");
            }
        });

        col_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_9.setSelected(true);
                color = Color.parseColor("#93ca9b");
            }
        });

        col_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_10.setSelected(true);
                color = Color.parseColor("#9787be");
            }
        });

        col_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_11.setSelected(true);
                color = Color.parseColor("#fef499");
            }
        });

        col_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                col_12.setSelected(true);
                color = Color.parseColor("#f4b386");
            }
        });

        Button btn_confirm = (Button) findViewById(R.id.btn_Confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(color == 0) {
                    Toast.makeText(context, "색상을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    EditText editText = (EditText) findViewById(R.id.edtxt_name);
                    name = editText.getText().toString();

                    Drawable drawable = new BitmapDrawable(getBitmapFromByteArray.convertImg(img));
                    dbHelper.insert(name, color, value, drawable);
                    AddInfoActivity.this.finish();
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddInfoActivity.this.finish();
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });


    }

    void setFalse(){
        col_1.setSelected(false);
        col_2.setSelected(false);
        col_3.setSelected(false);
        col_4.setSelected(false);
        col_5.setSelected(false);
        col_6.setSelected(false);
        col_7.setSelected(false);
        col_8.setSelected(false);
        col_9.setSelected(false);
        col_10.setSelected(false);
        col_11.setSelected(false);
        col_12.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        imageView_color.setBackgroundColor(UserInfo.color);
        color = UserInfo.color;
        */
    }

}
