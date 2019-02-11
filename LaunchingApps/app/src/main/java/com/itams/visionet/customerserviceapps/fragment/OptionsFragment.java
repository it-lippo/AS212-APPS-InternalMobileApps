package com.itams.visionet.customerserviceapps.fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionsFragment extends Fragment {

    @BindView(R.id.btnProject)
    LinearLayout btnProject;
    @BindView(R.id.btnService)
    LinearLayout btnService;

    public OptionsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btnProject, R.id.btnService})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProject:
                setFragment(new SettingFragment(), "Setting");
                break;
            case R.id.btnService:
                setFragment(new ChangeLinkSettingFragment(), "Options");
                break;
        }
    }

    public void setFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }
}
