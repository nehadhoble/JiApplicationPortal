package com.LetsCrack.jiapplicationportal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText3,edittext4;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i1=new Intent(MainActivity.this,VerifyCodeActivity.class);
                startActivity(i1);
                finish();
            }
        }, 3000);
    }
}
