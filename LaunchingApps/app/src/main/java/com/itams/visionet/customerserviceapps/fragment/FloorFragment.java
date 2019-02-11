package com.itams.visionet.customerserviceapps.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.adapter.FloorAdapter;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.floorRealmHelper;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.floorModel;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.rest.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Administrator on 24/11/2016.
 */

public class FloorFragment extends Fragment {
    Realm realm;

    private floorRealmHelper floorHelper;
    private roomRealmHelper roomHelper;
    private ArrayList<floorModel> floorModels = new ArrayList<>();
    private FloorAdapter mAdapter;
    private ApiClient api;
    private String floorDatarequest;
    private ArrayList<String> savedPreferredUnit = new ArrayList<>();
    private String savedPreferredUnitRequest;
    private SessionManagement session;
    floorModel floormodel;
    String cluster_id;
    String project_id;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    public FloorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_main, container, false);
        ButterKnife.bind(this, view);
        Log.i("floorfragment", "floorfragment");
        realm = Realm.getDefaultInstance();
        floorHelper = new floorRealmHelper(getActivity(), realm);
        roomHelper = new roomRealmHelper(getActivity(), realm);
        session = new SessionManagement(getActivity());
        api = new ApiClient();
        progressDialog = new ProgressDialog(getContext());

        //update 0212 : Mengambil data floor sesuai cluster_id
        Bundle bundle = this.getArguments();
        cluster_id = null;
        project_id = null;
        if (bundle != null) {
            cluster_id = bundle.getString("cluster_id");
            project_id = bundle.getString("project_id");
        }
        Log.i("floorfragment", "cluster id" + cluster_id);
        Log.i("floorfragment", "project_id id" + project_id);

        AsyncCallWS task = new AsyncCallWS();
        task.execute();

        Log.d("floorFragment", cluster_id + "");
//        mAdapter = new FloorAdapter(getActivity(), floorModels, roomHelper);

        return view;
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("getprojectrequeest", "onPreExecute");
            progressDialog.setMessage("Getting data");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
            floorDatarequest = api.getInfoTower(project_id, cluster_id,getContext());
            savedPreferredUnitRequest = api.getDataPP(session.getPPNo(), project_id,getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            if (floorDatarequest != null && savedPreferredUnitRequest != null) {
                storefloortorealm();
                parseDataSavedPrefUnit();
                initrecycleview();
            } else {
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getActivity());
                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                //kotakBuilder.setTitle("Error");
                kotakBuilder.setMessage("Please check your internet connection");
                kotakBuilder.setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().onBackPressed();
                    }
                });
                kotakBuilder.create().show();
            }

            progressDialog.dismiss();

        }

    }

    private void initrecycleview() {

        floorModels.clear();
        floorModels.addAll(floorHelper.getFloor(cluster_id));
        mAdapter = new FloorAdapter(getActivity(), floorModels, savedPreferredUnit, roomHelper);
        mAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.scrollToPosition(floorModels.size());
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setAdapter(mAdapter);

        Log.i("changsize", String.valueOf(floorModels.size()));
        Log.i("changsize", String.valueOf(floorModels.size()));
    }

    private void parseDataSavedPrefUnit() {
        Log.i("GetSavedPref", "true");
        if (!savedPreferredUnitRequest.isEmpty()) {
            roomHelper.clearSelectedRoomprefunit();
            try {
                JSONObject objPrefUnit = new JSONObject(savedPreferredUnitRequest);
                int code = objPrefUnit.getInt("code");
                if (code == 1) {
                    JSONArray arrayroom = objPrefUnit.getJSONArray("preferredUnit");
                    for (int i = 0; i < arrayroom.length(); i++) {
                        JSONObject itemroom = arrayroom.getJSONObject(i);
                        roomModel roomModel = new roomModel();
                        roomModel.setUnitCode(itemroom.getString("UnitCode"));
                        roomModel.setUnitNo(itemroom.getString("UnitNo"));
                        roomModel.setOrder(itemroom.getString("orderNo"));
                        //roomModel.setSelected(roomHelper.getRoomByUnitNo(itemroom.getString("UnitNo")).isSelected());
                        //roomModel.setSelected(false);
//                    if(itemroom.getString("UnitStatus") != "S"){
//                        roomModel.setSelected( true);
//                    }else {
//                        roomModel.setSelected(false);
//                    }
                        roomHelper.addSelectedRoom(roomModel);
                        savedPreferredUnit.add(itemroom.getString("UnitNo"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void storefloortorealm() {
        try {
            JSONObject resultfloor = new JSONObject(floorDatarequest);
            String status = resultfloor.getString("code");
            if (status == "1") {
                JSONArray datafloor = resultfloor.getJSONArray("towerData");

                for (int i = 0; i < datafloor.length(); i++) {

                    floormodel = new floorModel();
                    JSONObject itemfloor = datafloor.getJSONObject(i);

                    floormodel.setCluster_id(cluster_id);
                    floormodel.setProject_id(project_id);
                    floormodel.setFloor_id(itemfloor.getString("floor"));
                    floormodel.setTotal_unit(itemfloor.getString("totalUnit"));
                    floormodel.setTotal_sold(itemfloor.getString("totalUnitSold"));
                    floormodel.setPercentageUnitSold(itemfloor.getString("percentageUnitSold"));

                    floorHelper.addFloor(floormodel);
                }
            }

        } catch (Exception l) {

        }
    }
}
