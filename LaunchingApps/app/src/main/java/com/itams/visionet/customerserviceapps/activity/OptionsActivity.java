package com.itams.visionet.customerserviceapps.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.OptionsFragment;
import com.itams.visionet.customerserviceapps.fragment.ScannerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.itams.visionet.customerserviceapps.R;

public class OptionsActivity extends AppCompatActivity {

    @BindView(R.id.containerView)
    FrameLayout containerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        setFragment(new OptionsFragment(), "Options");
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i("count fragment", String.valueOf(count));

//        if (count == 0) {
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }

        if (count>1){
            getSupportFragmentManager().popBackStack();
        }else {
            finish();
        }
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        // set the toolbar title
        getSupportActionBar().setTitle(fragment.getTag().toString());

        super.onAttachFragment(fragment);
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
