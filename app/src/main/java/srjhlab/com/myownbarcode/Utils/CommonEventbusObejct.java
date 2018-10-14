package srjhlab.com.myownbarcode.Utils;

public class CommonEventbusObejct {
    private int mType;
    private Object mVal;

    public CommonEventbusObejct(int type){
        this.mType = type;
    }

    public

    CommonEventbusObejct(int type, Object val){
        this.mType = type;
        this.mVal = val;
    }

    public int getType(){
        return this.mType;
    }

    public Object getVal(){
        return this.mVal;
    }
}
