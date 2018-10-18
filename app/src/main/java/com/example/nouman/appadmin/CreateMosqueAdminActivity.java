package com.example.nouman.appadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CreateMosqueAdminActivity extends AppCompatActivity {

    EditText email,pasword,mosqueid,adminName;
    Button createMosqueAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mosque_admin);
        email=(EditText) findViewById(R.id.email_editText);
        pasword=(EditText) findViewById(R.id.password_editText);
        mosqueid=(EditText)findViewById(R.id.mosqueID_editText);
        adminName=(EditText) findViewById(R.id.admin_name_editText);
        createMosqueAdmin=(Button) findViewById(R.id.create_account_button);
    }
}
