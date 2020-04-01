package com.LetsCrack.jiapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class Main2Login extends AppCompatActivity implements View.OnClickListener {
    public Button SignUpBtn;
    public EditText password,emailR;
    ProgressBar progressBar1;
    public TextView reset1;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_login);
        SignUpBtn=(Button) findViewById(R.id.Sign_Up);
        password=(EditText) findViewById(R.id.passw);
        emailR=(EditText) findViewById(R.id.collgID);
        progressBar1=(ProgressBar) findViewById(R.id.progressBar);
        reset1=(TextView) findViewById(R.id.reset);
        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ioi=new Intent(Main2Login.this,ForgotPassword.class);
                startActivity(ioi);
                finish();
            }
        });



        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Login.this,Main3Registration.class);
                finish();
                startActivity(i);
            }
        });

        mAuth=FirebaseAuth.getInstance();
        findViewById(R.id.Sign_In).setOnClickListener(this);

        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Main2Login.this);
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

    private void userLogin(){
        final String sEmail=emailR.getText().toString().trim();
        final String sPassword=password.getText().toString().trim();
        if(sEmail.isEmpty()){
            emailR.setError("Email Required");
            emailR.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            emailR.setError("Enter a Valid Email");
            emailR.requestFocus();
            return;
        }
            if (sPassword.isEmpty()){
                password.setError("Password Required");
                password.requestFocus();
                return;
            }
            if (sPassword.length()<6){
                password.setError("password should be at least 6 character");
                password.requestFocus();
                return;
            }

            progressBar1.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar1.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                       // finish();
                        Intent intent=new Intent(Main2Login.this, PoratalActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        //finish();

                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(Main2Login.this, PoratalActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Sign_In:
                userLogin();
                return;
        }
    }
}