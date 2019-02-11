package com.itams.visionet.customerserviceapps.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.R;
import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureFragment extends Fragment {

    private static final int SIGN_PERMISSIONS_REQUEST_STORAGE = 204;
    @BindView(R.id.signView)
    SignatureView signView;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnClear)
    Button btnClear;

    public SignatureFragment() {
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
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        ButterKnife.bind(this, view);

        //update 1412 : Checking permission untuk pengguna marshmallow
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SIGN_PERMISSIONS_REQUEST_STORAGE);
            disabledButton();
        }

        return view;
    }

    @OnClick({R.id.btnClear, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear: signView.clearCanvas();
                break;
            case R.id.btnSave:
                Bitmap bmp = signView.getSignatureBitmap();

                SaveImage(bmp);
                break;
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getActivity(), "Signature Saved !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disabledButton(){
        btnSave.setEnabled(false);
    }
}
