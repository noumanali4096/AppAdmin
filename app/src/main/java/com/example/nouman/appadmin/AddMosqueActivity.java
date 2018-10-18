package com.example.nouman.appadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddMosqueActivity extends AppCompatActivity {

    Button add;
    EditText et1,et2,et3,et4,et5;
    DatabaseReference databaseMoque;
    DatabaseReference databaseMoqueAdmin;
    List<Mosque> mosqueList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mosque);
        et1=(EditText) findViewById(R.id.mosque_name_editText);
        et2=(EditText) findViewById(R.id.longitude_editText);
        et3=(EditText) findViewById(R.id.latitudes_editText);
        et4=(EditText) findViewById(R.id.editText4);
        et5=(EditText) findViewById(R.id.editText3);
        add=(Button) findViewById(R.id.add_button);
        databaseMoque = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/mosque");
        databaseMoqueAdmin = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/mosqueadmin");
        mosqueList=new ArrayList<>();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMosque();
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et5.setText("");
            }
        });

    }

    private void addMosque(){
        String name =  et1.getText().toString().trim();
        String s1 = et2.getText().toString();
        String s2 = et3.getText().toString();
        String phone=et4.getText().toString()+et5.getText().toString().trim();
        double lang = Double.parseDouble(s1);
        double lat =  Double.parseDouble(s2);

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2) &&
                !TextUtils.isEmpty(et5.getText().toString().trim()))
        {
            Mosque mosque = new Mosque(name,lang,lat,phone);
            MosqueAdmin mosqueAdmin = new MosqueAdmin(phone,phone);
            databaseMoque.child(phone).setValue(mosque);
            databaseMoqueAdmin.child(phone).setValue(mosqueAdmin);
            Toast.makeText(this,"Mosque added",Toast.LENGTH_LONG).show();
        }
        else {
            if(TextUtils.isEmpty(name)){
                et1.setError("Enter Mosque name");
            }
            if(TextUtils.isEmpty(s1)){
                et1.setError("Enter Longitude");
            }
            if(TextUtils.isEmpty(s2)){
                et1.setError("Enter Latitude");
            }
            if(TextUtils.isEmpty(et5.getText().toString().trim()))
            {
                et5.setError("Enter phone#");
            }
        }
    }
}
