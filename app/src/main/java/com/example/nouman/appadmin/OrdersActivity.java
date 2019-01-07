package com.example.nouman.appadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class OrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    View view;
    OrdersAdapter ordersAdapter;
    ArrayList<OrderModel> orderModels;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

       // view=inflater.inflate(R.layout.fragment_orders,container,false);

        recyclerView= findViewById(R.id.recycler_view_order);
        swipeRefreshLayout= findViewById(R.id.sweep);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this, LinearLayout.VERTICAL,false));
        swipeRefreshLayout.setEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com/orders");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts");
      //  FirebaseDatabase.getInstance().getReferenceFromUrl("https://azzan-f7f08.firebaseio.com//islamicproducts");
        orderModels= new ArrayList<>();
        firebaseCall();
//        if (orderModels.size()==0)
//        {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//
                orderModels.clear();
//                orderModels= new ArrayList<>();
                firebaseCall();
                ordersAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setEnabled(false);
                swipeRefreshLayout.setRefreshing(false);



            }
        });
        ordersAdapter= new OrdersAdapter(OrdersActivity.this, orderModels, new OrdersAdapter.MyClick() {
            @Override
            public void OnClick(int position) {

                Intent mIntent= new Intent(OrdersActivity.this,ViewProduct.class);
                mIntent.putExtra("data",orderModels.get(position));
                startActivity(mIntent);

            }
        });

        recyclerView.setAdapter(ordersAdapter);
        ordersAdapter.notifyDataSetChanged();
    }


    private void firebaseCall(){
//        swipeRefreshLayout.setRefreshing(true);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot messageSnapshot, @Nullable String s) {

                final OrderModel orderModel= new OrderModel();
                orderModel.quantity = (String) messageSnapshot.child("quantity").getValue();
                orderModel.phone = (String) messageSnapshot.child("phone").getValue();
                orderModel.pid = (String) messageSnapshot.child("pid").getValue();
                orderModel.created_at= (Long) messageSnapshot.child("created_at").getValue();
                orderModel.oid = messageSnapshot.getKey();

                final String productId = (String) messageSnapshot.child("pid").getValue();

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {

                        for (DataSnapshot childDataSnapshot : dataSnapshot1.getChildren()) {
                            Log.d("getId" ,childDataSnapshot.getKey());
                            Log.d("id" ,String.valueOf(String.valueOf(childDataSnapshot.getKey()).equals(productId)));
                            if(String.valueOf(childDataSnapshot.getKey()).equals(productId)){

                                Log.d("getId" ,(String)childDataSnapshot.child("title").getValue());
                                orderModel.productTitle = (String) childDataSnapshot.child("title").getValue();
                                orderModel.productPrice = (String) childDataSnapshot.child("price").getValue();
                                orderModel.productImage= (String) childDataSnapshot.child("imageUrl").getValue();


                                orderModels.add(orderModel);
                                ordersAdapter.notifyDataSetChanged();
//                                swipeRefreshLayout.setEnabled(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
