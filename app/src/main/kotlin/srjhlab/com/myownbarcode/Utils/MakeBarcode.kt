package srjhlab.com.myownbarcode.Utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.aztec.AztecWriter
import com.google.zxing.datamatrix.DataMatrixWriter
import com.google.zxing.oned.*
import com.google.zxing.pdf417.PDF417Writer
import com.google.zxing.qrcode.QRCodeWriter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import java.util.*

class MakeBarcode {
    val TAG = this.javaClass.simpleName

    fun makeBarcode(type: Int, value: String): Bitmap? {
        var bmp: Bitmap? = null

        when (type) {
            ConstVariables.CODE_39 -> {
                Log.d(TAG, "##### makeBarcode ##### CODE_39")
                bmp = makeCODE39(value)
            }
            ConstVariables.CODE_93 -> {
                Log.d(TAG, "##### makeBarcode ##### CODE_93")
                bmp = makeCODE93(value)
            }
            ConstVariables.CODE_128 -> {
                Log.d(TAG, "##### makeBarcode ##### CODE_128")
                bmp = makeCODE128(value)
            }
            ConstVariables.EAN_8 -> {
                Log.d(TAG, "##### makeBarcode ##### EAN_8")
                bmp = makeEAN8(value)
            }
            ConstVariables.EAN_13 -> {
                Log.d(TAG, "##### makeBarcode ##### EAN_13")
                bmp = makeEAN13(value)
            }
            ConstVariables.PDF_417 -> {
                Log.d(TAG, "##### makeBarcode ##### PDF_417")
                bmp = makePDF417(value)
            }
            ConstVariables.UPC_A -> {
                Log.d(TAG, "##### makeBarcode ##### UPC_A")
                bmp = makeUPCA(value)
            }
            ConstVariables.CODABAR -> {
                Log.d(TAG, "##### makeBarcode ##### CODABAR")
                bmp = makeCODABAR(value)
            }
            ConstVariables.ITF -> {
                Log.d(TAG, "##### makeBarcode ##### ITF")
                bmp = makeITF(value)
            }
            ConstVariables.QR_CODE -> {
                Log.d(TAG, "##### makeBarcode ##### QR_CODE")
                bmp = makeQRCODE(value)
            }
            ConstVariables.MAXI_CODE -> {
                Log.d(TAG, "##### makeBarcode ##### MAXI_CODE")
                bmp = makeMAXICODE(value)
            }
            ConstVariables.AZTEC -> {
                Log.d(TAG, "##### makeBarcode ##### AZTEC")
                bmp = makeAZTEC(value)
            }
        }

        return bmp
    }

