package com.itams.visionet.customerserviceapps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.activity.ScannerActivity;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @BindView(R.id.btnRegister)
    LinearLayout btnRegister;
    @BindView(R.id.btnChecker)
    LinearLayout btnChecker;
    @BindView(R.id.btnDiagrammatic)
    LinearLayout btnDiagrammatic;
    @BindView(R.id.btnPPStatus)
    LinearLayout btnPPStatus;
    @BindView(R.id.btnSignature)
    LinearLayout btnSignature;

    SessionManagement session;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        session = new SessionManagement(getActivity());

        hideItem();
        return view;
    }

    @OnClick({R.id.btnRegister, R.id.btnChecker, R.id.btnDiagrammatic, R.id.btnPPStatus, R.id.btnSignature})
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.btnRegister:
                i = new Intent(getActivity(), ScannerActivity.class);
                i.putExtra("title", "Registration");
                startActivity(i);
                break;
            case R.id.btnChecker:
                i = new Intent(getActivity(), ScannerActivity.class);
                i.putExtra("title", "Checker");
                startActivity(i);
                break;
            case R.id.btnDiagrammatic:
                setFragment(new DiagramaticFragment(), "Diagrammatic");
                break;
            case R.id.btnPPStatus:
                i = new Intent(getActivity(), ScannerActivity.class);
                i.putExtra("title", "PP Status");
                startActivity(i);
                break;
            case R.id.btnSignature:
                setFragment(new SignatureFragment(), "Signature");
                break;
        }
    }

    public void setFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();

        // set the toolbar title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    public void hideItem(){
        ArrayList<String> menus = session.getMenus();
        for(int i = 0; i < menus.size(); i++){
            if(menus.get(i).toLowerCase().equals("registration")){
                btnRegister.setVisibility(View.VISIBLE);
            }
            if(menus.get(i).toLowerCase().equals("ppinformation")){
                btnPPStatus.setVisibility(View.VISIBLE);
            }
            if(menus.get(i).toLowerCase().equals("checker")){
                btnChecker.setVisibility(View.VISIBLE);
            }
            if(menus.get(i).toLowerCase().equals("preferredunitselection")){
                btnDiagrammatic.setVisibility(View.VISIBLE);
            }
            if(menus.get(i).toLowerCase().equals("signature")){
                btnSignature.setVisibility(View.VISIBLE);
            }
        }

    }
}
