package example.com.daggerdemo.reflect;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作相关工具类
 */
public class FileUtils {

    /**
     * 从文件中读取byte[]数据
     *
     * @param filename
     * @return
     */
    public static byte[] readFile(String filename) {
        InputStream in = null;
        ByteArrayOutputStream bout = null;
        byte[] content = null;
        byte[] buf = new byte[1024];
        try {
            bout = new ByteArrayOutputStream();
            int length = 0;
            // in = getAssets().open(filename); // 获得输入流
            in = new FileInputStream(new File(filename));
            while ((length = in.read(buf)) != -1) {
                bout.write(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            content = bout.toByteArray();
            try {
                if (in != null)
                    in.close();
                if (bout != null)
                    bout.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 保存byte[]数组到文件中
     *
     * @param imageArray
     * @param filepath
     * @param filename
     */
    public static void saveImageArray(byte[] imageArray, final String filepath, final String filename) {
        File destDir = null;
        try {
            destDir = new File(filepath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } catch (Exception e1) {
            destDir = null;
            e1.printStackTrace();
        }

        if (destDir != null) {
            FileOutputStream outStream = null;
            try {
                File saveFile = new File(destDir.getAbsolutePath(), filename);
                outStream = new FileOutputStream(saveFile);
                outStream.write(imageArray);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outStream != null)
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    /**
     * 保存Bitmap到文件中
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String path) {

        if (bitmap == null || TextUtils.isEmpty(path))
            return false;

        File f = new File(path);
        FileOutputStream fOut = null;
        boolean result = true;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (!result && fOut != null)
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        if (result) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            } finally {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取 Uri 的真实路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();

            }
        }
        return data;
    }

    /**
     * 删除文件/文件夹
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return file.delete();
            }

            for (File f : childFile) {
                deleteFile(f);
            }

            return file.delete();
        } else {
            return file.delete();
        }
    }
}
