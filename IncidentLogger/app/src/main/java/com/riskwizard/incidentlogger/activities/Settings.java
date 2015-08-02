package com.riskwizard.incidentlogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.riskwizard.incidentlogger.R;
import com.riskwizard.incidentlogger.dao.ContactDAO;
import com.riskwizard.incidentlogger.dao.IncidentDAO;
import com.riskwizard.incidentlogger.dao.IncidentTypeDAO;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;

public class Settings extends ActionBarActivity {

    private DatabaseHelper db;
    private IncidentTypeDAO incidentType_dao;
    private IncidentDAO incident_dao;
    private ContactDAO contact_dao;

    private String[] mDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTitle = "test";

        mDrawerItemTitles = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItemTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                //getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mTitle);
                //getSupportActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_rw);

        db = DatabaseHelper.getInstance(this);
        incidentType_dao = new IncidentTypeDAO(this);
        incident_dao = new IncidentDAO(this);
        contact_dao = new ContactDAO(this);

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                //start settings activity
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void setupViews() {

        Button button1 = (Button) findViewById(R.id.provideInfoButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Settings.this, "Provide Info not implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        Button button4 = (Button) findViewById(R.id.seedDatabaseButton);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Settings.this)
                        .setTitle("Seed Database")
                        .setMessage("Existing data may be lost, continue?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Toast.makeText(Settings.this, "Now seeding database...", Toast.LENGTH_SHORT).show();
                                db.SeedDatabase();

                                String MSG = String.format("Records added - Contact: %d, Incident Types: %d, Incidents: %d", contact_dao.getContactCount(), incidentType_dao.getIncidentTypeCount(), incident_dao.getIncidentCount());
                                Toast.makeText(Settings.this, MSG, Toast.LENGTH_LONG).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    private void selectDrawerItem(int position) {
        //Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mDrawerItemTitles[position]);
        //mDrawerList.setSelection(position);

        switch (mDrawerItemTitles[position]) {
            case "Create Incident":
                Intent i = new Intent(this, IncidentForm.class);
                //i.putExtra("incident_id", new Long("63"));
                startActivity(i);
                break;
            case "Incident Registry":
                startActivity(new Intent(this, IncidentRegistry.class));
                break;
            case "Settings":
                startActivity(new Intent(this, Settings.class));
                break;
            case "Help":
                startActivity(new Intent(this, Help.class));
                break;
            case "About":
                startActivity(new Intent(this, About.class));
                break;
            case "Contact Us":
                startActivity(new Intent(this, com.riskwizard.incidentlogger.activities.Contact.class));
                break;
            case "Logout":
                logout();
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void logout() {

        SharedPreferences _SharedPref = getApplicationContext().getSharedPreferences("MyAppData", 0);
        SharedPreferences.Editor _SharedPrefEditor = _SharedPref.edit();

        _SharedPrefEditor.remove("UserKey");
        _SharedPrefEditor.remove("UserName");
        _SharedPrefEditor.remove("Password");
        _SharedPrefEditor.remove("RememberMe");
        //_SharedPrefEditor.clear(); //This will clear all data inside MyAppData Shared Preference File
        _SharedPrefEditor.commit();

        startActivity(new Intent(this, Login.class));
    }
}
