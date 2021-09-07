package com.example.transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForTransactionActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    Button submit;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_transaction);

        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextPhone = findViewById(R.id.editTextPhone);
        submit = findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                String enteredName = editTextName.getText().toString().trim();
                String enteredPhone = editTextPhone.getText().toString().trim();
                bundle = new Bundle();
                bundle.putString("NAME",enteredName);
                bundle.putString("PHONE",enteredPhone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}