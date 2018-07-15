package srjhlab.com.myownbarcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BarcodeFocusActivity extends AppCompatActivity {
    byte[] body;
    Bitmap bmp;
    GetBitmapFromByteArray getBitmapFromByteArray;
    String name, value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_focus);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getBitmapFromByteArray = new GetBitmapFromByteArray();

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        body = intent.getByteArrayExtra("body");
        value = intent.getExtras().getString("value");
        bmp = getBitmapFromByteArray.convertImg(body);

        TextView textView_name = (TextView)findViewById(R.id.text_name);
        textView_name.setText(name);

        ImageView imageView = (ImageView)findViewById(R.id.img_barcode);
        imageView.setImageBitmap(bmp);

        TextView textView = (TextView)findViewById(R.id.text_value);
        textView.setText(value);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BarcodeFocusActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.hold);
    }
}
