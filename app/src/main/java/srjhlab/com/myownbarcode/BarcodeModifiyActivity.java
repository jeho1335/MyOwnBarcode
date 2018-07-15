package srjhlab.com.myownbarcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BarcodeModifiyActivity extends AppCompatActivity {
    Button btn_edit, btn_delete;
    int mid, color;
    String id, value;
    byte[] body;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_modifiy);
        dbHelper = new DbHelper(getApplicationContext(), "barcodeimg.db", null, 1);
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        value = intent.getExtras().getString("value");
        body = intent.getByteArrayExtra("body");
        color = intent.getExtras().getInt("color");
        mid = intent.getExtras().getInt("mid");

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarcodeModifiyActivity.this, EditInfoActivity.class);
                intent.putExtra("mid" , mid);
                intent.putExtra("id", id);
                intent.putExtra("body", body);
                intent.putExtra("color", color);
                intent.putExtra("value", value);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
                BarcodeModifiyActivity.this.finish();
            }
        });

        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete(mid);
                Toast.makeText(getApplicationContext(), "바코드가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                BarcodeModifiyActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
            }
        });
    }
}
