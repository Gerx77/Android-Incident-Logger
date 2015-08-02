package com.riskwizard.incidentlogger.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riskwizard.incidentlogger.R;

import com.riskwizard.incidentlogger.helper.DateTimeHelper;
import com.riskwizard.incidentlogger.helper.PhotoHelper;
import com.riskwizard.incidentlogger.model.Attachment;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by mattg on 19/03/2015.
 */
public class AttachmentsAdapter extends ArrayAdapter<Attachment> {

    public AttachmentsAdapter(Context context, ArrayList<Attachment> attachments) {
        super(context, 0, attachments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Attachment attachment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_attachment, parent, false);
        }
        // Lookup view for data population
        final ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.attachmentThumbImageView);
        TextView tvFirstLine = (TextView) convertView.findViewById(R.id.firstLine);
        TextView tvSecondline = (TextView) convertView.findViewById(R.id.secondLine);

        File imgFile = new  File(attachment.getPhysicalFilename());

        ivThumbnail.setImageBitmap(getPreview(imgFile));

        // Populate the data into the template view using the data object
        tvFirstLine.setText(attachment.toString());
        tvSecondline.setText(String.format("ID: %s, Size: %s KB, Attached Date: %s", attachment.getId(), attachment.getSize(), DateTimeHelper.toLocaleDate(attachment.getAttachedDate())));

        // Return the completed view to render on screen
        return convertView;
    }

    //This is a pretty crude way of getting a thumbnail but will do for now.
    Bitmap getPreview(File image) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;

        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 100; //THUMBNAIL_SIZE;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }
}
