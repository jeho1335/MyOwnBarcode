package srjhlab.com.myownbarcode.Utils;

 public class ConstVariables {

    /*
    * Start EventBus message type
    * */

    public final static int EVENTBUS_ADD_NEW_BARCODE = 0;
    public final static int EVENTBUS_SHOW_BARCODE = 1;
    public final static int EVENTBUS_MODIFY_BARCODE = 2;
    public final static int EVENTBUS_ADD_FROM_KEY = 3;
    public final static int EVENTBUS_ADD_FROM_CCAN = 4;
    public final static int EVENTBUS_ADD_FROM_IMAGE = 5;
    public final static int EVENTBUS_ADD_BARCODE = 6;
    public final static int EVENTBUS_ADD_BARCODE_SUCCESS = 7;

    /*
    * End EventBus message type
    * */

    /*
    * Start BarcodeType variables
    * */

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

    /*
    * End BarcodeType variables
    * */
}
