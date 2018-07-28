package srjhlab.com.myownbarcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditInfoActivity extends AppCompatActivity {
    DbHelper dbHelper;
    String id, value;
    byte[] body;
    int color, mid;
    Bitmap bmp;
    GetBitmapFromByteArray getBitmapFromByteArray = new GetBitmapFromByteArray();
    ImageView imageView_barcode, col_1, col_2, col_3, col_4, col_5, col_6, col_7, col_8, col_9, col_10, col_11, col_12;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        dbHelper = new DbHelper(getApplicationContext(), "barcodeimg.db", null, 1);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        body = intent.getByteArrayExtra("body");
        color = intent.getExtras().getInt("color");
        mid = intent.getExtras().getInt("mid");
        value = intent.getExtras().getString("value");
        bmp = getBitmapFromByteArray.convertImg(body);

        imageView_barcode = (ImageView)findViewById(R.id.img_info_barcode);
        imageView_barcode.setImageBitmap(bmp);

        textView = (TextView)findViewById(R.id.txt_value);
        textView.setText("바코드 번호 : " + value);

        editText = (EditText)findViewById(R.id.edtxt_name);
        editText.setText(id);

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

        setFocusInit(color);

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

        Button btn_confirm = (Button)findViewById(R.id.btn_Confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "바코드가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                id = editText.getText().toString();
                dbHelper.update(mid, id, color);
                EditInfoActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
            }
        });

        Button btn_cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditInfoActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
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

    void setFocusInit(int color) {
        if (color == Color.parseColor("#ef9da2"))
            col_1.setSelected(true);
         else if (color == Color.parseColor("#9787be"))
            col_2.setSelected(true);
         else if (color == Color.parseColor("#88d2f4"))
            col_3.setSelected(true);
         else if (color == Color.parseColor("#d4e29b"))
            col_4.setSelected(true);
         else if (color == Color.parseColor("#f9cc8e"))
            col_5.setSelected(true);
         else if (color == Color.parseColor("#f19a7b"))
            col_6.setSelected(true);
         else if (color == Color.parseColor("#f3ebdb"))
            col_7.setSelected(true);
         else if (color == Color.parseColor("#c692bd"))
            col_8.setSelected(true);
         else if (color == Color.parseColor("#93ca9b"))
            col_9.setSelected(true);
         else if (color == Color.parseColor("#9787be"))
            col_10.setSelected(true);
         else if (color == Color.parseColor("#fef499"))
            col_11.setSelected(true);
         else if (color == Color.parseColor("#f4b386"))
            col_12.setSelected(true);
        else
            return;

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Log.d("TAG" , "onResume 호출");
        imageView.setBackgroundColor(UserInfo.color);
        color = UserInfo.color;
        */
    }
}
