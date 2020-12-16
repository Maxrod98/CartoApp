package com.ecarto.cartoapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public static void createFolderIfNecessary(String path) {
        File folder = new File(path);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

    }

    public static void copyFile(String sourcepath, String targetpath) {
        File sourceLocation = new File(sourcepath);
        File targetLocation = new File(targetpath);

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceLocation);
            out = new FileOutputStream(targetLocation);
            // Copy the bits from instream to outstream

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void copyFileFromUri(Activity activity, Uri source_uri, String destination_path) {
        try (InputStream ins = activity.getContentResolver().openInputStream(source_uri)) {
            File dest = new File(destination_path);
            copyFileFromStream(ins, dest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void copyFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteFolderContent(String path){
        //delete contents of pdf folder
        File dir = new File(path);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
        createFolderIfNecessary(path);
    }

    public static void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }

    public static String getFileDetailFromUri(final Context context, final Uri uri) {
        String fileName = null;
        if (uri != null) {
            // File Scheme.
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                fileName = file.getName();
            }
            // Content Scheme.
            else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null && returnCursor.moveToFirst()) {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    fileName = returnCursor.getString(nameIndex);
                    returnCursor.close();
                }
            }
        }
        return fileName;
    }

}