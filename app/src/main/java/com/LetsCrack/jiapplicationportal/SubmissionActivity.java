package com.LetsCrack.jiapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SubmissionActivity extends AppCompatActivity {
    EditText startDate1,endDate1,fRoomNo1,txtName1,txtPhone1,txtph1,txtph2;
    EditText yearr;
    private long backPressedTime;
    private Toast backToast;
    Button sendMail12,toProcess;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    private String TAG;
    DatabaseReference reff2;
    FirebaseAuth auth2;
    FirebaseUser userr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        startDate1=(EditText) findViewById(R.id.editTextD18);
        endDate1=(EditText) findViewById(R.id.editTextD28);
        fRoomNo1=(EditText) findViewById(R.id.wrRoomNo8);
        txtName1=(EditText) findViewById(R.id.wName8);
        txtPhone1=(EditText) findViewById(R.id.wPhoneNo8);
        sendMail12=(Button) findViewById(R.id.buttonSendM);
        txtph1=(EditText) findViewById(R.id.wph1);
        txtph2=(EditText) findViewById(R.id.wPh2);
        toProcess=(Button) findViewById(R.id.toProcess);
        yearr=(EditText) findViewById(R.id.yr);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        endDate1.setText(str);

        sendMail12.setEnabled(false);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        startDate1.setText(currentDateTimeString);

        endDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        SubmissionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mdateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mdateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy:"+dayOfMonth+"/"+month+"/"+year);
                String date=dayOfMonth+"/"+month+"/"+year;
                endDate1.setText(date);
            }
        };

        sendMail12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finalFetch();
                sendSms();
                sendMail();
            }
        });

        toProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail12.setEnabled(true);
                sendSms();
                finalFetch();
            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(SubmissionActivity.this);
        builder.setMessage("Do u Want to close?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void finalFetch(){
        auth2=FirebaseAuth.getInstance();
        userr2=auth2.getCurrentUser();
        reff2= FirebaseDatabase.getInstance().getReference().child("Users").child(auth2.getUid());
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String phoneNO=dataSnapshot.child("phoneNO").getValue().toString();
                String rooomNN=dataSnapshot.child("roomNo").getValue().toString();
                String sph1=dataSnapshot.child("Uph1").getValue().toString();
                String sph2=dataSnapshot.child("Uph2").getValue().toString();
                String syr=dataSnapshot.child("Uyear").getValue().toString();
                txtName1.setText(name);
                txtPhone1.setText(phoneNO);
                fRoomNo1.setText(rooomNN);
                txtph1.setText(sph1);
                txtph2.setText(sph2);
                yearr.setText(syr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMail(){
        String sd1=startDate1.getText().toString();
        String sd2=endDate1.getText().toString();
        String sd3=fRoomNo1.getText().toString();
        String sd4=txtName1.getText().toString();
        String sd5=txtPhone1.getText().toString();
        String sd6=yearr.getText().toString();

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String[] to={"jijaugirlshostel2020@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL,to);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Leave Application");
        intent.putExtra(Intent.EXTRA_TEXT,"Start Date:- \n"+sd1+"\nEnd Date:- \n"+sd2+"\nRoom No:- \n"+sd3+"\nName:- \n"+sd4+"\nPhone No:- \n"+sd5+"\n Year:- \n "+sd6);
        intent.setType("message/rfc822");
        Intent chooser = intent.createChooser(intent, "Send Email");
        startActivity(chooser);
    }

    public void sendSms(){
        String sd6=txtph1.getText().toString().trim();
        String sd7=txtph2.getText().toString().trim();
        String sd2=endDate1.getText().toString();
        String sd1=startDate1.getText().toString();

        int permissionCheck= ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            MyMessage();
        }

        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);

        }

    }

    private void MyMessage() {
        String sd6=txtph1.getText().toString().trim();
        String sd7=txtph2.getText().toString().trim();
        String sd2=endDate1.getText().toString();
        String sd1=startDate1.getText().toString();

        if (!txtph1.getText().toString().equals("")||!txtph2.getText().toString().equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            SmsManager smsManager2 = SmsManager.getDefault();
            smsManager.sendTextMessage(sd6 , null, "Your Daughter has Taken The Leave From The Jijau Girls Hostel", null, null);
            smsManager.sendTextMessage(sd6 , null, sd1+"\t to \t"+sd2, null, null);
            smsManager2.sendTextMessage(sd7, null, "Your Daughter has Taken The Leave From The Jijau Girls Hostel GCOEA ", null, null);
            smsManager.sendTextMessage(sd7 , null, sd1+"\t to \t"+sd2, null, null);

            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Please Check The phone number",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode){
            case 0:
                if(grantResults.length>=0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                }
                else {

                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,Main2Login.class));
                finish();
                break;
            case R.id.about:
                LetsCrackDialog LetsCrackDialog=new LetsCrackDialog();
                LetsCrackDialog.show(getSupportFragmentManager(),"LetsCrack Dialog");
                break;

        }
        return true;
    }
}


/* Intent fintent=new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",sd6,null));
        Intent fintent2=new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",sd7,null));
        fintent.putExtra("sms_body","Your Daughter has Taken The From Jijau Girls Hostel GCOEA");
        fintent.putExtra("sms_bod",sd1+"\t to \t"+sd2);
        fintent2.putExtra("sms_body","Your Daughter has Taken The From Jijau Girls Hostel GCOEA");
        fintent2.putExtra("sms_bod",sd1+"\t to \t"+sd2);
        startActivity(fintent);
        startActivity(fintent2);*/