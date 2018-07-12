package srjhlab.com.myownbarcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddFromImageActivity extends AppCompatActivity {
    final int REQ_CODE = 200;
    ImageView imageView;
    MakeBarcode makeBarcode = new MakeBarcode();
    GetByteArrayFromDrawable getByteArrayFromDrawable = new GetByteArrayFromDrawable();
    ScanFromImage scanFromImage = new ScanFromImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_image);
        imageView = (ImageView)findViewById(R.id.img);


        /*
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE);
        */


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            try {
                Bitmap image = data.getParcelableExtra("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                imageView.setImageBitmap(image);
                scanFromImage.putImage(image);
                if (scanFromImage.getFormat() == null || scanFromImage.getValue() == null) {
                    Toast.makeText(getApplicationContext(), "바코드를 인식할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    AddFromImageActivity.this.finish();
                } else {
                    Bitmap bm = makeBarcode.MakeBarcode(scanFromImage.getValue(), scanFromImage.getFormat());
                    Drawable drawable = new BitmapDrawable(bm);
                    Intent intent = new Intent(AddFromImageActivity.this, AddInfoActivity.class);
                    intent.putExtra("barcode", getByteArrayFromDrawable.getByteArrayFromDrawable(drawable));
                    intent.putExtra("type", scanFromImage.getFormat());
                    intent.putExtra("value", scanFromImage.getValue());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                AddFromImageActivity.this.finish();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "이미지 처리 과정에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.fade, R.anim.hold);
                AddFromImageActivity.this.finish();
            }
        }
        else if(resultCode == RESULT_CANCELED){
            overridePendingTransition(R.anim.fade, R.anim.hold);
            AddFromImageActivity.this.finish();
        }
    }
}
