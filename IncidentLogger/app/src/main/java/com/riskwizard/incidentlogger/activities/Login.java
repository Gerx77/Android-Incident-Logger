package com.riskwizard.incidentlogger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.riskwizard.incidentlogger.R;

public class Login extends ActionBarActivity {

    private String txt_UserName;
    private String txt_Password;
    private boolean cb_RememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        SharedPreferences _SharedPref = getApplicationContext().getSharedPreferences("MyAppData", 0);
        final SharedPreferences.Editor _SharedPrefEditor = _SharedPref.edit();
        int UserKey = _SharedPref.getInt("UserKey", -1);
        txt_UserName = _SharedPref.getString("UserName", "");
        txt_Password = _SharedPref.getString("Password", "");
        cb_RememberMe = _SharedPref.getBoolean("RememberMe", false);

        ((CheckBox) findViewById(R.id.cbRememberMe)).setChecked(false);

        if (_SharedPref.getBoolean("RememberMe", false)) {
            ((EditText) findViewById(R.id.UserName)).setText(txt_UserName);
            ((EditText) findViewById(R.id.Password)).setText(txt_Password);
            ((CheckBox) findViewById(R.id.cbRememberMe)).setChecked(cb_RememberMe);
        }

        Button btn_Login = (Button) findViewById(R.id.btn_Login);

        btn_Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                txt_UserName = ((EditText)findViewById(R.id.UserName)).getText().toString();
                txt_Password = ((EditText)findViewById(R.id.Password)).getText().toString();

                int UserID = 1; //IsLogin(txt_UserName, txt_Password);

                //TODO This validation should really be in the ValidationHelper.java
                if (txt_UserName.isEmpty())
                {
                    ((EditText)findViewById(R.id.UserName)).setError("User name is a required field!");
                }
                else if (txt_Password.isEmpty())
                {
                    ((EditText) findViewById(R.id.Password)).setError("Password is a required field!");
                }
                else if (txt_UserName.equals("admin") && txt_Password.equals("password")) {
                    _SharedPrefEditor.putInt("UserKey", UserID);
                    _SharedPrefEditor.putString("UserName", txt_UserName);
                    _SharedPrefEditor.putString("Password", txt_Password);
                    _SharedPrefEditor.putBoolean("RememberMe", ((CheckBox)findViewById(R.id.cbRememberMe)).isChecked());
                    _SharedPrefEditor.commit();

                    Intent i = new Intent(Login.this, IncidentRegistry.class);
                    startActivity(i);
                }
                else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        });

    }

    private int IsLogin(String user_name, String password) {
        if (user_name.toString() == "admin" && password.toString() == "password")
            return 1;
        else
            return -1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
