package com.example.transaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //private ListView listView;
    private TextView userName,expenseVal,earningVal,textView;
    private LinearLayout pending;
    private LinearLayout payable;
    private Button newTran;

    //to create popup dialoge
    private AlertDialog.Builder dialogeBuilder;
    private AlertDialog dialog;
    private EditText popupname,popupmoney,popupdate,popupphone;
    private Button popupsave;
    private Spinner spinnerTypeSelect;
    private String selectedType;

    //firebase instences
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //username
    private String passedName;

    private ProgressDialog progressDialog;
    //stores the contents of the list
    private ArrayList<Details> arrayList;
    private double earnings,expenses,pending_val,payable_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        earnings = 0;
        expenses = 0;
        payable_val = 0;
        pending_val = 0;
        System.out.println("On create method called");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        setContentView(R.layout.activity_home);

        userName = findViewById(R.id.user_name);
        expenseVal = findViewById(R.id.val_expenses);
        earningVal = findViewById(R.id.val_earnings);

        Bundle bundle1 = getIntent().getExtras();
        passedName = bundle1.getString("NAME");
        userName.setText("Hey "+passedName+" !");
        setTransactionList();
        textView = findViewById(R.id.heading_history);
        pending = findViewById(R.id.pending);
        payable = findViewById(R.id.payable);
        newTran = findViewById(R.id.button_newTran);
        newTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialoge();
            }
        });

        Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",passedName);

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","pending");
                intent.putExtras(bundle);
                startActivity(intent);
                setTransactionList();
            }
        });

        payable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","payable");
                intent.putExtras(bundle);
                startActivity(intent);
                setTransactionList();
            }
        });
    }

    public void setPending_val(){
        TextView textRefPending = findViewById(R.id.on_button_pending);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(passedName).child("Customers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pending_val = 0;
                for(DataSnapshot postSnapShot : snapshot.getChildren()){
                    String money = postSnapShot.child("money").getValue().toString();
                    pending_val = pending_val + Double.valueOf(money);
                }
                //System.out.println(pending_val);
                textRefPending.setText(String.valueOf(pending_val));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load !",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setPayable_val(){
        TextView textRefPending = findViewById(R.id.on_button_payable);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(passedName).child("Sellers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                payable_val = 0;
                for(DataSnapshot postSnapShot : snapshot.getChildren()){
                    String money = postSnapShot.child("money").getValue().toString();
                    payable_val = payable_val + Double.valueOf(money);
                    //System.out.println(payable_val);
                }
                textRefPending.setText(String.valueOf(payable_val));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load !",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setTransactionList(){
        progressDialog.show();
        setPayable_val();
        setPending_val();
        ListView listView = findViewById(R.id.history);
        arrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(passedName).child("Transaction");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                earnings = 0;
                expenses = 0;
                for(DataSnapshot postSnapShot : snapshot.getChildren()){
                    String name = postSnapShot.child("name").getValue().toString();
                    String money = postSnapShot.child("money").getValue().toString();
                    String date = postSnapShot.child("date").getValue().toString();
                    String tempType = postSnapShot.child("type").getValue().toString();
                    String type;
                    if(tempType.equals("Income")){
                        type = "+";
                        earnings = earnings + Double.valueOf(money);
                    }else{
                        type = "-";
                        expenses = expenses + Double.valueOf(money);
                    }
                    Details founndDetails = new Details(name,type,money,date);
                    arrayList.add(founndDetails);
                }
                DetailsAdapter detailsAdapter = new DetailsAdapter(getApplicationContext(),R.layout.adapter_layout,arrayList);
                listView.setAdapter(detailsAdapter);
                expenseVal.setText(String.valueOf(expenses));
                earningVal.setText(String.valueOf(earnings));
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to Load !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void popupDialoge(){
        earnings = 0;
        expenses = 0;
        arrayList.clear();
        dialogeBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_transaction,null);
        popupname = (EditText) popupView.findViewById(R.id.Tpopupname);
        popupmoney = (EditText) popupView.findViewById(R.id.Tpopupmoney);
        popupdate = (EditText) popupView.findViewById(R.id.Tpopupdate);
        popupphone = (EditText) popupView.findViewById(R.id.Tpopupphone);
        popupsave = (Button) popupView.findViewById(R.id.Tpopupsave);
        spinnerTypeSelect = (Spinner) popupView.findViewById(R.id.spinnerTypeSelect);
        spinnerTypeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "Expense";
            }
        });
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Expense");
        arrayList.add("Income");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,arrayList);
        spinnerTypeSelect.setAdapter(arrayAdapter);

        dialogeBuilder.setView(popupView);
        dialog = dialogeBuilder.create();
        dialog.show();

        // add transaction to database
        popupsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataName = popupname.getText().toString().trim();
                String dataMoney = popupmoney.getText().toString().trim();
                String dataDate = popupdate.getText().toString().trim();
                String dataPhone = popupphone.getText().toString().trim();
                Details details = new Details(dataName,selectedType,dataMoney,dataDate);
                //add to database
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("users");
                databaseReference.child(passedName).child("Transaction").child(dataPhone).setValue(details);
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}