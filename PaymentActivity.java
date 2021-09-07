package com.example.transaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    // for popup from
    private AlertDialog.Builder dialogeBuilder;
    private AlertDialog dialog;
    private EditText popupname,popupmoney,popupdate,popupphone;
    private Button popupsave;
    private Button add_new;
    private ListView detailsView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private boolean pendingType;
    Bundle bundle;
    private String passedUserName;
    private ArrayList<PaymentDetails> paymentDetailsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Deatils..");

        bundle = getIntent().getExtras();
        String passedType = bundle.getString("type");
        passedUserName = bundle.getString("username");
        if(passedType.equals("pending")){
            pendingType = true;
        }else{
            pendingType = false;
        }
        add_new = findViewById(R.id.addNewButton);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialoge();
            }
        });
        setPaymentList();

//        detailsView = findViewById(R.id.listCont);
//        ArrayList<PaymentDetails> paymentDetailsArrayList = new ArrayList<>();
//        // fetch details from datanase
//        for(int i=0;i<10;i++)
//        {
//            String name = "name"+i;
//            String money = "100"+i;
//            String date = ""+i+""+i+"/"+i+""+i+"/"+i+""+i;
//            String phone = "123456789"+i;
//            PaymentDetails paymentDetails = new PaymentDetails(name,money,date,phone);
//            paymentDetailsArrayList.add(paymentDetails);
//        }
//        PaymentAdapter detailsAdapter = new PaymentAdapter(this,R.layout.payment_layout,paymentDetailsArrayList);
//        detailsView.setAdapter(detailsAdapter);
    }

    public void setPaymentList(){
        progressDialog.show();
        detailsView = findViewById(R.id.listCont);
        paymentDetailsArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if(pendingType == true) {
            databaseReference = firebaseDatabase.getReference().child("users").child(passedUserName).child("Customers");
        }else {
            databaseReference = firebaseDatabase.getReference().child("users").child(passedUserName).child("Sellers");
        }
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    paymentDetailsArrayList.clear();
                    for(DataSnapshot postSnapShot : snapshot.getChildren()){
                        String name = postSnapShot.child("name").getValue().toString();
                        String money = postSnapShot.child("money").getValue().toString();
                        String date = postSnapShot.child("date").getValue().toString();
                        String phone = postSnapShot.child("phone").getValue().toString();
                        PaymentDetails foundDetails = new PaymentDetails(name,money,date,phone);
                        paymentDetailsArrayList.add(foundDetails);
                    }
                    PaymentAdapter paymentAdapter;
                    if(pendingType == true) {
                        paymentAdapter = new PaymentAdapter(getApplicationContext(), R.layout.payment_layout, paymentDetailsArrayList,passedUserName, "Customers");
                    }else{
                        paymentAdapter = new PaymentAdapter(getApplicationContext(), R.layout.payment_layout, paymentDetailsArrayList,passedUserName, "Sellers");
                    }
                    detailsView.setAdapter(paymentAdapter);
                    paymentAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Failed to Load !",Toast.LENGTH_SHORT).show();
                }
            });
        }


    public void popupDialoge(){
        paymentDetailsArrayList.clear();
        dialogeBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup,null);
        popupname = (EditText) popupView.findViewById(R.id.popupname);
        popupmoney = (EditText) popupView.findViewById(R.id.popupmoney);
        popupdate = (EditText) popupView.findViewById(R.id.popupdate);
        popupphone = (EditText) popupView.findViewById(R.id.popupphone);
        popupsave = (Button) popupView.findViewById(R.id.popupsave);

        dialogeBuilder.setView(popupView);
        dialog = dialogeBuilder.create();
        dialog.show();

        // add pending/payable to database
        popupsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dataName = popupname.getText().toString().trim();
                String dataMoney = popupmoney.getText().toString().trim();
                String dataDate = popupdate.getText().toString().trim();
                String dataPhone = popupphone.getText().toString().trim();
                PaymentDetails paymentDetails = new PaymentDetails(dataName,dataMoney,dataDate,dataPhone);

                //add to database
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("users");

                if(pendingType == true){
                    databaseReference.child(passedUserName).child("Customers").child(dataPhone).setValue(paymentDetails);
                }else{
                    databaseReference.child(passedUserName).child("Sellers").child(dataPhone).setValue(paymentDetails);
                }
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}