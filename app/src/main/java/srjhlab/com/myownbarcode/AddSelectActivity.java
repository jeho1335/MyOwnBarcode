package srjhlab.com.myownbarcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddSelectActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_select);
        Intent intent = getIntent();
        final int pos = intent.getExtras().getInt("position");

        Button btn_sel_key = (Button)findViewById(R.id.btn_sel_key);
        btn_sel_key.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddSelectActivity.this, AddFromKeyActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                AddSelectActivity.this.finish();
            }
        });

        Button btn_sel_scan = (Button)findViewById(R.id.btn_sel_scan);
        btn_sel_scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)MainActivity.context).setScan(pos);
                AddSelectActivity.this.finish();
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });

        Button btn_sel_image = (Button)findViewById(R.id.btn_sel_image);
        btn_sel_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSelectActivity.this, AddFromImageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                AddSelectActivity.this.finish();
            }
        });
    }
}
