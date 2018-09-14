package srjhlab.com.myownbarcode.Utils;

public class ConstVariables {
    //EventBus mesage type
    public final static int EVENTBUS_ADD_NEW_BARCODE = 0;
    public final static int EVENTBUS_SHOW_BARCODE = 1;
    public final static int EVENTBUS_MODIFY_BARCODE = 2;
    public final static int EVENTBUS_ADD_FROM_KEY = 3;
    public final static int EVENTBUS_ADD_FROM_SCAN = 4;
    public final static int EVENTBUS_ADD_FROM_IMAGE = 5;
    public final static int EVENTBUS_ADD_BARCODE = 6;
    public final static int EVENTBUS_ADD_BARCODE_SUCCESS = 7;
    public final static int EVENTBUS_EDIT_BARCODE = 8;
    public final static int EVENTBUS_DELETE_BARCODE = 9;
    public final static int EVENTBUS_SHARE_BARCODE = 10;
    public final static int EVENTBUS_ITEM_MOVE_FINISH = 11;
    public final static int EVENTBUS_CLICK_BARCODELIST = 12;
    public final static int EVENTBUS_LONGCLICK_BARCODELIST = 13;

    //Barcode type variables
    public final static int CODE_39 = 0;
    public final static int CODE_93 = 1;
    public final static int CODE_128 = 2;
    public final static int EAN_8 = 3;
    public final static int EAN_13 = 4;
    public final static int PDF_417 = 5;
    public final static int UPC_A = 6;
    public final static int CODABAR = 7;
    public final static int ITF = 8;
    public final static int QR_CODE = 9;
    public final static int MAXI_CODE = 10;
    public final static int AZTEC = 11;

    //Barcode Item variavles
    public final static int ITEM_TYPE_EMPTY = 0;
    public final static int ITEM_TYPE_BARCODE = 1;

    //Preferences Key
    public final static String PREF_BARCODE_ITEM = "barcode.item.list";
    public final static String PREF_SPLIT = ";";
}
