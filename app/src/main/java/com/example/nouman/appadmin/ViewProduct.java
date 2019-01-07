package com.example.nouman.appadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;




public class ViewProduct extends AppCompatActivity {

    TextView title, desc, price,edit,delete;
    private ImageView image;
    private DatabaseReference mDatabase;
    private FirebaseStorage mFirebaseStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts");
        mFirebaseStorage = FirebaseStorage.getInstance();

        final ProductModel productModel = getIntent().getParcelableExtra("data");
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        price = findViewById(R.id.price);
        image=findViewById(R.id.image);
        edit=findViewById(R.id.edit_btn);
        delete=findViewById(R.id.delete_btn);

        title.setText(productModel.title);
        desc.setText(productModel.desc);
        price.setText(String.valueOf(productModel.price));
        Glide.with(this).load(productModel.imageUrl).into(image);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(productModel.pid).removeValue();
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(productModel.imageUrl);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Log.d("tag", "onSuccess: deleted file");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Log.d("tag", "onFailure: did not delete file");
                    }
                });
                Intent mainintent = new Intent(ViewProduct.this, MainActivity.class);
                startActivity(mainintent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),PostEditActivity.class);
                intent.putExtra("data",productModel);
                startActivity(intent);

            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                onBackPressed();

                break;
        }

        return true;
    }
}
