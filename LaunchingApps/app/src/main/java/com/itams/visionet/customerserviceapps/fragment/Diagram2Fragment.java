package com.itams.visionet.customerserviceapps.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.adapter.Digram2matic;
import com.itams.visionet.customerserviceapps.adapter.FloorAdapter;
import com.itams.visionet.customerserviceapps.adapter.MyLinearLayoutManager;
import com.itams.visionet.customerserviceapps.adapter.RoomAdapter;
import com.itams.visionet.customerserviceapps.adapter.TopAdapter;
import com.itams.visionet.customerserviceapps.core.Infinit_Scroll;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.floorRealmHelper;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.floorModel;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.model.selectedPrefUnitModel;
import com.itams.visionet.customerserviceapps.rest.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Administrator on 14/12/2016.
 */

public class Diagram2Fragment extends Fragment {

    private JSONArray selectedRoom = new JSONArray();

    Realm realm;
    SessionManagement session;

    private Digram2matic digram2matic;
    private TopAdapter topAdapter;
    private int itemvisible = 5;
    private int itemonloadscroll = 0;
    private int limit = 5;
    //    floor
    private floorRealmHelper floorHelper;
    private roomRealmHelper roomHelper;
    private ArrayList<floorModel> floorModels = new ArrayList<>();
    private ArrayList<roomModel> savedPreferredUnit = new ArrayList<>();
    private ArrayList<selectedPrefUnitModel> savedPreferredUnitDB = new ArrayList<>();
    private ArrayList<ArrayList<roomModel>> dataFloorRoom = new ArrayList<>();
    private FloorAdapter flooradapter;
    private ApiClient api;
    private String floorDatarequest;
    private String bookingRoom;
    private String savedPreferredUnitRequest;
    private Boolean onokError = false;
    floorModel floormodel;
    String cluster_id;
    String project_id;
    ProgressDialog progress;
    private int code_button;


    //    room
    private ArrayList<com.itams.visionet.customerserviceapps.model.roomModel> roomModels = new ArrayList<>();
//    private ArrayList<String> selectedRoom = new ArrayList<>();

    private RoomAdapter roomAdapter;
    String resultRoom;
    String floor_id;
    roomModel roomModel;


    @BindView(R.id.textinfo)
    TextView textinfo;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btnPesan)
    Button btnPesan;
    @BindView(R.id.btnBeli)
    Button btnBeli;
    @BindView(R.id.LLButton)
    LinearLayout LLButton;
    @BindView(R.id.recyclerview_main)
    RecyclerView recyclerView;

    public Diagram2Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout, container, false);
        ButterKnife.bind(this, view);
//        realm
        realm = Realm.getDefaultInstance();
        floorHelper = new floorRealmHelper(getActivity(), realm);
        roomHelper = new roomRealmHelper(getActivity(), realm);
//        api
        api = new ApiClient();
        session = new SessionManagement(getActivity());

        Bundle bundle = this.getArguments();
        cluster_id = null;
        project_id = null;
        if (bundle != null) {
            cluster_id = bundle.getString("cluster_id");
            project_id = bundle.getString("project_id");
        }


//        Toast.makeText(getContext(), session.getAllowedPrefUnit(), Toast.LENGTH_LONG).show();


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cluster");


