package com.riskwizard.incidentlogger.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.riskwizard.incidentlogger.R;
import com.riskwizard.incidentlogger.dao.ContactDAO;
import com.riskwizard.incidentlogger.dao.IncidentDAO;
import com.riskwizard.incidentlogger.dao.IncidentTypeDAO;
import com.riskwizard.incidentlogger.helper.DateTimeHelper;
import com.riskwizard.incidentlogger.helper.ValidationHelper;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.Incident;
import com.riskwizard.incidentlogger.model.IncidentType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncidentForm extends ActionBarActivity {

    private Incident anIncident;
    private long incidentID = -1;

    // DAO
    private IncidentDAO incident_dao;
    private IncidentTypeDAO incidentType_dao;
    private ContactDAO contact_dao;

    private Spinner incidentTypeSpinner;
    private EditText notifyField;
    private EditText dateField;
    private EditText timeField;
    private EditText descriptionField;
    private EditText initialActionTakenField;
    private CheckBox reportableCheckbox;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private AlertDialog.Builder notifyContactsDialog;

    private int hour;
    private int minute;

    private ArrayList<Integer> selectedItemsIndexList = new ArrayList<>();
    private List<String> selectedNotifyContactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_form);

        incidentTypeSpinner = (Spinner) findViewById(R.id.spIncidentType);
        notifyField = (EditText) findViewById(R.id.txtNotify);
        dateField = (EditText) findViewById(R.id.txtDate);
        timeField = (EditText) findViewById(R.id.txtTime);
        descriptionField = (EditText) findViewById(R.id.txtDescription);
        initialActionTakenField = (EditText) findViewById(R.id.txtInitialActionTaken);
        reportableCheckbox = (CheckBox) findViewById(R.id.cbReportable);

        // Create DAO object
        incident_dao = new IncidentDAO(this);
        incidentType_dao = new IncidentTypeDAO(this);
        contact_dao = new ContactDAO(this);

        //If ID on the Incident object is null or -1 we are creating a new Incident else editing
        Intent intent = getIntent();
        incidentID = intent.getLongExtra("incident_id", -1);

        if (incidentID != -1) {
            anIncident = incident_dao.getIncident(incidentID);
        }
        else {
            anIncident = new Incident();
        }

        setupViews();

        if (incidentID != -1)
            loadFormData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_incident_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_attachment) {
            if (incidentID == -1)
                Toast.makeText(IncidentForm.this, "Please save the incident before adding attachments.", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(this, AttachmentActivity.class);
                i.putExtra("incident_id", new Long(anIncident.getId()));
                //startActivity(i);
                startActivityForResult(i, 0);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {

        Button button1 = (Button) findViewById(R.id.saveButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation())
                    saveIncident();
                else
                    Toast.makeText(IncidentForm.this, "Form contains error", Toast.LENGTH_LONG).show();
            }
        });

        loadSpinnerData();

        /*
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        */

        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && v == dateField)
                    datePickerDialog.show();
                v.clearFocus();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateField.setText(DateTimeHelper.toLocaleDate(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && v == timeField)
                    timePickerDialog.show();
                v.clearFocus();
            }
        });

        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;

                Date aDate = new Date();
                aDate.setMinutes(selectedMinute);
                aDate.setHours(selectedHour);
                timeField.setText(DateTimeHelper.toLocaleTime(aDate));

                // set current time into timepicker
                //timePicker1.setCurrentHour(hour);
                //timePicker1.setCurrentMinute(minute);

            }
        }, hour, minute, false);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time of Incident");

        notifyField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && v == notifyField)
                    notifyContactsDialog.show();
                v.clearFocus();
            }
        });

        //TODO - source http://myandroidtipsandtricks.blogspot.com.au/2011/10/using-multiselect-list-with-dialog.html

        List<Contact> allContacts = contact_dao.getAllContacts();
        final String[] items = new String[allContacts.size()];
        boolean checkedItems[] = new boolean[allContacts.size()];
        int x = 0;
        String tmp = "";

        for(Contact c : anIncident.getNotifyContacts())
            tmp += c.toString();

        for(Contact c : allContacts) {
            items[x] = c.toString();

            if (anIncident != null) {
                    if (tmp.contains(c.toString()))
                        checkedItems[x] = true;
                    else
                        checkedItems[x] = false;
            }

            x++;
        }

        notifyContactsDialog = new AlertDialog.Builder(IncidentForm.this);
        notifyContactsDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                String FullName = String.valueOf(items[which]);
                String FirstName = (FullName.split(" "))[0];
                String LastName = (FullName.split(" "))[1];

                Contact aContact = contact_dao.getContactByName(FirstName, LastName);

                if (isChecked) {
                    selectedItemsIndexList.add(which);
                    //selectedNotifyContactsList.add(items[which]);

                    if (anIncident.getNotifyContacts().size() == 0) {
                        List<Contact> notifyList = new ArrayList<>();
                        notifyList.add(aContact);
                        anIncident.setNotifyContacts(notifyList);
                    }
                    else
                        anIncident.getNotifyContacts().add(aContact);

                } else if (selectedItemsIndexList.contains(which)) {
                    selectedItemsIndexList.remove(Integer.valueOf(which));
                    //selectedNotifyContactsList.remove(items[which]);
                    anIncident.getNotifyContacts().remove(aContact);
                }
            }
        });
        notifyContactsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String tmp = "";

                for(Contact c : anIncident.getNotifyContacts()) {
                    tmp = tmp + c + ", ";
                }

                //remove last ", " from string
                if (tmp.length() > 0)
                    tmp = tmp.substring(0, tmp.length() - 2);

                notifyField.setText(tmp);
            }
        });
        notifyContactsDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        notifyContactsDialog.setTitle("Please select contacts to notify");
        //notifyContactsDialog.setAdapter()
    }

    private void loadSpinnerData() {

        Spinner spinner = (Spinner) findViewById(R.id.spIncidentType);

        // Spinner Drop down elements
        List<IncidentType> incident_types = incidentType_dao.getAllIncidentTypes();

        // Creating adapter for spinner
        ArrayAdapter<IncidentType> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, incident_types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void loadFormData() {

        List<IncidentType> incident_types = incidentType_dao.getAllIncidentTypes();
        ArrayAdapter<IncidentType> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, incident_types);

        for(int i=0; i < dataAdapter.getCount(); i++) {
            if(anIncident.getIncidentType().getId() == (dataAdapter.getItem(i).getId())){
                incidentTypeSpinner.setSelection(i);
                break;
            }
        }

        if (anIncident.getNotifyContacts().size() > 0) {
            String tmp = "";

            for(Contact c : anIncident.getNotifyContacts()) {
                tmp = tmp + c.toString() + ", ";
            }

            //remove last ", " from string
            tmp = tmp.substring(0, tmp.length() - 2);

            notifyField.setText(tmp);
        }

        dateField.setText(DateTimeHelper.toLocaleDate(anIncident.getIncidentDate()));
        timeField.setText(DateTimeHelper.toLocaleTime(anIncident.getIncidentDate()));

        descriptionField.setText(anIncident.getDescription());
        initialActionTakenField.setText(anIncident.getInitialAction());
        reportableCheckbox.setChecked(anIncident.getReportable());

    }

    private void saveIncident() {

        IncidentType anIncidentType = incidentType_dao.getIncidentType(incidentTypeSpinner.getSelectedItem().toString());

        anIncident.setIncidentType(anIncidentType);
        anIncident.setCreatedBy(contact_dao.getContact(1)); //1: Admin User

        anIncident.setIncidentDate(DateTimeHelper.parseDateAndTime(String.valueOf(dateField.getText()), String.valueOf(timeField.getText())));

        anIncident.setDescription(descriptionField.getText().toString());
        anIncident.setInitialAction(initialActionTakenField.getText().toString());
        anIncident.setReportable(reportableCheckbox.isChecked());

        if (incidentID != -1) {
            //Updating an Incident in the db
            incident_dao.updateIncident(anIncident);
            Toast.makeText(IncidentForm.this, "Incident updated.", Toast.LENGTH_SHORT).show();
        }
        else {
            // Inserting Incidents in db
            Toast.makeText(IncidentForm.this, "Incident created.", Toast.LENGTH_SHORT).show();
            incidentID = incident_dao.createIncident(anIncident);
        }

        //these should be in onDestroy()
        incident_dao.close();
        incidentType_dao.close();
        contact_dao.close();

    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!ValidationHelper.hasText(dateField)) ret = false;
        if (!ValidationHelper.hasText(timeField)) ret = false;
        //if (!ValidationHelper.hasText(etNormalText)) ret = false;
        //if (!ValidationHelper.isEmailAddress(etEmailAddrss, true)) ret = false;
        //if (!ValidationHelper.isPhoneNumber(etPhoneNumber, false)) ret = false;

        return ret;
    }

}

