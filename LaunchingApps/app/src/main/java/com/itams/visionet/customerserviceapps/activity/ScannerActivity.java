package com.itams.visionet.customerserviceapps.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.ScannerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity {

    @BindView(R.id.containerView)
    FrameLayout containerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String Scanner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        Scanner = bundle.getString("title");
        setFragment(new ScannerFragment(), Scanner);
    }

    @Override
    public void onBackPressed() {
        if(getSupportActionBar().getTitle().equals("Setting")){
            setFragment(new ScannerFragment(), Scanner);
        }else{
            super.onBackPressed();
            ScannerActivity.this.finish();
        }

      /*if (count == 0) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }*/
        /*if (count==0){
            super.onBackPressed();
        } else if (count>0){
            getSupportFragmentManager().popBackStack();
        }*/
    }

    @Override
    protected void onDestroy() {
        Log.i("Scanner", "Destroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i("Scanner", "Pause");
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
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
