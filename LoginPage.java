package com.example.transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class LoginPage extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText t1;
    EditText t2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        t1 = (EditText)findViewById(R.id.t1);
        t2 = (EditText)findViewById(R.id.tusername);
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(t1);
        b1=(Button)findViewById(R.id.b1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this,Otp.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                intent.putExtra("username",t2.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}