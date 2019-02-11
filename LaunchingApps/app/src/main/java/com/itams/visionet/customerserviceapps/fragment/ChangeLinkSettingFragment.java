package com.itams.visionet.customerserviceapps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.activity.NetworkActivity;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.webkit.URLUtil.isValidUrl;

public class ChangeLinkSettingFragment extends Fragment {
    @BindView(R.id.inputIntranet)
    EditText inputIntranet;
    @BindView(R.id.inputInternet)
    EditText inputInternet;
    @BindView(R.id.btnSave)
    Button btnSave;

    SessionManagement sessionManagement;

    public ChangeLinkSettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_change_link_setting, container, false);
        ButterKnife.bind(this, view);
        sessionManagement = new SessionManagement(getContext());

        inputInternet.setText(sessionManagement.getinternet());
        inputIntranet.setText(sessionManagement.getIntranet());

        return view;
    }

    @OnClick(R.id.btnSave)
    public void onClick() {
        savelink();

    }

    private void savelink() {

        if(correctioneditext(inputInternet) && correctioneditext(inputIntranet)){
//            set to Sharepref
            sessionManagement.setLink(inputIntranet.getText().toString().trim(),inputInternet.getText().toString().trim());
            Toast.makeText(getContext(),"Link Has Been Saved",Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }

    }

    public boolean correctioneditext(EditText editext) {
        if(editext.getText().toString() == "" || editext.getText().toString() == null || editext.getText().toString().isEmpty()){
            editext.setError("This field is required");
            return  false;
        }else {
            if (!isValidUrl(editext.getText().toString())){
                Log.i("check input url ", String.valueOf(isValidUrl(editext.getText().toString())));
                editext.setError("URL Format is required ");
                return false;
            }else {
                return true;
            }
        }
    }
}
