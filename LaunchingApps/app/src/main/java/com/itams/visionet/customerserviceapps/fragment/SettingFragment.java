package com.itams.visionet.customerserviceapps.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.MainActivity;
import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.diagramaticRealmHelper;
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

public class SettingFragment extends Fragment {
    ProgressDialog progress;
    @BindView(R.id.spinner_project)
    Spinner spinnerProject;
    @BindView(R.id.btnSave)
    Button btnSave;

    private String testgetproject;
    private ApiClient api;
    private Realm realm;
    private SessionManagement session;
    private diagramaticRealmHelper diagramaticHelper;
    ArrayAdapter<diagramaticProjectModel> projectAdapter;
    List<diagramaticProjectModel> projectlist;

    public SettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        //inisialisasi variable
        api = new ApiClient();
        realm = Realm.getDefaultInstance();
        diagramaticHelper = new diagramaticRealmHelper(getActivity(), realm);
        session = new SessionManagement(getActivity());

        AsyncCallWS task = new AsyncCallWS();
        task.execute();

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
    }

    private void storetorealm() {
        try {
            JSONArray project = new JSONArray(testgetproject);
            for (int i = 0; i < project.length(); i++) {
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

        projectlist.clear();
        projectlist.addAll(diagramaticHelper.getProject());
        projectAdapter.notifyDataSetChanged();
        if(!session.getProjectCode().equals("")){
            int i = projectlist.indexOf(diagramaticHelper.getProjectByCode(session.getProjectCode()));
            spinnerProject.setSelection(i);
            Log.i("Spinner", projectlist.size() + " : " + i);
        }
    }

    @OnClick(R.id.btnSave)
    public void onClick() {
        diagramaticProjectModel project = (diagramaticProjectModel) spinnerProject.getSelectedItem();
        session.setProjectCode(project.getProject_id());
        Toast.makeText(getActivity(), "Project sucessfully saved", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
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
            progress.dismiss();
            if(testgetproject != null)
                storetorealm();
            else{
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                //kotakBuilder.setTitle("Error");
                kotakBuilder.setMessage("Please check your internet connection");
                kotakBuilder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();


//                        setFragment(new HomeFragment(), "Launching Apps");
                    }
                });
                kotakBuilder.create().show();
            }
        }
    }

    public void setFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, title).addToBackStack(null);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
//        fragmentTransaction.dis

        // set the toolbar title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }
}
