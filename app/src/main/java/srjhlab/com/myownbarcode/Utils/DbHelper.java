package srjhlab.com.myownbarcode.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.Drawable;
import android.util.Log;

import srjhlab.com.myownbarcode.GetByteArrayFromDrawable;

/**
 * Created by Administrator on 2016-10-21.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "barcodeimg.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DbHelper mDBHelper;
    private Context mCtx;
    private int size;
    GetByteArrayFromDrawable convert = new GetByteArrayFromDrawable();

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_wor = "CREATE TABLE barcode(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, color INTEGER, value TEXT, icon BLOB);";
        Log.d("TAG" , "db생성");
        db.execSQL(sql_wor);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String name, int color, String value, Drawable img) {
        byte[] barcode = convert.getByteArrayFromDrawable(img);
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement p = db.compileStatement("INSERT INTO barcode values(null, '" + name + "', " + color+ ", '" + value + "', ?);");
        p.bindBlob(1, barcode);
        p.execute();
        Log.d("tag" , "db삽입 : " + barcode);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM barcode"  + " WHERE id = '" + id + "';");
        Log.d("TAG", "db삭제 : " + id);
        db.close();
    }

    public void update(int id, String name, int color){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE barcode SET name = '" + name + "', color = '" + color + "' WHERE id = '" + id + "';");
        Log.d("TAG", "db업데이트 : " + id);
        db.close();
    }

    public int getLength(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM barcode", null);
        Log.d("TAG" , "컬럼 개수 조회");
        size = cursor.getCount();
        return cursor.getCount();
    }

    public byte[][] getBarcode() {
        SQLiteDatabase db = getReadableDatabase();
        byte[][] result = new byte[size + 1][];
        Cursor cursor = db.rawQuery("SELECT icon FROM barcode;", null);
        while (cursor.moveToNext()) {
            int i;
            i = cursor.getPosition();
            result[i] = cursor.getBlob(0);
            Log.d("TAG" , "배열 : " + result[i]);
        }
        return result;
    }

    public String[] getName(){
        SQLiteDatabase db = getReadableDatabase();
        String[] name = new String[size + 1];
        Cursor cursor = db.rawQuery("SELECT name FROM barcode;", null);
        while (cursor.moveToNext()){
            int i;
            i = cursor.getPosition();
            name[i] = cursor.getString(0);
        }
        return name;
    }

    public int[] getColor(){
        SQLiteDatabase db = getReadableDatabase();
        int[] color = new int[size + 1];
        Cursor cursor = db.rawQuery("SELECT color FROM barcode;", null);
        while (cursor.moveToNext()){
            int i;
            i = cursor.getPosition();
            color[i] = cursor.getInt(0);
        }
        return color;
    }

    public String[] getValue(){
        SQLiteDatabase db = getReadableDatabase();
        String[] value = new String[size + 1];
        Cursor cursor = db.rawQuery("SELECT value FROM barcode;", null);
        while (cursor.moveToNext()){
            int i;
            i = cursor.getPosition();
            value[i] = cursor.getString(0);
        }
        return value;
    }

    public int[] getId(){
            SQLiteDatabase db = getReadableDatabase();
            int[] id = new int[size + 1];
            Cursor cursor = db.rawQuery("SELECT id FROM barcode;", null);
            while (cursor.moveToNext()){
                int i;
                i = cursor.getPosition();
                id[i] = cursor.getInt(0);
            }
            return id;
    }
    public void close() {
        mDB.close();
    }
}
