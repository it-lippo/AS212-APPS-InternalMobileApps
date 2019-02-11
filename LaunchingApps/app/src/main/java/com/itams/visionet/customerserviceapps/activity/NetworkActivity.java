package com.itams.visionet.customerserviceapps.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.MainActivity;
import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetworkActivity extends AppCompatActivity {

    @BindView(R.id.radioIntra)
    RadioButton radioIntra;
    @BindView(R.id.radioInter)
    RadioButton radioInter;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.RadioGroupnet)
    RadioGroup RadioGroupnet;


    SessionManagement session;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ButterKnife.bind(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/Lato-Light.ttf");

        textView.setTypeface(custom_font);
        btnLogin.setTypeface(custom_font);
        radioInter.setTypeface(custom_font);
        radioIntra.setTypeface(custom_font);

        session = new SessionManagement(this);

        //update 0512 : jika sudah ada user login langsung masuk ke main activity
    }

    @OnClick(R.id.btn_login)
    public void onClick() {

        gotologin();

    }

    private void gotologin() {
        Intent i ;
//        startActivity(i);

        //jeda selesai Splashscreen
        if (radioInter.isChecked()){
            session.setSite("INTERNET");
//            Toast.makeText(this,session.getinternet(),Toast.LENGTH_SHORT).show();
        }else if (radioIntra.isChecked()){
            session.setSite("INTRANET");
//            Toast.makeText(this,session.getIntranet(),Toast.LENGTH_SHORT).show();

        }


        if (session.getKeyIsLoggedIn()){
            intent = new Intent(this,MainActivity.class);
        }else{
            intent = new Intent(this,LoginActivity.class);
        }
        startActivity(intent);
        this.finish();
    }
}
