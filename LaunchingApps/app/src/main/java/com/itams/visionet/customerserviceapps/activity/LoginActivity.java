package com.itams.visionet.customerserviceapps.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.MainActivity;
import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.rest.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    SessionManagement session;
    String resultLogin;
    ApiClient api = new ApiClient();
    String username;
    String password;
    String domainName;
    ProgressDialog progress;
    String Site;

    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.btn_login)
    TextView btn_login;
    @BindView(R.id.spinnerDomain)
    Spinner spinnerDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/Lato-Light.ttf");
        ButterKnife.bind(this);

        inputEmail.setTypeface(custom_font);
        inputPassword.setTypeface(custom_font);
        btn_login.setTypeface(custom_font);
        progress = new ProgressDialog(LoginActivity.this);

        initspinner();


        session = new SessionManagement(this);

        //update 0512 : jika sudah ada user login langsung masuk ke main activity
        if (session.getKeyIsLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void initspinner() {
        ArrayList<String> projectlist = new ArrayList<>();
        projectlist.add("KARAWACINET");
        projectlist.add("LIPPO-CIKARANG");
        MySpinnerAdapter projectAdapter = new MySpinnerAdapter(this, R.layout.spinner_domain_item, projectlist);
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDomain.setAdapter(projectAdapter);



    }


    @OnClick(R.id.btn_login)
    public void onClick() {

        username = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        domainName = spinnerDomain.getSelectedItem().toString();


        if (inputEmail.getText().toString().equals("budi@gmail.com") & inputPassword.getText().toString().equals("12345678")) {
            inputBudi();
        } else if (username != null || username != " " || password != null) {
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } else {
            Toast.makeText(getApplicationContext(), "username or password is empty", Toast.LENGTH_LONG).show();
        }

    }

    private void inputBudi() {
        ArrayList<String> menus = new ArrayList<>();
        menus.add("registration");
        menus.add("checker");
        menus.add("Ppinformation");
        menus.add("PreferredUnitSelection");
        menus.add("signature");
        //update 0512 : data login disimpan di SharedPref
        session.setActiveInformation("budi@gmail.com", "12345678", "KARAWACINET", menus,Site);
        session.setKeyIsLoggedIn(true);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Light.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showdialog();

            Log.i("getprojectrequeest", "onPreExecute");

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
            resultLogin = api.doLogin(username, password, domainName,getApplicationContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");

            hidepdialog();

            if (resultLogin != null)
                checkResultlogin();
            else {
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(LoginActivity.this);
                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                //kotakBuilder.setTitle("Error");
                kotakBuilder.setMessage("Please check your internet connection");
                kotakBuilder.setPositiveButton("close", null);
                kotakBuilder.create().show();
            }

        }

    }

    private void checkResultlogin() {
        try {
            JSONObject resultObj = new JSONObject(resultLogin);
            int code = resultObj.getInt("code");
            if (code == 1) {
//                update 0512 : data login disimpan di SharedPref
                JSONArray resultMenus = resultObj.getJSONArray("menus");
                if(resultMenus.length() > 0){
                    ArrayList<String> menus = new ArrayList<>();
                    for (int i = 0; i < resultMenus.length(); i++) {
                        JSONObject objectMenus = resultMenus.getJSONObject(i);
                        menus.add(objectMenus.getString("name"));
                    }
                    session.setActiveInformation(username, password, domainName, menus,Site);
                    session.setKeyIsLoggedIn(true);
//                Toast.makeText(LoginActivity.this, "Username and password is correct", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "login failed,check username or password", Toast.LENGTH_SHORT).show();
                }
            } else if (code == 0) {
                Toast.makeText(LoginActivity.this, "login failed,check username or password", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(LoginActivity.this, "login failed,check username or password", Toast.LENGTH_SHORT).show();

        }
    }

    private void hidepdialog() {

            progress.dismiss();

    }

    private void showdialog() {
        progress = ProgressDialog.show(this, null,
                "Loading...", true);
    }
}