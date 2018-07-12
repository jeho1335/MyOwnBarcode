package srjhlab.com.myownbarcode;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.oned.CodaBarReader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.UPCEANReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;

/**
 * Created by Administrator on 2017-05-16.
 */

public class ValidityCheck {
    Context context;

    public boolean check(String value, String format) {
        boolean validity = true;
        int length = value.length();
        context = AddFromKeyActivity.context;

        if(format.equals("CODE_39")) {
            String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";

            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if (length > 80) {
                Toast.makeText(context, "요청된 바코드 값은 80자리 이하여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            for (int i = 0; i < length; i++) {
                int indexInString = ALPHABET_STRING.indexOf(value.charAt(i));
                if (indexInString < 0) {
                    Toast.makeText(context, "잘못된 바코드값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                    validity = false;
                    return validity;
                }
            }
        }

        else if(format.equals("CODE_93")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length > 80) {
                Toast.makeText(context, "요청된 바코드 값은 80자리 이하여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_LONG).show();
                validity = false;
                return validity;
            }
            for(int result = 0; result < length; ++result) {
                if (("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(value.charAt(result))) < 0) {
                    Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    validity = false;
                    return validity;
                }
            }
        }

        else if(format.equals("CODE_128")){
            final char ESCAPE_FNC_1 = '\u00f1';
            final char ESCAPE_FNC_2 = '\u00f2';
            final char ESCAPE_FNC_3 = '\u00f3';
            final char ESCAPE_FNC_4 = '\u00f4';

            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length < 1 || length > 80) {
                Toast.makeText(context, "요청된 바코드 값은 1~80자리 사이 여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "잘못된 바코드값이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                            validity = false;
                            return validity;
                    }
                }
            }
        }

        else if(format.equals("EAN_8")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if (length != 8) {
                Toast.makeText(context, "요청된 바코드 값은 8자리 이하여야 합니다. 현재 자리수 :  " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }

        else if(format.equals("EAN_13")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if (length != 13) {
                Toast.makeText(context, "요청된 바코드 값은 13자리 이하여야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }

        else if(format.equals("PDF_417")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

        }

        else if(format.equals("UPC_A")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length != 12) {
                Toast.makeText(context, "요청된 바코드 값은 11자리 또는 12자리가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }

        else if(format.equals("UPC_E")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length != 8) {
                Toast.makeText(context, "요청된 바코드 값은 8자리가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

        }

        else if(format.equals("CODABAR")) {
            final char[] START_END_CHARS = new char[]{'A', 'B', 'C', 'D'};
            final char[] ALT_START_END_CHARS = new char[]{'T', 'N', '*', 'E'};

            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length >= 2) {
                char resultLength = Character.toUpperCase(value.charAt(0));
                char result = Character.toUpperCase(value.charAt(value.length() - 1));
                boolean position = arrayContains(START_END_CHARS, resultLength);
                boolean index = arrayContains(START_END_CHARS, result);
                boolean c = arrayContains(ALT_START_END_CHARS, resultLength);
                boolean code = arrayContains(ALT_START_END_CHARS, result);
                if (position) {
                    if (!index) {
                        Toast.makeText(context, "잘못된 바코드 값입니다. " + value, Toast.LENGTH_SHORT).show();
                        validity = false;
                        return validity;
                    }
                } else if (c) {
                    if (!code) {
                        Toast.makeText(context, "잘못된 바코드 값입니다." + value, Toast.LENGTH_SHORT).show();
                        validity = false;
                        return validity;
                    }
                } else {
                    if (index || code) {
                        Toast.makeText(context, "잘못된 바코드 값입니다. " + value, Toast.LENGTH_SHORT).show();
                        validity = false;
                        return validity;
                    }
                }
            }

        }

        else if(format.equals("ITF")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

            if(length % 2 != 0) {
                Toast.makeText(context, "요청된 바코드 값은 짝수가 되어야 합니다.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
            else if(length > 80) {
                Toast.makeText(context, "요청된 바코드 값의 자리수는 80자리 이하가 되어야 합니다. 현재 자리수 : " + length, Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
            else
                return true;
        }

        else if(format.equals("QR_CODE")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }

        else if(format.equals("DATA_MATRIX")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }
        }

        else if(format.equals("AZTEC")){
            if(value.isEmpty()){
                Toast.makeText(context, "바코드 값을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                validity = false;
                return validity;
            }

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
