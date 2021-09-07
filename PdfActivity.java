package com.example.transaction;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class PdfActivity extends AppCompatActivity {

    private Button create;
    EditText name , pno,qty , item , status;
    Bitmap bmp;
    Bitmap scaleBmp;
    int pageWidth = 1200;
    Date dateObj;
    DateFormat dateFormat;
//    Float[] price = new Float[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//
//        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#f58769"));

        // Set BackgroundDrawable
       // actionBar.setBackgroundDrawable(colorDrawable);
        create = findViewById(R.id.button_pdf);
        name = findViewById(R.id.name);
        pno = findViewById(R.id.pno);
        qty = findViewById(R.id.qty);
        item = findViewById(R.id.item);
        status = findViewById(R.id.status);

        bmp = BitmapFactory.decodeResource(getResources() , R.drawable.index1);
        scaleBmp = Bitmap.createScaledBitmap(bmp,1200,518,false);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        CreatePDF();


    }

    public void CreatePDF(){
        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                dateObj = new Date();


                if (name.getText().toString().length()==0 ||
                        pno.getText().toString().length()==0||
                        qty.getText().toString().length()==0 ||
                        item.getText().toString().length()==0){
                    Toast.makeText(PdfActivity.this,"Fill all fields" , Toast.LENGTH_LONG).show();

                }
                else{
                    PdfDocument myPdf = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();

                    PageInfo myPageInfo = new PageInfo.Builder(1200, 2010, 1).create();

                    PdfDocument.Page myPage = myPdf.startPage(myPageInfo);

                    Canvas canvas = myPage.getCanvas();
                    canvas.drawBitmap(scaleBmp ,0 ,0 , myPaint);
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT , Typeface.BOLD));
                    titlePaint.setTextSize(70);
                    canvas.drawText("FOR Your Payment",pageWidth/2 , 270 , titlePaint);

                    myPaint.setColor(Color.rgb(0,113,188));
                    myPaint.setTextSize(30);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Developed by S3HNV",1100,40,myPaint);
                    canvas.drawText("call-9094859032",1100,90,myPaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT , Typeface.ITALIC));
                    titlePaint.setTextSize(70);
                    canvas.drawText("invoice" , pageWidth/2 , 500, titlePaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Customer Name" + name.getText() , 20,40,myPaint);
                    canvas.drawText("phone number:" + pno.getText() , 20,90,myPaint);

                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    dateFormat = new SimpleDateFormat("dd/mm/yy");
                    canvas.drawText("Date:" + dateFormat.format(dateObj), pageWidth-20,640,myPaint);
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Time" + dateFormat.format(dateObj) , pageWidth-20, 690,myPaint);

                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20,780,pageWidth-20,860,myPaint);

                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("NO" , 140,830 , myPaint);
                    canvas.drawText("Item" , 240,830 , myPaint);
                    // canvas.drawText("Quantity" , 700,830 , myPaint);
                    canvas.drawText("Price" , 900,830 , myPaint);
                    canvas.drawText("Status" , 1050,830 , myPaint);

                    canvas.drawLine(160,790,160,840,myPaint);
                    //  canvas.drawLine(500,790,500,840,myPaint);
                    canvas.drawLine(800,790,800,840,myPaint);
                    canvas.drawLine(940,790,940,840,myPaint);
                    canvas.drawText("1", 40,950, myPaint);
                    canvas.drawText(item.getText().toString() , 300,950,myPaint);
                    canvas.drawText(qty.getText().toString() , 900,950,myPaint);
                    // float price = 8*1000;

                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(status.getText().toString(), pageWidth-40, 950, myPaint);

                    myPdf.finishPage(myPage);
                    File file = new File(Environment.getExternalStorageDirectory() , "Invoice.pdf");
                    try {
                        myPdf.writeTo(new FileOutputStream(file));
                        Toast.makeText(PdfActivity.this, "INVOICE generated.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(PdfActivity.this, "INVOICE failed", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
//                        e.printStackTrace();
                        Toast.makeText(PdfActivity.this, "INVOICE generation failed.", Toast.LENGTH_SHORT).show();
                    }
                    myPdf.close();
                }

            }
        }) ;
    }


}