package com.itams.visionet.customerserviceapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

public class SplashScreen extends AppCompatActivity {
    TextView label;
    SessionManagement sessionManagement; ;
    //Set waktu lama splashscreen
    private static int splashInterval = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        sessionManagement = new SessionManagement(this);

        new Handler().postDelayed(new Runnable() {




            @Override
            public void run() {
                // TODO Auto-generated method stub

                if (sessionManagement.getinternet() == "" && sessionManagement.getinternet()== ""){
                    Intent i = new Intent(SplashScreen.this, InputLink.class);
                    startActivity(i);

                    //jeda selesai Splashscreen
                    this.finish();
                    SplashScreen.this.finish();

                }else {
                    Intent i = new Intent(SplashScreen.this, NetworkActivity.class);
                    startActivity(i);

                    //jeda selesai Splashscreen
                    this.finish();
                    SplashScreen.this.finish();
                }

            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };
}