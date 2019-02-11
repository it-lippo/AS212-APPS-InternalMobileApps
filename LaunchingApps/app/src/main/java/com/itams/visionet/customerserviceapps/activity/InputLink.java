package com.itams.visionet.customerserviceapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.webkit.URLUtil.isValidUrl;

public class InputLink extends AppCompatActivity {

    @BindView(R.id.inputIntranet)
    EditText inputIntranet;
    @BindView(R.id.inputInternet)
    EditText inputInternet;
    @BindView(R.id.btnSave)
    Button btnSave;




    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_link);
        ButterKnife.bind(this);
        sessionManagement = new SessionManagement(getApplicationContext());

        if (sessionManagement.getinternet() != "" && sessionManagement.getIntranet() != "" ){
            Intent intent = new Intent(this,NetworkActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @OnClick(R.id.btnSave)
    public void onClick() {

        savelink();
    }

    private void savelink() {

        if(correctioneditext(inputInternet) && correctioneditext(inputIntranet)){
//            set to Sharepref
            sessionManagement.setLink(inputIntranet.getText().toString().trim(),inputInternet.getText().toString().trim());
            Toast.makeText(this,"Link Has Been Saved",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,NetworkActivity.class);
            startActivity(intent);
            this.finish();
        }

    }


    public boolean correctioneditext(EditText editext) {
        if(editext.getText().toString() == "" || editext.getText().toString() == null || editext.getText().toString().isEmpty()){
            editext.setError("This field is required");
            return  false;
        }else {
            if (!isValidUrl(editext.getText().toString())){
                Log.i("check input url ", String.valueOf(isValidUrl(editext.getText().toString())));
                editext.setError("URL Format is required");
                return false;
            }else {
                return true;
            }
        }
    }
}
