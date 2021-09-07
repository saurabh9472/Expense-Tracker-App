package com.example.transaction;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.text.InputType;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    EditText name;
    EditText mobile;
    EditText email;
    EditText description;
    EditText amount;
    EditText date;
    Button addEvent;

    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        name = findViewById(R.id.eventUserName);
        mobile = findViewById(R.id.eventUserPhone);
        email = findViewById(R.id.eventUserEmail);
        description = findViewById(R.id.eventDescription);
        amount = findViewById(R.id.eventAmount);
        date = findViewById(R.id.eventDate);
        addEvent = findViewById(R.id.addEvent);

        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ReminderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() && mobile.getText().toString().length() == 10 && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() && !date.getText().toString().isEmpty() && !amount.getText().toString().isEmpty()) {

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                    intent.putExtra(CalendarContract.Events.TITLE, "Pending Transaction from " + name.getText().toString());

                    String info = "Mobile number: " + mobile.getText().toString() + "\nAmount: " + amount.getText().toString() + "\n";
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, info + description.getText().toString());

//                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
//                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Nagpur");


                    try {
                        Date dtFromString = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                        assert dtFromString != null;
                        long timeInMilliseconds = dtFromString.getTime();

                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeInMilliseconds);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeInMilliseconds + 86400000);

                        Log.i("this", date.getText().toString() + " " + timeInMilliseconds + " " + dtFromString);
                    } catch (ParseException e) {
//                        e.printStackTrace();
                    }


                    intent.putExtra(Intent.EXTRA_EMAIL, email.getText().toString());


                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "There is no app that can support this action", Toast.LENGTH_SHORT).show();
                    }

                } else if (mobile.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Mobile number is Invalid", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(getApplicationContext(), "Email address is Invalid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}