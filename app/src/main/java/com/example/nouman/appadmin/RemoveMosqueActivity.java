package com.example.nouman.appadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveMosqueActivity extends AppCompatActivity {

    Button remove;
    EditText et1;
    TextView t1;
    DatabaseReference databaseMoque;
    DatabaseReference databaseMoquenTimming;
    DatabaseReference databaseMoqueAdmin;
    List<Mosque> mosqueList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_mosque);
        et1=(EditText) findViewById(R.id.editText);
        databaseMoque = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/mosque");
        databaseMoquenTimming = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/mosquentiming");
        databaseMoqueAdmin = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/mosqueadmin");
        remove =(Button) findViewById(R.id.button2);
        mosqueList=new ArrayList<>();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMosque();
            }
        });
    }

    private void removeMosque(){
        String id =   et1.getText().toString().trim();

        if(!TextUtils.isEmpty(id))
        {

            databaseMoque.child(id).removeValue();
            Query applesQuery = databaseMoque.orderByChild("phone").equalTo(id);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseMoqueAdmin.child(id).removeValue();
            applesQuery = databaseMoque.orderByChild("phone").equalTo(id);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseMoquenTimming.child(id).removeValue();
            applesQuery = databaseMoque.orderByChild("phone").equalTo(id);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Toast.makeText(RemoveMosqueActivity.this,"Mosque removed",Toast.LENGTH_SHORT).show();
        }
        else {
            if(TextUtils.isEmpty(id)){
                et1.setError("Enter Mosque Id");
            }
        }
    }
}

