package srjhlab.com.myownbarcode;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017-02-08.
 */

public class CardviewItem {
    Bitmap body;
    String id;
    String value;
    int color;
    int mid;

    public Bitmap getBody(){
        return body;
    }

    public String getId(){
        return id;
    }

    public String getValue(){
        return value;
    }
    public int getColor(){
        return color;
    }
    public int getMid(){
        return mid;
    }

    public CardviewItem(int mid, String id, int color, String value, Bitmap body){
        this.mid = mid;
        this.id = id;
        this.color = color;
        this.value = value;
        this.body = body;
    }
}