    fun initBarcodeList(item: MutableList<BarcodeItem>, test : (Int) -> Unit): MutableList<BarcodeItem> {

        for (i in item.indices) {
            if (item[i].barcodeBitmapArr == null && item[i].itemType === ConstVariables.ITEM_TYPE_BARCODE) {
                when (item[i].barcodeType?.toInt()) {
                    ConstVariables.CODE_39 -> {
                        Log.d(TAG, "##### initBarcodeList ##### CODE_39")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeCODE39(item[i].barcodeValue)))
                    }
                    ConstVariables.CODE_93 -> {
                        Log.d(TAG, "##### initBarcodeList ##### CODE_93")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeCODE93(item[i].barcodeValue)))
                    }
                    ConstVariables.CODE_128 -> {
                        Log.d(TAG, "##### initBarcodeList ##### CODE_128")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeCODE128(item[i].barcodeValue)))
                    }
                    ConstVariables.EAN_8 -> {
                        Log.d(TAG, "##### initBarcodeList ##### EAN_8")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeEAN8(item[i].barcodeValue)))
                    }
                    ConstVariables.EAN_13 -> {
                        Log.d(TAG, "##### initBarcodeList ##### EAN_13")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeEAN13(item[i].barcodeValue)))
                    }
                    ConstVariables.PDF_417 -> {
                        Log.d(TAG, "##### initBarcodeList ##### PDF_417")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makePDF417(item[i].barcodeValue)))
                    }
                    ConstVariables.UPC_A -> {
                        Log.d(TAG, "##### initBarcodeList ##### UPC_A")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeUPCA(item[i].barcodeValue)))
                    }
                    ConstVariables.CODABAR -> {
                        Log.d(TAG, "##### initBarcodeList ##### CODABAR")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeCODABAR(item[i].barcodeValue)))
                    }
                    ConstVariables.ITF -> {
                        Log.d(TAG, "##### initBarcodeList ##### ITF")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeITF(item[i].barcodeValue)))
                    }
                    ConstVariables.QR_CODE -> {
                        Log.d(TAG, "##### initBarcodeList ##### QR_CODE")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeQRCODE(item[i].barcodeValue)))
                    }
                    ConstVariables.MAXI_CODE -> {
                        Log.d(TAG, "##### initBarcodeList ##### MAXI_CODE")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeMAXICODE(item[i].barcodeValue)))
                    }
                    ConstVariables.AZTEC -> {
                        Log.d(TAG, "##### initBarcodeList ##### AZTEC")
                        item[i].barcodeBitmapArr = BitmapByteConverter().bitmapToByte(Objects.requireNonNull<Bitmap>(makeAZTEC(item[i].barcodeValue)))
                    }
                }
                test(i)
            }
        }

        return item
    }

    private fun makeCODE39(value: String): Bitmap? {
        Log.d(TAG, "##### makeCODE39 #####")
        var bmp: Bitmap? = null
        val c9 = Code39Writer()
        bmp = Bitmap.createBitmap(1800, 4500, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.CODE_39, 1800, 450)
            bmp = Bitmap.createBitmap(1800, 450, Bitmap.Config.ARGB_8888)
            for (i in 0..1799) {
                for (j in 0..449) {
                    bmp.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeCODE93(value: String): Bitmap? {
        Log.d(TAG, "##### makeCODE93 #####")
        var bmp: Bitmap? = null

        val c9 = Code93Writer()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.CODE_93, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeCODE128(value: String): Bitmap? {
        Log.d(TAG, "##### makeCODE128 #####")
        var bmp: Bitmap? = null

        val c9 = Code128Writer()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.CODE_128, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeEAN8(value: String): Bitmap? {
        Log.d(TAG, "##### makeEAN8 #####")
        var bmp: Bitmap? = null

        val c9 = EAN8Writer()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.EAN_8, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeEAN13(value: String): Bitmap? {
        Log.d(TAG, "##### makeEAN13 #####")
        var bmp: Bitmap? = null

        val c9 = EAN13Writer()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.EAN_13, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makePDF417(value: String): Bitmap? {
        Log.d(TAG, "##### maekPDF417 #####")
        var bmp: Bitmap? = null

        val c9 = PDF417Writer()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.PDF_417, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeUPCA(value: String): Bitmap? {
        Log.d(TAG, "##### makeUPCA #####")
        var bmp: Bitmap? = null

        val c9 = UPCAWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.UPC_A, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeUPCE(value: String): Bitmap? {
        Log.d(TAG, "##### makeUPCE #####")
        var bmp: Bitmap? = null

        val c9 = UPCEWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.UPC_E, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeCODABAR(value: String): Bitmap? {
        Log.d(TAG, "##### makeCODABAR #####")
        var bmp: Bitmap? = null

        val c9 = CodaBarWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.CODABAR, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeITF(value: String): Bitmap? {
        Log.d(TAG, "##### makeITF #####")
        var bmp: Bitmap? = null

        val c9 = ITFWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.ITF, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeQRCODE(value: String): Bitmap? {
        Log.d(TAG, "##### makeQRCODE #####")
        var bmp: Bitmap? = null

        val c9 = QRCodeWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.QR_CODE, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeMAXICODE(value: String): Bitmap? {
        Log.d(TAG, "##### makeMAXICODE #####")
        var bmp: Bitmap? = null

        val c9 = DataMatrixWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.DATA_MATRIX, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }

    private fun makeAZTEC(value: String): Bitmap? {
        Log.d(TAG, "##### makeAZTEC #####")
        var bmp: Bitmap? = null

        val c9 = AztecWriter()
        bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)
        try {
            val bm = c9.encode(value, BarcodeFormat.AZTEC, 800, 200)
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888)

            for (i in 0..799) {
                for (j in 0..199) {
                    bmp?.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bmp
    }
}