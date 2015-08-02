package com.riskwizard.incidentlogger.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.riskwizard.incidentlogger.Constants;
import com.riskwizard.incidentlogger.R;
import com.riskwizard.incidentlogger.adapters.AttachmentsAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mattg on 18/03/2015.
 */
public class PhotoHelper {

    public final static String LOG_TAG = "PHOTOHELPER_EVENT";

    public static Uri generateTimeStampPhotoFileUri() {
        Uri photoFileUri = null;

        File outputDir = getPhotoDirectory();

        if (outputDir != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
            String photoFileName = "IMG_" + timeStamp + ".jpg";

            File photoFile = new File(outputDir, photoFileName);
            photoFileUri = Uri.fromFile(photoFile);
        }

        return photoFileUri;
    }

    private static File getPhotoDirectory() {
        File outputDir = null;

        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            //File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            outputDir = new File(pictureDir, Constants.IMAGE_DIR_NAME);
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                   Log.e(LOG_TAG, "Failed to create directory: " + outputDir.getAbsolutePath());
                   outputDir = null;
                }
            }
        }

        /*
        try {
        catch (IOException e) {
        // Error while creating file

         */
        return outputDir;
    }

    public static void addPhotoToMediaStoreAndDisplayThumbnail(String pathName, Activity activity, ImageView imageView) {
        final ImageView thumbnailImageView = imageView;
        final Activity thumbnailActivity = activity;

        String [] filesToScan = {pathName};

        MediaScannerConnection.scanFile(thumbnailActivity, filesToScan, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        long id = ContentUris.parseId(uri);
                        ContentResolver contentResolver = thumbnailActivity.getContentResolver();

                        final Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);

                        thumbnailActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //thumbnailImageView.setImageBitmap(thumbnail);
                            }
                        });
                    }
                });

    }

}
