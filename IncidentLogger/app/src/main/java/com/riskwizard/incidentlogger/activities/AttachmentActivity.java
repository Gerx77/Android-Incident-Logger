package com.riskwizard.incidentlogger.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.riskwizard.incidentlogger.R;
import com.riskwizard.incidentlogger.adapters.AttachmentsAdapter;
import com.riskwizard.incidentlogger.dao.AttachmentDAO;
import com.riskwizard.incidentlogger.dao.ContactDAO;
import com.riskwizard.incidentlogger.dao.IncidentDAO;
import com.riskwizard.incidentlogger.helper.PhotoHelper;
import com.riskwizard.incidentlogger.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Attachments could go in a fragment with a swipe right from editing an incident

public class AttachmentActivity extends ActionBarActivity {

    static final int TAKE_PICTURE_REQUEST_CODE = 1010;

    private Incident anIncident;
    private long incidentID = -1;

    // DAO
    private AttachmentDAO attachment_dao;
    private IncidentDAO incident_dao;
    private ContactDAO contact_dao;

    private Button takePictureButton;
    ImageView mThumbNailImageView;
    ListView listView;

    Uri mPhotoPathUri;
    String mPhotoPathName;

    AttachmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);

        Intent intent = getIntent();
        incidentID = intent.getLongExtra("incident_id", -1);

        // Create DAO object
        incident_dao = new IncidentDAO(this);
        attachment_dao = new AttachmentDAO(this);
        contact_dao = new ContactDAO(this);

        anIncident = incident_dao.getIncident(incidentID);

        //TextView debugTV = (TextView) findViewById(R.id.debugTVAttachmentForm);
        //debugTV.setText("IncidentID=" + Long.toString(incidentID));

        takePictureButton = (Button) findViewById(R.id.btTakePic);
        //mThumbNailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        listView = (ListView) findViewById(R.id.attachmentsListView);

        setupViews();

        // Construct the data source
        ArrayList<Attachment> attachmentList = (ArrayList)anIncident.getAttachments();

        // Create the adapter to convert the array to views
        adapter = new AttachmentsAdapter(this, attachmentList);

        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the attachment, positioned to the corresponding row in the result set
                Attachment attachment = (Attachment) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), String.format("Selected pos: %d", position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attachment, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.home:
                Intent i = new Intent(this, IncidentForm.class);
                i.putExtra("incident_id", incidentID);
                startActivity(i);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {

        Button button1 = (Button) findViewById(R.id.btBack);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AttachmentActivity.this, IncidentForm.class);
                i.putExtra("incident_id", new Long(incidentID));
                startActivity(i);
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTakePictureButton((Button) view);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        switch (requestCode) {
            case TAKE_PICTURE_REQUEST_CODE:
                handleTakePictureResult(resultCode, resultIntent);
                break;
        }
    }

    private void handleTakePictureResult(int resultCode, Intent resultIntent) {
        if (resultCode == RESULT_OK) {
            String photoPathName = mPhotoPathUri.getPath();
            PhotoHelper.addPhotoToMediaStoreAndDisplayThumbnail(photoPathName, this, mThumbNailImageView);

            File aPhoto= new File(photoPathName);

            Attachment a1 = new Attachment();
            a1.setFilename(aPhoto.getName());
            a1.setPhysicalFilename(aPhoto.getPath());
            a1.setSize(String.valueOf(aPhoto.length()/1024));
            a1.setDescription("This is a dummy attachment.");
            a1.setAttachedBy(contact_dao.getContact(1));
            a1.setAttachedDate(new Date());


            long attachment1_id = attachment_dao.createAttachment(a1);
            Attachment anAttachment = attachment_dao.getAttachment((int)attachment1_id);
            Incident anIncident = incident_dao.getIncident(incidentID);

            incident_dao.addAttachmentToIncident(anAttachment, anIncident);

            adapter.add(anAttachment);
            adapter.notifyDataSetChanged();
        }
        else {
            mPhotoPathUri = null;
            Toast.makeText(AttachmentActivity.this, "User canceled", Toast.LENGTH_LONG).show();
        }
    }

    //todo make this approach to button clicks standard throughout app
    private void handleTakePictureButton(Button button) {

        mPhotoPathUri = PhotoHelper.generateTimeStampPhotoFileUri();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoPathUri);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);

    }

}
