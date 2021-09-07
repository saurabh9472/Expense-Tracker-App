package com.example.transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    EditText tvFeedback;
    // RatingBar ratingbars;
    RatingBar ratingBar;
    TextView tvRateCount,tvRateMessage;
    float ratedValue;
    Button sendbutton;
    EditText ouremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar = findViewById(R.id.ratingBar);
        // tvRateCount = (TextView) findViewById(R.id.tvRateCount);
        tvRateMessage =  findViewById(R.id.textView2);
        tvFeedback=findViewById(R.id.editTextTextMultiLine);
        sendbutton=findViewById(R.id.button);
        ouremail=findViewById(R.id.editTextTextMultiLine);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {

                ratedValue = ratingBar.getRating();

                // tvRateCount.setText("Your Rating : " + ratedValue + "/5.");




                if(ratedValue<1){

                    tvRateMessage.setText("Very Bad");

                }else if(ratedValue<2){

                    tvRateMessage.setText("Bad");

                }else if(ratedValue<3){

                    tvRateMessage.setText("Not bad.");

                }else if(ratedValue<4){

                    tvRateMessage.setText("OK");

                }else if(ratedValue<5){

                    tvRateMessage.setText("Nice");

                }else if(ratedValue==5){

                    tvRateMessage.setText("Very Nice");

                }

            }

        });



        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in feedback text box", Toast.LENGTH_LONG).show();

                } else {

                    String feedback=tvFeedback.getText().toString();
                    ratedValue = ratingBar.getRating();
                    String email=ouremail.getText().toString();

                    //rootnode =FirebaseDatabase.getInstance();
                    //reference=rootnode.getReference("users");
                    //helperclass Userhelper = new helperclass(ratedValue, feedback);
                    //reference.child(feedback).setValue(Userhelper);

                    Intent send =new Intent(Intent.ACTION_SEND);
                    //send.setType("message/html");

                    send.putExtra(Intent.EXTRA_EMAIL ,email);
                    send.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK FROM APP");
                    send.putExtra(Intent.EXTRA_TEXT,"RATING : "+ratedValue+"\n FEEDBACK: " + feedback);
                    send.setType("message/rfc822");
                    send.setPackage("com.google.android.gm");
                    startActivity(send);




                    tvFeedback.setText("");
                    ouremail.setText("");
                    //ratedValue = ratingBar.getRating();
                    String rating=String.valueOf(ratingBar.getRating());

                    Toast.makeText(getApplicationContext(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getApplicationContext(),rating,Toast.LENGTH_LONG).show();
                    ratingBar.setRating(0);
                }
            }
        });

    }
}