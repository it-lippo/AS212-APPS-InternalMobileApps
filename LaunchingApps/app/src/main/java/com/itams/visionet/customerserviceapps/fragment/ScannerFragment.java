package com.itams.visionet.customerserviceapps.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.model.priorityPassModel;
import com.itams.visionet.customerserviceapps.rest.ApiClient;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ScannerFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CAMERA = 200;
    private static final String TAG = "Scanner";

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.cameraPreview)
    CompoundBarcodeView cameraPreview;
    @BindView(R.id.btnCheck)
    Button btnCheck;
    @BindView(R.id.btnPriorityPass)
    LinearLayout btnPriorityPass;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvRegisterTime)
    TextView tvRegisterTime;
    @BindView(R.id.tvDealingTime)
    TextView tvDealingTime;
    @BindView(R.id.expandablePP)
    RelativeLayout expandablePP;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPSCode)
    TextView tvPSCode;
    @BindView(R.id.imageViewKTP)
    ImageView imageViewKTP;
    @BindView(R.id.imageViewNPWP)
    ImageView imageViewNPWP;
    @BindView(R.id.tvMemberName)
    TextView tvMemberName;
    @BindView(R.id.tvMemberCode)
    TextView tvMemberCode;
    @BindView(R.id.btnCustomer)
    LinearLayout btnCustomer;
    @BindView(R.id.expandableCust)
    RelativeLayout expandableCust;
    @BindView(R.id.btnMember)
    LinearLayout btnMember;
    @BindView(R.id.expandableMember)
    RelativeLayout expandableMember;
    @BindView(R.id.btnOtherPP)
    LinearLayout btnOtherPP;
    @BindView(R.id.expandableOther)
    RelativeLayout expandableOther;
    @BindView(R.id.tvRelatedPP)
    TextView tvRelatedPP;
    @BindView(R.id.tvRelatedPPTitle)
    TextView tvRelatedPPTitle;
    @BindView(R.id.hiasan)
    FrameLayout hiasan;
    @BindView(R.id.scView)
    ScrollView scView;
    @BindView(R.id.tvNumberSchemeCode)
    TextView tvNumberSchemeCode;
    @BindView(R.id.tvNumberSchemeName)
    TextView tvNumberSchemeName;
    @BindView(R.id.tvNameUnitCodeBooked)
    TextView tvNameUnitCodeBooked;
    @BindView(R.id.tvNameUnitNoBooked)
    TextView tvNameUnitNoBooked;
    @BindView(R.id.FLImageFull)
    FrameLayout FLImageFull;
    @BindView(R.id.imageViewFull)
    ImageView imageViewFull;
    @BindView(R.id.LLMain)
    LinearLayout LLMain;
    @BindView(R.id.btnFlash)
    Button btnFlash;

    SessionManagement session;

    private String APIResult;
    private String isRegistered;
    private String isChecked;
    private ApiClient api;
    private String cluster_id;
    private String project_id;
    private Bundle bundle;
    private priorityPassModel PPModel;
    private String PPno ;

    ProgressDialog progress;

    //update 2501 : Pinch zoom
    private PhotoViewAttacher mAttacher;
    public View view;

    public ScannerFragment() {
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
        view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, view);
        api = new ApiClient();
        session = new SessionManagement(getActivity());
        if (((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString().contains("Diagrammatic")) {

            bundle = this.getArguments();
            cluster_id = null;
            project_id = null;
            if (bundle != null) {
                cluster_id = bundle.getString("cluster_id");
                project_id = bundle.getString("project_id");
//                session.setProjectCode(project_id);

            }
        }

        //update 1001 : Jika projectCode belum pernah dipilih masuk ke menu setting terlebih dahulu
        if (session.getProjectCode().isEmpty() || session.getProjectCode().equals("")) {
            setFragment(new SettingFragment(), "Setting");
        } else {
            //update 1412 : Checking permission untuk pengguna marshmallow
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                initControls();
            }
        }

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_ENTER:
                            if (!editText.getText().toString().equals("")) {
                                String PPNo = editText.getText().toString();

                                callWS(PPNo, session.getProjectCode());
                            }
                            Log.d("blog", "true");
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void refreshScanner() {
        cameraPreview.resume();
        //setFragment(new ScannerFragment(), ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString());
    }

    private void initControls() {
        cameraPreview.decodeContinuous(callback);
        cameraPreview.setStatusText(null);
        cameraPreview.setMinimumHeight(100);
        cameraPreview.setMinimumWidth(100);

        if(!hasFlash())
            btnFlash.setVisibility(View.GONE);

        if(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString().contains("Diagrammatic"))
            mAttacher = new PhotoViewAttacher(imageViewFull);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                //barcodeView.setStatusText(result.getText());
                cameraPreview.pause();
                editText.setText(result.getText());
//                PPno = result.getText();
                callWS(result.getText(), session.getProjectCode());
            }

            //Do something with code result
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        cameraPreview.resume();
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    backPressed();

                    return true;

                }

                return false;
            }
        });
    }

    public void backPressed(){
        if (FLImageFull.getVisibility() == View.VISIBLE) {
            FLImageFull.setVisibility(View.GONE);
            LLMain.setVisibility(View.VISIBLE);
        } else {
            getActivity().onBackPressed();
        }
    }

    @OnClick({R.id.btnCheck, R.id.imageViewKTP, R.id.imageViewNPWP, R.id.btnFlash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:
                if (!editText.getText().toString().equals("")) {
                    String PPNo = editText.getText().toString();
                    ArrayList array = new ArrayList<String>();
                    array.add(PPNo);
                    array.add(session.getProjectCode());
                    if (btnCheck.getText().equals("Register")) {
                        registrationWS registerWS = new registrationWS();
                        registerWS.execute(array);
                    } else
                        callWS(PPNo, session.getProjectCode());
                }
                break;
            case R.id.imageViewKTP:
                //if(imageViewKTP.getDrawingCache() != null){
                LLMain.setVisibility(View.GONE);
                FLImageFull.setVisibility(View.VISIBLE);
                Glide.with(this).load(PPModel.getKTPImgUrl()).into(imageViewFull);
                //}
                Log.i("clickImage", "True");
                break;
            case R.id.imageViewNPWP:
                LLMain.setVisibility(View.GONE);
                FLImageFull.setVisibility(View.VISIBLE);
                Glide.with(this).load(PPModel.getNPWPImgUrl()).into(imageViewFull);
                break;
            case R.id.btnFlash:
                String text = btnFlash.getText().toString();
                if(text.equals("Turn On Flash")){
                    cameraPreview.setTorchOn();
                    btnFlash.setText("Turn Off Flash");
                }
                else{
                    cameraPreview.setTorchOff();
                    btnFlash.setText("Turn On Flash");
                }
                break;
        }
    }

    @Override
    public void onPause() {
        cameraPreview.pause();
        super.onPause();
        //releaseCamera();
        //mCamera.setPreviewCallback(null);
    }

    private boolean hasFlash() {
        return getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void setText(priorityPassModel ppModel) {
        cameraPreview.setVisibility(View.GONE);
        if (!((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().equals("Registration")) {
            btnCheck.setVisibility(View.GONE);
        } else
            btnCheck.setText("Register");

        editText.setEnabled(false);
        hiasan.setVisibility(View.VISIBLE);
        scView.setVisibility(View.VISIBLE);


        if (ppModel != null) {
            if (ppModel.getPPStatus() != null)
                tvStatus.setText("" + ppModel.getPPStatus());
            if (ppModel.getDealingTime() != null)
                tvDealingTime.setText(ppModel.getDealingTime());
            if (ppModel.getScmCode() != null)
                tvNumberSchemeCode.setText(ppModel.getScmCode());
            if (ppModel.getScmName() != null)
                tvNumberSchemeName.setText(ppModel.getScmName());
            if (ppModel.getUnitCodeBooked() != null)
                tvNameUnitCodeBooked.setText(ppModel.getUnitCodeBooked());
            if (ppModel.getPPStatus() != null)
                tvNameUnitNoBooked.setText(ppModel.getUnitNoBooked());
            if (ppModel.getMemberName() != null)
                tvName.setText(ppModel.getMemberName());
            if (ppModel.getPsCode() != null)
                tvPSCode.setText(ppModel.getPsCode());
            if (ppModel.getMemberName() != null)
                tvMemberName.setText(ppModel.getMemberName());
            if (ppModel.getMemberCode() != null)
                tvMemberCode.setText(ppModel.getMemberCode());

            if (ppModel.getRelatedPP() != null) {

                String RelatedPP = "", RelatedPPTitle = "";
                for (int i = 0; i < ppModel.getRelatedPP().size(); i++) {
                    RelatedPPTitle += "PP " + (i + 1) + "\n";
                    RelatedPP += ppModel.getRelatedPP().get(i) + "\n";
                }
                tvRelatedPPTitle.setText(RelatedPPTitle);
                tvRelatedPP.setText(RelatedPP);
            }


            if (ppModel.getKTPImgUrl() != null && !ppModel.getKTPImgUrl().isEmpty())
                Glide.with(this).load(ppModel.getKTPImgUrl()).into(imageViewKTP);
            if (ppModel.getNPWPImgUrl() != null && !ppModel.getNPWPImgUrl().isEmpty())
                Glide.with(this).load(ppModel.getNPWPImgUrl()).into(imageViewNPWP);
        }

        //Log.i("Glide", ppModel.getKTPImgUrl());
    }

    private class registrationWS extends AsyncTask<ArrayList<String>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            Log.i(TAG, "doInBackground");

            isRegistered = api.doRegisterPP(arrayLists[0].get(0), arrayLists[0].get(1),getContext());
            APIResult = arrayLists[0].get(0);

            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();

            //if(isRegistered != null && APIResult != null){
            if (isRegistered != null) {
                try {
                    JSONObject messageObject = new JSONObject(isRegistered);

                    showDialog(messageObject.getString("message"));

                    refreshScanner();

                    if (messageObject.getInt("code") == 1)
                        session.setPPNo(APIResult);


                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(e.getMessage());
                    refreshScanner();
                }
            } else {
                showDialog("Please check your internet connection");
                refreshScanner();
            }
        }
    }

//    private class registration_diagramWS extends AsyncTask<ArrayList<String>, Void, Void> {
//        @Override
//        protected Void doInBackground(ArrayList<String>... arrayLists) {
//            Log.i(TAG, "doInBackground");
//
//            isRegistered = api.doRegisterPP(arrayLists[0].get(0), arrayLists[0].get(1));
//            APIResult = arrayLists[0].get(0);
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.i(TAG, "onPreExecute");
//            progress = ProgressDialog.show(getActivity(), null,
//                    "Getting Data...", true);
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            Log.i(TAG, "onPostExecute");
//
//            progress.dismiss();
//
//            //if(isRegistered != null && APIResult != null){
//            if (isRegistered != null) {
//                try {
//                    JSONObject messageObject = new JSONObject(isRegistered);
//
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                    alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                    alertBuilder.setTitle(null);
//
//
//                    refreshScanner();
//
//                    if (messageObject.getInt("code") == 1) {
//                        session.setPPNo(APIResult);
//                        Fragment fragment;
//                        String title;
//
//                        alertBuilder.setMessage(messageObject.getString("message"));
//                        alertBuilder.setPositiveButton("close", null);
//                        alertBuilder.create().show();
//
//                        if (!((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().equals("Diagrammatic 2")) {
//                            title = "Cluster";
//                            fragment = new Diagram2Fragment();
//                        }
//                        else{
//                            fragment = new FloorFragment();
//                            title = "Diagrammatic";
//                        }
//
//                        fragment.setArguments(bundle);
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.containerView, fragment, title);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
//                    kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                    //kotakBuilder.setTitle("Error");
//                    kotakBuilder.setMessage(e.getMessage());
//                    kotakBuilder.setPositiveButton("close", null);
//                    kotakBuilder.create().show();
//                    refreshScanner();
//                }
//            } else {
//                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
//                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//                //kotakBuilder.setTitle("Error");
//                kotakBuilder.setMessage("Please check your internet connection");
//                kotakBuilder.setPositiveButton("close", null);
//                kotakBuilder.create().show();
//                refreshScanner();
//            }
//        }
//    }

    private class ppStatusWS extends AsyncTask<ArrayList<String>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            Log.i(TAG, "doInBackground");

            APIResult = api.getDataPP(arrayLists[0].get(0), arrayLists[0].get(1),getContext());

            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();

            if (APIResult != null) {
                try {
                    JSONObject resultObject = new JSONObject(APIResult);

                    if (resultObject.getInt("code") == 0) {
                        showDialog(resultObject.getString("message"));

                        refreshScanner();
                    } else {
                        Gson gson = new Gson();
                        priorityPassModel ppModel = gson.fromJson(String.valueOf(resultObject.get("pp")), priorityPassModel.class);
                        setText(ppModel);
                        PPModel = ppModel;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(e.getMessage());
                    refreshScanner();
                }
            } else {
                showDialog("Please check your internet connection");
                refreshScanner();
            }
        }
    }

    private class ppStatusDiagramWS extends AsyncTask<ArrayList<String>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            Log.i(TAG, "doInBackground");

            APIResult = api.getDataPP(arrayLists[0].get(0), arrayLists[0].get(1),getContext());

            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Log.i("apiresult ppstatus", APIResult);

            progress.dismiss();

            if (APIResult != null) {
                try {
                    JSONObject resultObject = new JSONObject(APIResult);
                    if (resultObject.getInt("code") == 0) {
                        showDialog("PP " + editText.getText().toString() +" not found");
                        refreshScanner();
                    } else {
                        Gson gson = new Gson();
                        priorityPassModel ppModel = gson.fromJson(String.valueOf(resultObject.get("pp")), priorityPassModel.class);
                        JSONArray pp = resultObject.getJSONArray("preferredUnit");

                        if (ppModel.getAllowedPrefUnit() == 0) {
                            showDialog("PP " + editText.getText().toString() +" not found");
                        } else {
                            int getAllowedPrefUnit = ppModel.getAllowedPrefUnit();
                            if(pp != null)
                                getAllowedPrefUnit -= pp.length();

                            Log.i("PPLenght ", pp.length() + " allowed : " + ppModel.getAllowedPrefUnit());
//                            showDialog("You have " + getAllowedPrefUnit + " units to book");
                            session.setAllowedPrefUnit(ppModel.getAllowedPrefUnit());
                            session.setPPNo(ppModel.getPPNo());
//                            setText(ppModel);
                            gotodiagram();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(e.getMessage());
                    refreshScanner();
                }
            } else {
                showDialog("Please check your internet connection");
                refreshScanner();
            }
        }
    }

    private void gotodiagram() {
        Fragment fragment;
        String title;
        if (!((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().equals("Diagrammatic 2")) {
            title = "Cluster";
            fragment = new Diagram2Fragment();
        } else {
            fragment = new FloorFragment();
            title = "Diagrammatic";
        }

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private class checkerWS extends AsyncTask<ArrayList<String>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            Log.i(TAG, "doInBackground");

            isChecked = api.doCheckedPP(arrayLists[0].get(0), arrayLists[0].get(1),getContext());

            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();

            //if(isChecked != null && APIResult !=null){
            if (isChecked != null) {
                try {
                    JSONObject messageObject = new JSONObject(isChecked);

                    showDialog(messageObject.getString("message"));

                    refreshScanner();

                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(e.getMessage());
                    refreshScanner();
                }
            } else {
                showDialog("Please check your internet connection");
                refreshScanner();
            }

        }

    }

    private void callWS(String PPNo, String ProjectCode) {
        String Title = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();
        ArrayList<String> input = new ArrayList<>();
        input.add(PPNo);

        if (Title.contains("Diagrammatic")) {
            input.add(project_id);
            ppStatusDiagramWS task = new ppStatusDiagramWS();
            task.execute(input);
        }

        input.add(ProjectCode);

        if (Title.equals("Registration")) {
            ppStatusWS task = new ppStatusWS();
            task.execute(input);
        } else if (Title.equals("PP Status")) {
            ppStatusWS task = new ppStatusWS();
            task.execute(input);
        } else if (Title.equals("Checker")) {
            checkerWS task = new checkerWS();
            task.execute(input);
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

    public void showDialog(String Message){
        AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
        kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        kotakBuilder.setMessage(Message);
        kotakBuilder.setPositiveButton("close", null);
        kotakBuilder.create().show();
    }


}
