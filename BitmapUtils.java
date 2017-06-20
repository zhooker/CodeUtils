package example.com.daggerdemo.reflect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Bitmap操作相关工具类
 */
public class BitmapUtils {

    /**
     * 计算图片的压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 转化 Bitmap 成 byte[] 数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] convertBitmap2Byte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * 获取图片的宽高
     *
     * @param aPath
     * @return
     */
    public static int[] getBitmapWH(String aPath) {
        int[] WH = new int[2];
        try {
            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            File check = new File(aPath);
            if (check != null && check.exists()) {
                BitmapFactory.decodeFile(aPath, options);
                WH[0] = options.outWidth;
                WH[1] = options.outHeight;
                return WH;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 旋转一张图片
     *
     * @param bitmap
     * @param rotationAngleDegree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotationAngleDegree) {

        if (rotationAngleDegree % 360 == 0)
            return bitmap;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int newW = w, newH = h;
        if (rotationAngleDegree == 90 || rotationAngleDegree == 270) {
            newW = h;
            newH = w;
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(newW, newH, bitmap.getConfig());
        Canvas canvas = new Canvas(rotatedBitmap);

        Rect rect = new Rect(0, 0, newW, newH);
        Matrix matrix = new Matrix();
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(rotationAngleDegree);
        matrix.postTranslate(px, py);
        canvas.drawBitmap(bitmap, matrix, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();

        return rotatedBitmap;
    }

    /**
     * 从文件中按比例加载图片
     *
     * @param aPath
     * @param aInSampleSize
     * @return
     */
    public static Bitmap loadBitmap(String aPath, int aInSampleSize) {
        try {
            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = aInSampleSize;
            Bitmap bThumbImage = BitmapFactory.decodeFile(aPath, options);
            return bThumbImage;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