//get APi soap
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

        initrecycleview();

        return view;
    }

    private void initrecycleview() {

        final MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        digram2matic = new Digram2matic(getContext(), floorModels, dataFloorRoom, savedPreferredUnit, realm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(digram2matic);
//        recyclerView.addOnScrollListener(new Infinit_Scroll(layoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//
//                itemonloadscroll = itemvisible;
//                itemvisible = itemvisible + 5;
//                Log.i("size floororoommodel", String.valueOf(dataFloorRoom.size()));
//                Log.d("itemonloadscroll", String.valueOf(itemonloadscroll));
//                Log.d("itemvisible", String.valueOf(itemvisible));
//
//                if (floorModels.size() < itemvisible) {
//                    if (limit == visibleThreshold) {
//                        if (floorModels.size() > itemonloadscroll) {
//                            itemvisible = floorModels.size();
//                            limit++;
//                        }
//                    }
//                }
//                if (floorModels.size() > itemonloadscroll) {
//                    roomcallaws roomcallaws = new roomcallaws();
//                    roomcallaws.execute();
////                    if (roomcallaws.getStatus() == AsyncTask.Status.FINISHED) {
////                        digram2matic.notifyDataSetChanged();
////                        Log.i("room calla aws ","selesai");
//////                        Log.i("diagramatic 2",digram2matic.notifyDataSetChanged());
////                    }
//                }
//            }



//        });
        int sisaroom = session.getAllowedPrefUnit() - roomHelper.getSelectedRoom().length();
        textinfo.setText(  sisaroom + " units");

    }

    private void insertDataRoom() {
//        dataFloorRoom.clear();
        for (int i = 0; i < floorModels.size(); i++) {
            dataFloorRoom.add(roomHelper.getRoom(floorModels.get(i).getFloor_id()));
        }
        Log.d("isiFloor", dataFloorRoom.size() + "");

//        if (floorModels.size() != 0) {
//            if (floorModels.size() <= 5)
//                itemvisible = floorModels.size();
//
//            for (int k = itemonloadscroll; k < itemvisible; k++) {
//                dataFloorRoom.add(roomHelper.getRoom(floorModels.get(k).getFloor_id()));
//            }
//        }

//        digram2matic.notifyDataSetChanged();
        digram2matic.notify( floorModels, dataFloorRoom);

    }


    private void insertDataRoom_data_after_refresh() {
        dataFloorRoom.clear();
        if (floorModels.size() < itemvisible) {
            for (int i = 0; i < floorModels.size(); i++) {
                dataFloorRoom.add(roomHelper.getRoom(floorModels.get(i).getFloor_id()));
            }
        }else {
            for (int i = 0; i < itemvisible; i++) {
                dataFloorRoom.add(roomHelper.getRoom(floorModels.get(i).getFloor_id()));
            }
        }

        Log.d("isiFloor", dataFloorRoom.size() + "");
//        digram2matic.notifyDataSetChanged();
    }


    @OnClick({R.id.btnPesan, R.id.btnBeli, R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPesan:
                break;
            case R.id.btnBeli: {
                code_button = 1;
                booking();
                break;
            }
            case R.id.btn_clear: {
                code_button= 0;
                clearselect();
                break;

            }
        }
    }

    private void clearselect() {
        progress = ProgressDialog.show(getActivity(), null,
                "Loading", true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                roomHelper.clearselectedroom();
//                clearselectedprevunit();
                insertDataRoom();
//                storeRoomtoreal
//
//m();
//                initrecycleview();

//                insertDataRoom();
//                textinfo.setText("you can choose " + session.getAllowedPrefUnit() + " unit left");
                progress.dismiss();
                booking();
//                digram2matic.notify( floorModels, dataFloorRoom, savedPreferredUnit,itemvisible);
//                AsyncCallWS task = new AsyncCallWS();
//                task.execute();
                Log.d("I", "Love u");
            }   }, 3000);

    }

