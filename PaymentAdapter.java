package com.example.transaction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PaymentAdapter extends ArrayAdapter<PaymentDetails> {
    private static final String TAG = "DetailsAdapter";
    private Context mContext;
    private int mResource;
    private String type;
    private String passedUsername;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public PaymentAdapter(@NonNull Context context, int resource, @NonNull List<PaymentDetails> objects,String passedUsername,String type) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.type = type;
        this.passedUsername = passedUsername;
    }

    public void updateTransaction(String passedName,String name,String money,String date,String type,String dataPhone){
        if(type.equals("Customers")){
            type = "Income";
        }else{
            type = "Expense";
        }
        Details details = new Details(name, type, money, date);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(passedName).child("Transaction").child(dataPhone).setValue(details);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String money = getItem(position).getMoney();
        String date = getItem(position).getDate();
        String phone = getItem(position).getPhone();

        PaymentDetails paymentDetails = new PaymentDetails(name,money,date,phone);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        TextView tvname = (TextView) convertView.findViewById(R.id.pname);
        TextView tvmoney = (TextView) convertView.findViewById(R.id.pmoney);
        TextView tvdate = (TextView) convertView.findViewById(R.id.pdate);
        ImageView ivphone = (ImageView) convertView.findViewById(R.id.phoneimg);
        tvname.setText(name);
        tvmoney.setText(money);
        tvdate.setText(date);
        ivphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("users").child(passedUsername).child(type);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap : snapshot.getChildren()){
                            String fetchedPhone = snap.child("phone").getValue().toString();
                            String fetchedName = snap.child("name").getValue().toString();
                            String fetchedMoney = snap.child("money").getValue().toString();
                            String fetchedDate = snap.child("date").getValue().toString();
                            if(fetchedPhone.equals(phone)){
                                snap.getRef().removeValue();
                                updateTransaction(passedUsername,fetchedName,fetchedMoney,fetchedDate,type,fetchedPhone);
                                Toast.makeText(mContext.getApplicationContext(),"deleted "+phone,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG,"on cancelled",error.toException());
                    }
                });
            }
        });
        return convertView;
    }
}
