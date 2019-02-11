package com.itams.visionet.customerserviceapps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.activity.DiagramAcivity;
import com.itams.visionet.customerserviceapps.activity.OptionsActivity;
import com.itams.visionet.customerserviceapps.core.CoreApplication;
import com.itams.visionet.customerserviceapps.fragment.DiagramaticFragment;
import com.itams.visionet.customerserviceapps.fragment.HomeFragment;
import com.itams.visionet.customerserviceapps.fragment.ScannerFragment;
import com.itams.visionet.customerserviceapps.fragment.SettingFragment;
import com.itams.visionet.customerserviceapps.fragment.SignatureFragment;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PERMISSIONS_REQUEST_CAMERA = 200;
    private static final int SIGN_PERMISSIONS_REQUEST_STORAGE = 204;
    private static final int MAIN_PERMISSIONS_REQUEST_INTERNET = 205;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        session = new SessionManagement(this);

        navView.setNavigationItemSelectedListener(this);
        initnavheader();

        //update 1412 : Checking permission untuk pengguna marshmallow
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MAIN_PERMISSIONS_REQUEST_INTERNET);
        }

        setFragment(new HomeFragment(), "Launching Apps");
        //update 2501 :
        hideItem();
        //initFirebase();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.i("Tag", fragment.getTag());
        //getSupportActionBar().setTitle(fragment.getTag());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!getSupportActionBar().getTitle().equals("Launching Apps")){
            if(getSupportActionBar().getTitle().equals("Cluster")){
                setFragment(new DiagramaticFragment(), "Diagrammatic");
            } else {
                Log.i("Scan", "True");
                setFragment(new HomeFragment(), "Launching Apps");
            }
        } else {
            finish();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //proses pemilihan fragment sesuai item yang diklik
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (id) {
            case R.id.nav_reg:
                fragment = new ScannerFragment();
                title = "Registration";
                /*Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(i);*/
                break;
            case R.id.nav_check:
                fragment = new ScannerFragment();
                title = "Checker";
                break;
            case R.id.nav_diag:
                fragment = new DiagramaticFragment();
                title = "Diagrammatic";
                break;
            case R.id.nav_diag2:
                Intent intent = new Intent(getApplicationContext(), DiagramAcivity.class);
                startActivity(intent);
                break;
            case R.id.nav_status:
                fragment = new ScannerFragment();
                title = "PP Status";
                break;
            case R.id.nav_setting:
                Intent i = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(i);
                title = "Setting";
                break;
            case R.id.nav_logout:
                CoreApplication.getInstance().showLogoutConfirmation(MainActivity.this);
                break;
            case R.id.nav_signature:
                fragment = new SignatureFragment();
                title = "Signature";
                break;
        }

        // melakukan pemasangan fragment ke dalam frame layout secara programmatically
        if (fragment != null) {
            setFragment(fragment, title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("onReq", "Allow");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                //If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = new ScannerFragment();
                    String title = getSupportActionBar().getTitle().toString();
                    setFragment(fragment, title);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to use camera", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case SIGN_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SignatureFragment fragment = new SignatureFragment();
                    String title = "Signature";
                    setFragment(fragment, title);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to write storage", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case MAIN_PERMISSIONS_REQUEST_INTERNET: {
                if(grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(MainActivity.this, "Permission denied to access internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void hideItem(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        ArrayList<String> menus = session.getMenus();
        for(int i = 0; i < menus.size(); i++){
            if(menus.get(i).toLowerCase().equals("registration")){
                nav_Menu.findItem(R.id.nav_reg).setVisible(true);
            }
            if(menus.get(i).toLowerCase().equals("ppinformation")){
                nav_Menu.findItem(R.id.nav_status).setVisible(true);
            }
            if(menus.get(i).toLowerCase().equals("checker")){
                nav_Menu.findItem(R.id.nav_check).setVisible(true);
            }
            if(menus.get(i).toLowerCase().equals("preferredunitselection")){
                nav_Menu.findItem(R.id.nav_diag).setVisible(true);
                nav_Menu.findItem(R.id.nav_diag2).setVisible(true);
            }
            if(menus.get(i).toLowerCase().equals("signature")){
                nav_Menu.findItem(R.id.nav_signature).setVisible(true);
            }
        }

    }

    private void initnavheader() {
        View header_nav = navView.getHeaderView(0);

        TextView initial = (TextView)header_nav.findViewById(R.id.initial_name);
        TextView email_user = (TextView)header_nav.findViewById(R.id.email_user);
        TextView name_user = (TextView)header_nav.findViewById(R.id.name_user);

        String name = session.getUserName();
        if(!name.isEmpty()){
            initial.setText(name.substring(0,1));
            email_user.setText(name);
            String nickname = name.split("\\.")[0];
            name_user.setText(nickname);
        }

    }

    public void setFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title).addToBackStack(null);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();

        // set the toolbar title
        getSupportActionBar().setTitle(title);
    }
}
