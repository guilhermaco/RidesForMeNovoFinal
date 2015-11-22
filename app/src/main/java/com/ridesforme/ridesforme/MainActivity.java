package com.ridesforme.ridesforme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Runnable {
    private boolean logado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sf = getSharedPreferences("AndroidExamplePref", MODE_PRIVATE);
        String chaveSalva = sf.getString("name", "");
        if(chaveSalva != null && !chaveSalva.equals("")){
            logado = true;
        }
        Handler handler = new Handler();
        handler.postDelayed(this, 2000);
    }
    @Override
    public void run() {
        if(!logado){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
