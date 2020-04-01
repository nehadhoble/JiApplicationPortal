package com.LetsCrack.jiapplicationportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    public TextView resetEmail;
    public Button resetPass;
    public ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        Intent ii=new Intent(this,Main2Login.class);
        startActivity(ii);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmail=(TextView) findViewById(R.id.resetEmail);
        resetPass=(Button) findViewById(R.id.resetPass);
        progressBar=findViewById(R.id.progressBarR);

        Toolbar toolbar3=findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);



        firebaseAuth=FirebaseAuth.getInstance();
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(resetEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this,"Reset Password Mail Send To Your mail",Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

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
}
