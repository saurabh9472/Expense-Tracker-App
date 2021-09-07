package com.example.transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import static android.Manifest.permission.CALL_PHONE;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String passUsername;
    String passPhone;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = findViewById(R.id.tusermain);
        passUsername = getIntent().getStringExtra("username").toString();
        passPhone = getIntent().getStringExtra("phone").toString();

        textView1.setText(passUsername);
        //System.out.println("name and number : "+passUsername+" "+passPhone);
        ImageView imageView1 = findViewById(R.id.pdfact);
        ImageView imageView2 = findViewById(R.id.transaction);
        ImageView imageView3 = findViewById(R.id.reminder);
        ImageView imageView4 = findViewById(R.id.feedback);
        ImageView imageView5 = findViewById(R.id.logout);
        ImageView imageView6 = findViewById(R.id.call_main);


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PdfActivity.class);
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),ForTransactionActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                bundle = new Bundle();
                bundle.putString("NAME",passUsername);
                bundle.putString("PHONE",passPhone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ReminderActivity.class);
                startActivity(intent);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FeedbackActivity.class);
                startActivity(intent);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9431747194"));

                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
                else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }

            }

        });

    }
}