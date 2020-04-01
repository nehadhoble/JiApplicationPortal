package com.LetsCrack.jiapplicationportal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyCodeActivity extends AppCompatActivity {
    EditText code;
    Button verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        code = (EditText) findViewById(R.id.editCode);
        verify = (Button) findViewById(R.id.buttonVerify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod = code.getText().toString().trim();
                if (cod.isEmpty()) {
                    code.setError("Verification Code Required!");
                    code.requestFocus();
                    return;
                }
                if (cod.equals("15987")){
                    Intent v1=new Intent(VerifyCodeActivity.this,Main2Login.class);
                    startActivity(v1);
                    finish();
                }else {
                    Toast.makeText(VerifyCodeActivity.this,"Enter The Valid Code",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(VerifyCodeActivity.this);
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

}
