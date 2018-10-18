package com.example.nouman.appadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.welcometextview);
        b1=(Button) findViewById(R.id.addmosquebutton);
        b2= (Button) findViewById(R.id.removemosquebutton);
        b3=(Button) findViewById(R.id.mosqueadminbutton);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddMosqueActivity.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CreateMosqueAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}