//    private void clearselectedprevunit() {
//        savedPreferredUnitDB.clear();
//        savedPreferredUnitDB.addAll(roomHelper.getSelectedRoomArrayList());
//
//        for (int l = 0; l < roomModels.size(); l++) {
//            roomModel dataroom = roomModels.get(l);
//            if (savedPreferredUnitDB.contains(dataroom)) {
//                Log.i("removeselected", dataroom.getUnitNo());
//                roomHelper.removeSelectedRoom(dataroom);
//                dataroom.setSelected(false);
//                roomHelper.addUpdateRoom(dataroom);
//            }
//        }
//    }

    private void booking() {
        bookingWS task = new bookingWS();
        selectedRoom = roomHelper.getSelectedRoom();
        task.execute(session.getPPNo());
        initrecycleview();
    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null,
                    "Getting Data...", true);
            Log.i("getprojectrequeest", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
            Log.i("Log", project_id + " : " + cluster_id);
            floorDatarequest = api.getInfoTower(project_id, cluster_id,getContext());
            savedPreferredUnitRequest = api.getDataPP(session.getPPNo(), project_id,getContext());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            if (floorDatarequest == null && savedPreferredUnitRequest == null) {
                progress.dismiss();
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
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
            } else {
                storefloortorealm();
                parseDataSavedPrefUnit();
//            get database
                floorModels.addAll(floorHelper.getFloor(cluster_id));

//            get data room
                roomcallaws task = new roomcallaws();
                task.execute();
            }
        }
    }

    private class bookingWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.i("getprojectrequeest", "doInBackground");
            Log.i("domain", session.getDomainName() + "\\" + session.getUserName());
            bookingRoom = api.saveSelectedPreferedUnit(strings[0], project_id, selectedRoom, session.getDomainName() + "\\" + session.getUserName(),getContext());
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null,
                    "Booking room...", true);
            Log.i("getprojectrequeest", "onPreExecute");
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            progress.dismiss();
            if (bookingRoom != null) {
                Log.i("bookingRoom", bookingRoom);
                JSONObject booking = null;
                String failed = "";
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getActivity());
                try {
                    booking = new JSONObject(bookingRoom);
                    if (booking.getInt("code") == 1) {
                        JSONArray failedBooking = new JSONArray(booking.getString("failedSaveUnit"));
                        if (failedBooking.length() != 0 || failedBooking != null) {
                            for (int i = 0; i < failedBooking.length(); i++) {
                                failed += failedBooking.getJSONObject(i).getString("UnitNo") + " = ";
                                failed += failedBooking.getJSONObject(i).getString("message") + "\n";
                            }
                        }
                    }
//                Toast.makeText(getActivity(), booking.getString("message"), Toast.LENGTH_SHORT).show();
                    String succes;
                    if(code_button == 1){
                        succes = booking.getString("message");

                    }else{
                        succes = "Selected unit has been cleared";
                    }


                    kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    kotakBuilder.setMessage(failed + succes);
                    kotakBuilder.setPositiveButton("OK", null);
                    kotakBuilder.create().show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                //kotakBuilder.setTitle("Error");
                kotakBuilder.setMessage("Please check your internet connection");
                kotakBuilder.setPositiveButton("close", null);
                kotakBuilder.create().show();
            }

        }
    }

    private class roomcallaws extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("getprojectrequeest", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
////
            for (int k = 0; k < floorModels.size(); k++) {
                floorModel itemfloor = floorModels.get(k);
                String floor_room = itemfloor.getFloor_id();
                String roomrequest = api.getInfoUnitEachTower(project_id, cluster_id, floor_room,getContext());
                parsedataroom(roomrequest, project_id, cluster_id, floor_room);
            }

//            if (floorModels.size() != 0) {
//                if (floorModels.size() <= 5)
//                    itemvisible = floorModels.size();
//
//                for (int k = itemonloadscroll; k < itemvisible; k++) {
//                    Log.i("itemloadonscrool", String.valueOf(k));
//
//                    if (k < floorModels.size()) {
//                        floorModel itemfloor = floorModels.get(k);
//                        String floor_room = itemfloor.getFloor_id();
//                        String roomrequest = api.getInfoUnitEachTower(project_id, cluster_id, floor_room,getContext());
//                        parsedataroom(roomrequest, project_id, cluster_id, floor_room);
//                    }
//
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            progress.dismiss();
            if (onokError) {
                AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(getContext());
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
            } else {
                storeRoomtorealm();
                insertDataRoom();

//                initrecycleview();
//                digram2matic.notifyDataSetChanged();
            }
        }
    }




    private void parsedataroom(String roomrequest, String project_id, String cluster_id, String floor_room) {

        if (roomrequest != null) {
            try {
                JSONObject objroom = new JSONObject(roomrequest);
                int code = objroom.getInt("code");
                if (code == 1) {
                    JSONArray arrayroom = objroom.getJSONArray("UnitData");
                    for (int i = 0; i < arrayroom.length(); i++) {
                        JSONObject itemroom = arrayroom.getJSONObject(i);
                        roomModel = new roomModel();
                        roomModel.setProject_id(project_id);
                        roomModel.setCluster_id(cluster_id);
                        roomModel.setFloor_id(floor_room);
                        roomModel.setUnitCode(itemroom.getString("UnitCode"));
                        roomModel.setUnitNo(itemroom.getString("UnitNo"));
                        roomModel.setUnitStatus(itemroom.getString("UnitStatus"));
                        roomModel.setColoring(itemroom.getString("Coloring"));
                        //roomModel.setSelected(roomHelper.getRoomByUnitNo(itemroom.getString("UnitNo")).isSelected());
                        //roomModel.setSelected(false);
//                    if(itemroom.getString("UnitStatus") != "S"){
//                        roomModel.setSelected( true);
//                    }else {
//                        roomModel.setSelected(false);
//                    }
                        roomModels.add(roomModel);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            onokError = true;
        }
    }

    private void parseDataSavedPrefUnit() {
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
                        roomHelper.addSelectedRoom(roomModel);
                        savedPreferredUnit.add(roomModel);
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
            int status = resultfloor.getInt("code");
            if (status == 1) {
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

//                digram2matic.notifyDataSetChanged();
            }

        } catch (Exception l) {

        }
    }

    private void storeRoomtorealm() {
        for (int l = 0; l < roomModels.size(); l++) {
            roomModel dataroom = roomModels.get(l);
            if (savedPreferredUnit.contains(dataroom)){
                dataroom.setSelected(true);
                dataroom.setOrder(savedPreferredUnit.get(savedPreferredUnit.indexOf(dataroom)).getOrder());
            }
            Log.i("dataroomfloorid", dataroom.getFloor_id());
            roomHelper.addUpdateRoom(dataroom);
        }

        ArrayList<roomModel> tesdata = new ArrayList<>();
        tesdata.addAll(roomHelper.getRoom(floor_id));
    }

    public void changedtext(){
//        insertDataRoom_data_after_refresh();
        digram2matic.notify( floorModels, dataFloorRoom);
        digram2matic.notifyDataSetChanged();
        JSONArray countselctedroom = roomHelper.getSelectedRoom();
        int sisapilih = session.getAllowedPrefUnit() - countselctedroom.length();
        textinfo.setText( sisapilih + " units");
    }
}
