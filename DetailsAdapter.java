package com.example.transaction;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<Details> {

    private static final String TAG = "DetailsAdapter";
    private Context mContext;
    private int mResource;

    public DetailsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Details> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String type = getItem(position).getType();
        String money = getItem(position).getMoney();
        String date = getItem(position).getDate();

        Details details = new Details(name,type,money,date);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        TextView tvname = (TextView) convertView.findViewById(R.id.lname);
        TextView tvtype = (TextView) convertView.findViewById(R.id.ltype);
        TextView tvmoney = (TextView) convertView.findViewById(R.id.lmoney);
        TextView tvdate = (TextView) convertView.findViewById(R.id.ldate);

        tvname.setText(name);
        tvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getApplicationContext(),name,Toast.LENGTH_SHORT).show();
            }
        });
        tvtype.setText(type);
        tvmoney.setText(money);
        tvdate.setText(date);

        if(type.equals("+")){
            tvtype.setTextColor(Color.parseColor("#02d602"));
            tvmoney.setTextColor(Color.parseColor("#02d602"));
        }else {
            tvtype.setTextColor(Color.parseColor("#fa0000"));
            tvmoney.setTextColor(Color.parseColor("#fa0000"));
        }
        return convertView;
    }
}
