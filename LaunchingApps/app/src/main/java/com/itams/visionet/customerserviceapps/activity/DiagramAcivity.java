package com.itams.visionet.customerserviceapps.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.fragment.DiagramaticFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiagramAcivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrammatic);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setFragment(new DiagramaticFragment(), "Diagrammatic 2");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i("count fragment", String.valueOf(count));

      /*  if (count == 0) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }*/
        if(count > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
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
