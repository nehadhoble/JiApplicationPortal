package com.LetsCrack.jiapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PoratalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int STORAGE_CODE = 50;
    private long backPressedTime;
    private Toast backToast;
    EditText startDate,endDate,fRoomNo,txtName,txtPhone;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    Button proceed1,showBtn;
    DatabaseReference reff,reff1;
    FirebaseAuth auth;
    ProgressBar pb;
    FirebaseUser userr,userr1;
    TextView txtFrom,txtTo;
    String uid;
    User user;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poratal);

        pb=(ProgressBar) findViewById(R.id.progressBar2);
        startDate=(EditText) findViewById(R.id.editTextD1);
        endDate=(EditText) findViewById(R.id.editTextD2);
        fRoomNo=(EditText) findViewById(R.id.wrRoomNo);
        txtName=(EditText) findViewById(R.id.wName);
        txtFrom=(TextView) findViewById(R.id.textViewFrom);
        txtTo=(TextView) findViewById(R.id.textViewTo);
        txtPhone=(EditText) findViewById(R.id.wPhoneNo);
        proceed1=(Button) findViewById(R.id.proceed);
        showBtn=(Button) findViewById(R.id.buttonShow);
        pb.setProgress(20);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        startDate.setText(currentDateTimeString);

        findViewById(R.id.proceed).setOnClickListener(this);
        findViewById(R.id.buttonShow).setOnClickListener(this);

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        PoratalActivity.this,
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
                endDate.setText(date);
            }
        };

        txtName.addTextChangedListener(loginTextWatcher);
        txtPhone.addTextChangedListener(loginTextWatcher);



    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(PoratalActivity.this);
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



    private TextWatcher loginTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usrName=txtName.getText().toString().trim();
            String usrPhone=txtPhone.getText().toString().trim();
            proceed1.setEnabled(!usrName.isEmpty()&&!usrPhone.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    public void createPdf() {
       if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                //permission was not granted request it
                String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission,STORAGE_CODE);
            }else {
                //permission already granted
                savePdf();
            }
        }
        else {
            savePdf();
        }
    }
    private void savePdf() {
        //create object of document class
        Document mDoc=new Document();
        //pdf file name
        String mFileName=new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath= Environment.getExternalStorageDirectory() +"/" + mFileName +".pdf";
        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
            mDoc.open();
            String mtxt9="GCOEA JIJAU GIRLS HOSTEL";
            String mtxt11="SUBJECT:-HOME LEAVE APPLICATION";
            String mText=startDate.getText().toString();
            String mText2=endDate.getText().toString();
            String mText3=fRoomNo.getText().toString();
            String mText4=txtName.getText().toString();
            String mText5=txtPhone.getText().toString();
            String mText6=txtFrom.getText().toString();
            String mText7=txtTo.getText().toString();
            String mtxt8="Room No:-";

            mDoc.addAuthor("JIJAU GIRLS HOSTEL");
            mDoc.addHeader("GCOEA","GCOEA");
            mDoc.addCreationDate();
            mDoc.add(new Paragraph(mtxt9));
            mDoc.add(new Paragraph(mtxt11));
            mDoc.add(new Paragraph(mText6));
            mDoc.add(new Paragraph(mText));
            mDoc.add(new Paragraph(mText7));
            mDoc.add(new Paragraph(mText2));
            mDoc.add(new Paragraph(mtxt8));
            mDoc.add(new Paragraph(mText3));
            mDoc.add(new Paragraph(mText4));
            mDoc.add(new Paragraph(mText5));

            mDoc.close();
            Toast.makeText(this,mFileName +".pdf\nis saved to\n"+mFileName,Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission was granted from popup
                    savePdf();
                }
                else {
                    //permission was denied from popup
                    Toast.makeText(this,"Permission Denied!!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void enterData() {
        final String sD1 = startDate.getText().toString().trim();
        final String sD2 = endDate.getText().toString().trim();
        final String sRn = fRoomNo.getText().toString().trim();

        if (sD1.isEmpty()) {
            startDate.setError("StarDate Required");
            startDate.requestFocus();
            return;
        }
        if (sD2.isEmpty()) {
            endDate.setError("EndDate Required");
            endDate.requestFocus();
            return;
        }
        if (sRn.isEmpty()) {
            fRoomNo.setError("RoomNo Required");
            fRoomNo.requestFocus();
            return;
        }
        if (sRn.length()>3) {
            fRoomNo.setError("Enter valid RoomNo");
            fRoomNo.requestFocus();
            return;
        }
        userr = FirebaseAuth.getInstance().getCurrentUser();
        uid = userr.getUid();
        user = new User();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);//("Users/report")
        user.setD1(startDate.getText().toString().trim());
        user.setD2(endDate.getText().toString().trim());
        user.setrN(fRoomNo.getText().toString().trim());
        reff.push().setValue(user);
        if (reff == null) {
            Toast.makeText(PoratalActivity.this, "Cant Insert the data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PoratalActivity.this, "Data Inserted SuccessFully", Toast.LENGTH_LONG).show();
            Intent ii=new Intent(PoratalActivity.this,SubmissionActivity.class);
            startActivity(ii);
            finish();
        }
    }

        public void showData(){
        auth=FirebaseAuth.getInstance();
        userr1=auth.getCurrentUser();
        reff1=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String phoneNO=dataSnapshot.child("phoneNO").getValue().toString();
                txtName.setText(name);
                txtPhone.setText(phoneNO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            case R.id.developer:
                startActivity(new Intent(this,ActivityDevelopers.class));
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.proceed:
                createPdf();
                enterData();
                String str = endDate.getText().toString();
                Intent intent = new Intent(getApplicationContext(), SubmissionActivity.class);
                intent.putExtra("message", str);
                final String sD1 = startDate.getText().toString().trim();
                final String sD2 = endDate.getText().toString().trim();
                final String sRn = fRoomNo.getText().toString().trim();
                if (sD1.isEmpty()) {
                    startDate.setError("StarDate Required");
                    startDate.requestFocus();
                    return;
                }
                if (sD2.isEmpty()) {
                    endDate.setError("EndDate Required");
                    endDate.requestFocus();
                    return;
                }
                if (sRn.isEmpty()) {
                    fRoomNo.setError("RoomNo Required");
                    fRoomNo.requestFocus();
                    return;
                }
                if (sRn.length()>3) {
                    fRoomNo.setError("Enter valid RoomNo");
                    fRoomNo.requestFocus();
                    return;
                }
                startActivity(intent);
                finish();
                pb.setProgress(60);
                return;
            case R.id.buttonShow:
                showData();
                createPdf();
                pb.setProgress(40);
                return;
        }
    }
}
