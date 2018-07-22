package srjhlab.com.myownbarcode.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import srjhlab.com.myownbarcode.AddFromKeyActivity;

/**
 * Created by Administrator on 2017-05-16.
 */

public class ValidityCheck {
    private final static String TAG = ValidityCheck.class.getSimpleName();

    private static ValidityCheck mInstance = null;
    private Context mContext;

    public static ValidityCheck getInstance(Context context){
        if(mInstance != null){
            return mInstance;
        }else{
            return new ValidityCheck(context);
        }
    }

    private ValidityCheck(Context context){
        this.mContext = context;
        mInstance = this;
    }

    public boolean check(String value, int format) {
        boolean validity = true;
        int length = value.length();
        mContext= AddFromKeyActivity.context;

        switch (format) {
            case ConstVariables.CODE_39:
                validity = checkCODE39(value);
                break;
            case ConstVariables.CODE_98:
                validity = checkCODE93(value);
                break;
            case ConstVariables.CODE_128:
                validity = checkCODE128(value);
                break;
            case ConstVariables.EAN_8:
                validity = checkEAN8(value);
                break;
            case ConstVariables.EAN_13:
                validity = checkEAN13(value);
                break;
            case ConstVariables.PDF_417:
                validity = checkPDF417(value);
                break;
            case ConstVariables.UPC_A:
                validity = checkUPCA(value);
                break;
            case ConstVariables.CODABAR:
                validity = checkCODABAR(value);
                break;
            case ConstVariables.ITF:
                validity = checkITF(value);
                break;
            case ConstVariables.QR_CODE:
                validity = checkQRCODE(value);
                break;
            case ConstVariables.MAXI_CODE:
                validity = checkMAXICODE(value);
                break;
            case ConstVariables.AZTEC:
                validity = checkAZTEC(value);
                break;
        }
        return validity;
    }

