package com.example.inzynierka.Report;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    Activity activity;
    final String FILE_PATH;
    final String IMAGE_DIRECTORY = "Fotki";
    List<PhotoEntity> imagesToCopy;
    public FileManager(Activity activity) {
        this.activity = activity;
        imagesToCopy = new ArrayList<>();
        FILE_PATH = activity.getExternalFilesDir(null)+File.separator + IMAGE_DIRECTORY + File.separator;
    }

    public void setImagesToCopy(List<PhotoEntity> imagesToCopy) {
        this.imagesToCopy = imagesToCopy;
    }

    public void saveImage() {
        if (imagesToCopy == null){
            return;
        }
        File file = new File(FILE_PATH);
        Log.e("Image directory ", file.toString());
        if(!file.exists())
            file.mkdirs();

        int cut = 0;
        String result = null;
        int i=0;

        for (PhotoEntity photoEntity : imagesToCopy) {
            Uri uri = photoEntity.getPhotoUri();
            result = uri.getPath();
            cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
            try {
                Log.e("Photo uri", uri.toString());
                File copiedFile = copyFile(new File(getRealPathFromURI(uri)), file, File.separator + result);
                Log.e("Copied file ", copiedFile.toString());
                imagesToCopy.get(i).setPhotoUri(Uri.fromFile(copiedFile));
                Log.e("W photo enetites mam ", imagesToCopy.get(i).getPhotoUri()+"");
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File copyFile(File sourceFile, File destFile, String result) throws IOException {
        File outputFile = new File(destFile, result);
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(outputFile, false);
        Log.e("Source file ", sourceFile+"");
        Log.e("Zapisuje do:", outputFile+"");
        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;

        return outputFile;

    }
    public String getRealPathFromURI (Uri contentUri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, projection, null, null, null);
        if(cursor==null){
            return contentUri.toString();
        }
        activity.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
