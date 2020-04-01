package com.LetsCrack.jiapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.regex.Pattern;

public class Main3Registration extends AppCompatActivity implements View.OnClickListener {
    private long backPressedTime;
    private Toast backToast;
    public TextView alreadyReg;
    public EditText password, emailR, phoneNumber, roomNumber, nameR,coolgId1,year1,address1,ph1,ph2;
    public ProgressBar progressBar1;
    public Button signUp;
    private View view;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_registration);
        password = (EditText) findViewById(R.id.passw);
        emailR = (EditText) findViewById(R.id.collgID);
        roomNumber = (EditText) findViewById(R.id.roomNum);
        phoneNumber = (EditText) findViewById(R.id.phoneNum);

        ph1 = (EditText) findViewById(R.id.ph1);
        ph2 = (EditText) findViewById(R.id.ph2);
        address1 = (EditText) findViewById(R.id.address);
        year1 = (EditText) findViewById(R.id.year);
        coolgId1 = (EditText) findViewById(R.id.coolgId);

        nameR = (EditText) findViewById(R.id.name);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        alreadyReg = (TextView) findViewById(R.id.textView2);
        alreadyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Main3Registration.this, Main2Login.class);
                startActivity(i2);
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Main3Registration.this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, PoratalActivity.class));
            finish();
        }
    }

    private void registerUser() {
        final String sEmail = emailR.getText().toString().trim();
        final String sRoomNo = roomNumber.getText().toString().trim();
        final String sPhone = phoneNumber.getText().toString().trim();
        final String sName = nameR.getText().toString().trim();
        final String sPassword = password.getText().toString().trim();

        final String scollgId = coolgId1.getText().toString().trim();
        final String syear = year1.getText().toString().trim();
        final String saddress = address1.getText().toString().trim();
        final String sph1 = ph1.getText().toString().trim();
        final String sph2 = ph2.getText().toString().trim();


        if (sEmail.isEmpty()) {
            emailR.setError("Email Required");
            emailR.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            emailR.setError("Enter a Valid Email");
            emailR.requestFocus();
            return;
        }
        if (sRoomNo.isEmpty()) {
            roomNumber.setError("Valid RoomNumber Is Required");
            roomNumber.requestFocus();
            return;
        }
        if (sPhone.isEmpty()) {
            phoneNumber.setError("Phone Number Required");
            phoneNumber.requestFocus();
            return;
        }
        if (sPhone.length() != 10) {
            phoneNumber.setError("Enter Valid Phone Number");
            phoneNumber.requestFocus();
            return;
        }
        if (sName.isEmpty()) {
            nameR.setError("Name Is Required");
            nameR.requestFocus();
            return;
        }
        if (sPassword.isEmpty()) {
            password.setError("Password Required");
            password.requestFocus();
            return;
        }
        if (sPassword.length() < 6) {
            password.setError("password should be at least 6 character");
            password.requestFocus();
            return;
        }

        if (scollgId.isEmpty()) {
            coolgId1.setError("The feild cant be Empty");
            coolgId1.requestFocus();
            return;
        }

        if (syear.isEmpty()) {
            year1.setError("Password Required");
            year1.requestFocus();
            return;
        }

        if (saddress.isEmpty()) {
            address1.setError("Password Required");
            address1.requestFocus();
            return;
        }

        if (sph1.isEmpty()) {
            ph1.setError("Password Required");
            ph1.requestFocus();
            return;
        }

        if (sph1.length() != 10) {
            ph1.setError("Enter Valid Phone Number");
            ph1.requestFocus();
            return;
        }

        if (sph2.isEmpty()) {
            ph2.setError("Password Required");
            ph2.requestFocus();
            return;
        }

        if (sph2.length() != 10) {
            ph2.setError("Enter Valid Phone Number");
            ph2.requestFocus();
            return;
        }

        progressBar1.setVisibility(view.VISIBLE);

        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //we will store the additional feilds in database
                            User user = new User(sEmail, sRoomNo,scollgId,syear,sPhone,sName,saddress,sph1,sph2,sPassword);
                            FirebaseDatabase.getInstance().getReference("Users")//Creating the node into firebaseDatabase
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar1.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        finish();
                                        startActivity(new Intent(Main3Registration.this, PoratalActivity.class));
                                        finish();
                                        Toast.makeText(getApplicationContext(), "User Registered Successfullu", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Your Already Registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Main3Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                return;
        }

    }

}