    private boolean checkCODE39(String value) {
        Log.d(TAG, "##### checkCODE39 #####");

        boolean validity = true;
        int length = value.length();
        String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length > 80) {
            Toast.makeText(mContext, "요청된 바코드 값은 80자리 이하여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        for (int i = 0; i < length; i++) {
            int indexInString = ALPHABET_STRING.indexOf(value.charAt(i));
            if (indexInString < 0) {
                Toast.makeText(mContext, "잘못된 바코드값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }
        return validity;
    }

    private boolean checkCODE93(String value) {
        Log.d(TAG, "##### checkCODE93 #####");

        boolean validity = true;
        int length = value.length();

        final char ESCAPE_FNC_1 = '\u00f1';
        final char ESCAPE_FNC_2 = '\u00f2';
        final char ESCAPE_FNC_3 = '\u00f3';
        final char ESCAPE_FNC_4 = '\u00f4';

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length < 1 || length > 80) {
            Toast.makeText(mContext, "요청된 바코드 값은 1~80자리 사이 여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < ' ' || c > '~') {
                switch (c) {
                    case ESCAPE_FNC_1:
                    case ESCAPE_FNC_2:
                    case ESCAPE_FNC_3:
                    case ESCAPE_FNC_4:
                        break;
                    default:
                        Toast.makeText(mContext, "잘못된 바코드값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                        validity = false;
                        return validity;
                }
            }
        }
        return validity;
    }

    private boolean checkCODE128(String value) {
        Log.d(TAG, "##### checkCODE128 #####");

        boolean validity = true;
        int length = value.length();

        final char ESCAPE_FNC_1 = '\u00f1';
        final char ESCAPE_FNC_2 = '\u00f2';
        final char ESCAPE_FNC_3 = '\u00f3';
        final char ESCAPE_FNC_4 = '\u00f4';

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length < 1 || length > 80) {
            Toast.makeText(mContext, "요청된 바코드 값은 1~80자리 사이 여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < ' ' || c > '~') {
                switch (c) {
                    case ESCAPE_FNC_1:
                    case ESCAPE_FNC_2:
                    case ESCAPE_FNC_3:
                    case ESCAPE_FNC_4:
                        break;
                    default:
                        Toast.makeText(mContext, "잘못된 바코드값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                        validity = false;
                        return validity;
                }
            }
        }
        return validity;
    }

    private boolean checkEAN8(String value) {
        Log.d(TAG, "##### checkEAN8 #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length != 8) {
            Toast.makeText(mContext, "요청된 바코드 값은 8자리 이하여야 합니다. 현재 자리수 :  " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        return validity;
    }

    private boolean checkEAN13(String value) {
        Log.d(TAG, "##### checkEAN13 #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length != 13) {
            Toast.makeText(mContext, "요청된 바코드 값은 13자리 이하여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        return validity;
    }

    private boolean checkPDF417(String value) {
        Log.d(TAG, "##### checkPDF417 #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        return validity;
    }

    private boolean checkUPCA(String value) {
        Log.d(TAG, "##### checkUPCA #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length != 12) {
            Toast.makeText(mContext, "요청된 바코드 값은 11자리 또는 12자리가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        return validity;
    }

    private boolean checkUPCE(String value) {
        Log.d(TAG, "##### checkUPCE #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length != 8) {
            Toast.makeText(mContext, "요청된 바코드 값은 8자리가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        return validity;
    }

    private boolean checkCODABAR(String value) {
        Log.d(TAG, "##### checkCODABAR #####");

        boolean validity = true;
        int length = value.length();

        final char[] START_END_CHARS = new char[]{'A', 'B', 'C', 'D'};
        final char[] ALT_START_END_CHARS = new char[]{'T', 'N', '*', 'E'};

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length >= 2) {
            char resultLength = Character.toUpperCase(value.charAt(0));
            char result = Character.toUpperCase(value.charAt(value.length() - 1));
            boolean position = arrayContains(START_END_CHARS, resultLength);
            boolean index = arrayContains(START_END_CHARS, result);
            boolean c = arrayContains(ALT_START_END_CHARS, resultLength);
            boolean code = arrayContains(ALT_START_END_CHARS, result);
            if (position) {
                if (!index) {
                    Toast.makeText(mContext, "잘못된 바코드 값입니다. " + value, Toast.LENGTH_SHORT).show();
                    validity = false;
                    return validity;
                }
            } else if (c) {
                if (!code) {
                    Toast.makeText(mContext, "잘못된 바코드 값입니다." + value, Toast.LENGTH_SHORT).show();
                    validity = false;
                    return validity;
                }
            } else {
                if (index || code) {
                    Toast.makeText(mContext, "잘못된 바코드 값입니다. " + value, Toast.LENGTH_SHORT).show();
                    validity = false;
                    return validity;
                }
            }
        }
        return validity;
    }

    private boolean checkQRCODE(String value) {
        Log.d(TAG, "##### checkQRCODE #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        return validity;
    }

    private boolean checkITF(String value) {
        Log.d(TAG, "##### checkITF #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }

        if (length % 2 != 0) {
            Toast.makeText(mContext, "요청된 바코드 값은 짝수가 되어야 합니다.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        } else if (length > 80) {
            Toast.makeText(mContext, "요청된 바코드 값의 자리수는 80자리 이하가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        } else
            return validity;
    }

    private boolean checkMAXICODE(String value) {
        Log.d(TAG, "##### checkMAXICODE #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        return validity;
    }

    private boolean checkAZTEC(String value) {
        Log.d(TAG, "##### checkAZTEC #####");

        boolean validity = true;
        int length = value.length();

        if (value.isEmpty()) {
            Toast.makeText(mContext, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            validity = false;
            return validity;
        }
        return validity;
    }

    static boolean arrayContains(char[] array, char key) {
        if (array != null) {
            for (char c : array) {
                if (c == key) {
                    return true;
                }
            }
        }
        return false;
    }
}
