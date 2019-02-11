package com.itams.visionet.customerserviceapps.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.diagramaticRealmHelper;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.diagramaticClusterModel;
import com.itams.visionet.customerserviceapps.model.diagramaticProjectModel;
import com.itams.visionet.customerserviceapps.rest.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Administrator on 24/11/2016.
 */

public class DiagramaticFragment extends Fragment {
    Realm realm;
    SessionManagement session;

    private ApiClient api;
    String testgetproject;
    diagramaticRealmHelper diagramatrichelper;
    roomRealmHelper roomHelper;
    ArrayAdapter<diagramaticProjectModel> projectAdapter;
    List<diagramaticProjectModel> projectlist;
    List<diagramaticClusterModel> clusterlistReq;
    private Boolean onokError = false;

    ProgressDialog progress;

    private ArrayList<diagramaticClusterModel> listDiagramatic = new ArrayList<>();
    diagramaticRealmHelper diagramaticHelper;
    ArrayAdapter adapter;

    @BindView(R.id.spinner_cluster)
    Spinner spinnerCluster;
    @BindView(R.id.spinner_project)
    Spinner spinnerProject;
    @BindView(R.id.button_submit)
    Button btnSubmit;

    //update 0212 : Menampilkan daftar spinner cluster sesuai project yang dipilih
    List<diagramaticClusterModel> cluster = new ArrayList<>();
    ArrayAdapter<diagramaticClusterModel> clusterAdapter;

    public DiagramaticFragment() {
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
        View view = inflater.inflate(R.layout.fragment_diagrammatic, container, false);
        ButterKnife.bind(this, view);

        api = new ApiClient();
        clusterlistReq = new ArrayList<>();
        session = new SessionManagement(getActivity());

        Log.i("PPNo", session.getPPNo());

            AsyncCallWS task = new AsyncCallWS();
            task.execute();

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/Lato-Light.ttf");
            btnSubmit.setTypeface(font);

            realm = Realm.getDefaultInstance();
            diagramaticHelper = new diagramaticRealmHelper(getActivity(), realm);
            roomHelper = new roomRealmHelper(getActivity(), realm);

            initSpinner();

        return view;
    }

    private void initSpinner() {
        projectlist = new ArrayList<>();
//        projectlist.addAll(diagramaticHelper.getProject());
        projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, projectlist);
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProject.setAdapter(projectAdapter);

//        //Log.d("cluster" , diagramaticHelper.getCluster(project.get(1).getProject_id()).get(0).toString());


        clusterAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cluster);
        clusterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCluster.setAdapter(clusterAdapter);

        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diagramaticProjectModel project = (diagramaticProjectModel) spinnerProject.getSelectedItem();
                cluster.clear();
//                Log.d("cluster" , diagramaticHelper.getCluster(project.getProject_id()).get(0).toString());
                cluster.addAll(diagramaticHelper.getCluster(project.getProject_id()));
                clusterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.button_submit)
    public void onClick() {
        roomHelper.deleteallroom();

        Fragment fragment = null;
        String title = "Diagrammatic";
             fragment = new ScannerFragment();


        diagramaticClusterModel cluster = (diagramaticClusterModel) spinnerCluster.getSelectedItem();
        diagramaticProjectModel project = (diagramaticProjectModel) spinnerProject.getSelectedItem();
        Bundle bundle = new Bundle();
        bundle.putString("cluster_id", cluster.getCluster_id());
        bundle.putString("project_id", project.getProject_id());

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment,title);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void storetorealm() {
        try {
            JSONArray project = new JSONArray(testgetproject);
            for(int i = 0 ;i < project.length();i++){
                diagramaticProjectModel projectmodel = new diagramaticProjectModel();
                JSONObject itemproject = project.getJSONObject(i);
                projectmodel.setProject_id(itemproject.getString("ProjectCode"));
                projectmodel.setProject(itemproject.getString("ProjectName"));
                diagramaticHelper.addProject(projectmodel);
            }
//
        } catch (JSONException e) {
            e.printStackTrace();
        }

        projectlist.addAll(diagramaticHelper.getProject());
        projectAdapter.notifyDataSetChanged();
    }

    private void storeClustertorealm() {
        for(int h = 0 ; h< clusterlistReq.size();h++){
            diagramaticClusterModel clusterModelitem = clusterlistReq.get(h);;
            diagramaticHelper.addCluster(clusterModelitem);
        }
        cluster.clear();
        cluster.addAll(diagramaticHelper.getCluster(projectlist.get(0).getProject_id()));
        clusterAdapter.notifyDataSetChanged();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("getprojectrequeest", "onPreExecute");
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
            testgetproject = api.getProject(getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");

            if(testgetproject != null){
                storetorealm();

                AsyncCallCluster task = new AsyncCallCluster();
                task.execute();
            }else{
                progress.dismiss();
                showDialog();
            }
        }
    }

    private class AsyncCallCluster extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("getprojectrequeest", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {

            for(int k = 0; k <projectlist.size(); k++ ){
                diagramaticProjectModel tes = projectlist.get(k);
                String codePrject = tes.getProject_id();
                String getCluster =  api.getCluster(codePrject,getContext());
                getDatacluster(getCluster,codePrject);
//                Log.i("getCluster", getCluster);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            progress.dismiss();
            if(onokError){
                showDialog();
            }else{
                storeClustertorealm();
            }
        }
    }

    private void getDatacluster(String getCluster, String codePrject) {
        if(getCluster != null){
            try{
                JSONArray clusterarray = new JSONArray(getCluster);
                for(int l = 0;l < clusterarray.length();l++) {
                    diagramaticClusterModel cluserModel = new diagramaticClusterModel();
                    JSONObject objcluster = clusterarray.getJSONObject(l);
                    cluserModel.setProject_id(codePrject);
                    cluserModel.setCluster_id(objcluster.getString("ClusterCode"));
                    cluserModel.setCluster(objcluster.getString("ClusterName"));
                    clusterlistReq.add(cluserModel);
                }
            }
            catch (Exception e){
                Log.e("errorparseTomodel",e.getMessage());
            }
        }else{
            onokError = true;
        }

    }

    public void setFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();

        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    public void showDialog(){
        AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
        kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        //kotakBuilder.setTitle("Error");
        kotakBuilder.setMessage("Please check your internet connection");
        if(!((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().equals("Diagrammatic"))
            kotakBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
        else
            kotakBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    setFragment(new HomeFragment(), "Launching Apps");
                }
            });
        kotakBuilder.create().show();
    }
}
