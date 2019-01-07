package com.example.nouman.appadmin;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostEditActivity extends AppCompatActivity {

    private ImageButton imageBtn;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textTitle;
    private EditText textDesc;
    private EditText textPrice;
    private Button updateBtn;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private Boolean clikedImageButton;
    private DatabaseReference mDatabase;
    private FirebaseStorage mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        clikedImageButton = false;

        final ProductModel productModel = getIntent().getParcelableExtra("data");

        mFirebaseStorage = FirebaseStorage.getInstance();

        // initializing objects
        updateBtn = (Button)findViewById(R.id.add_product_btn);
        textDesc = (EditText)findViewById(R.id.product_desc);
        textTitle = (EditText)findViewById(R.id.product_name);
        textPrice = (EditText)findViewById(R.id.product_price);
        imageBtn = (ImageButton)findViewById(R.id.prodct_image);

        textTitle.setText(productModel.title);
        textDesc.setText(productModel.desc);
        textPrice.setText(productModel.price);
        Glide.with(this).load(productModel.imageUrl).into(imageBtn);

        storage = FirebaseStorage.getInstance().getReference();

       // mAuth = FirebaseAuth.getInstance();
       // mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts");

        Uri.parse(productModel.imageUrl);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                clikedImageButton = true;
            }
        });
        // posting to Firebase
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostEditActivity.this, "Updating...", Toast.LENGTH_LONG).show();
                updateBtn.setEnabled(false);
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostDesc = textDesc.getText().toString().trim();
                final String PostPrice = textPrice.getText().toString().trim();
                // do a check for empty fields
                if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle) && !TextUtils.isEmpty(PostPrice)){


                    if(clikedImageButton){
                        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts");
                        mDatabase.child(productModel.pid).removeValue();
                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(productModel.imageUrl);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                StorageReference filepath = storage.child("post_images").child(uri.getLastPathSegment());

                                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        @SuppressWarnings("VisibleForTests")
                                        //getting the post image download url
                                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                        databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts").child(productModel.pid);

                                        ProductModel product = new ProductModel();

                                        product.title = PostTitle;
                                        product.desc = PostDesc;
                                        product.price = PostPrice;
                                      //  product.uid = productModel.uid;
                                        product.imageUrl = downloadUrl.toString();

                                        databaseRef.setValue(product);
                                        Toast.makeText(PostEditActivity.this, "Product Updated", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PostEditActivity.this, IslamicProducts.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d("tag", "onFailure: did not delete file");
                            }
                        });

                    } else{
                        databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts").child(productModel.pid);

                        ProductModel product = new ProductModel();

                        product.title = PostTitle;
                        product.desc = PostDesc;
                        product.price = PostPrice;
                       // product.uid = productModel.uid;
                        product.imageUrl = productModel.imageUrl;

                        databaseRef.setValue(product);
                        finish();
                        Toast.makeText(PostEditActivity.this, "Product Updated", Toast.LENGTH_LONG).show();
                        Intent mIntent= new Intent(PostEditActivity.this,IslamicProducts.class);
                        startActivity(mIntent);

                    }


                }
            }
        });

    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();
            imageBtn.setImageURI(uri);
        }
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

