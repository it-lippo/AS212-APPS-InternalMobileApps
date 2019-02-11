package com.itams.visionet.customerserviceapps.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itams.visionet.customerserviceapps.R;
import com.itams.visionet.customerserviceapps.adapter.RoomAdapter;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;
import com.itams.visionet.customerserviceapps.helper.roomRealmHelper;
import com.itams.visionet.customerserviceapps.model.roomModel;
import com.itams.visionet.customerserviceapps.model.selectedPrefUnitModel;
import com.itams.visionet.customerserviceapps.rest.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Administrator on 28/11/2016.
 */

public class RoomFragment extends Fragment {
    Realm realm;
    @BindView(R.id.btnPesan)
    Button btnPesan;
    @BindView(R.id.btnBeli)
    Button btnBeli;
    @BindView(R.id.LLButton)
    LinearLayout LLButton;
    @BindView(R.id.list_room)
    RecyclerView mRecyclerView;
    @BindView(R.id.textinfo)
    TextView textinfo;
    @BindView(R.id.btn_clear)
    Button btnClear;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<roomModel> roomModels = new ArrayList<>();
    private JSONArray selectroom = new JSONArray();
    private ArrayList<String> selectedRoom = new ArrayList<>();
    private ArrayList<String> savedPrefUnit = new ArrayList<>();

    private RoomAdapter mAdapter;
    private roomRealmHelper helper;
    private String bookingRoom;

    ApiClient api = new ApiClient();
    String resultRoom;
    String floor_id;
    String project_id;
    String cluster_id;
    roomModel roomModel;
    ProgressDialog progress;
    SessionManagement session;

    public RoomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_room, container, false);
        ButterKnife.bind(this, rootView);

        //update 3011 : Mengambil data room sesuai floor_id
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            floor_id = bundle.getString("floor_id");
            cluster_id = bundle.getString("cluster_id");
            project_id = bundle.getString("project_id");
            savedPrefUnit = bundle.getStringArrayList("savedPrefUnit");
        }

        realm = Realm.getDefaultInstance();
        session = new SessionManagement(getActivity());
        helper = new roomRealmHelper(getActivity(), realm);
        progress = new ProgressDialog(getContext());
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
        helper.deleteallroom();

//        Toast.makeText(getContext(),session.getAllowedPrefUnit(),Toast.LENGTH_LONG).show();

//        roomModels = helper.getRoom(floor_id);

//

        return rootView;
    }

    @OnClick({R.id.btnPesan, R.id.btnBeli,R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPesan:
//                Toast.makeText(getActivity(), selectedRoom.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnBeli:
                booking();
                break;
            case R.id.btn_clear:
                clearselect();
                break;
        }
    }

    private void booking() {
        bookingWS task = new bookingWS();
        selectroom = helper.getSelectedRoom();
        //Toast.makeText(getContext(),"selected room" + String.valueOf(selectroom),Toast.LENGTH_LONG).show();
        task.execute(session.getPPNo());
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progress.setMessage("Get Data");
            progress.show();
            Log.i("getprojectrequeest", "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("getprojectrequeest", "doInBackground");
            resultRoom = api.getInfoUnitEachTower(project_id, cluster_id, floor_id,getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("getprojectrequeest", "onPostExecute");
            progress.dismiss();
            if (resultRoom != null) {
                Log.i("resultRoom", resultRoom);

                storeRoomtorealm();
                initrecycleview();
            } else {
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
            }
        }
    }

    private void initrecycleview() {
        roomModels.clear();
        roomModels.addAll(helper.getRoom(floor_id));

        if (roomModels.size() == 0) {
            Toast.makeText(getActivity(), "No room found", Toast.LENGTH_LONG).show();
        }


        mAdapter = new RoomAdapter(getActivity(), roomModels, savedPrefUnit, helper, new RoomAdapter.OnItemClickListener() {
            @Override
            public void onClick(roomModel item) {
                //update 0112 : menampilkan button pesan dan beli jika ada room yang dipilih
//                if (selectedRoom.contains(item.getUnitNo()))
//                    selectedRoom.remove(item.getUnitNo());
//                else
//                    selectedRoom.add(item.getUnitNo());
//
//                if (selectedRoom.isEmpty()) {
//                    LLButton.setVisibility(View.GONE);
//                } else {
//                    LLButton.setVisibility(View.VISIBLE);
//                }

//                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(selectedRoom.size() + " item Selected");
            }
        });

        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        int sisaroom = session.getAllowedPrefUnit() - helper.getSelectedRoom().length();
        textinfo.setText(sisaroom + " units");
    }

    private void storeRoomtorealm() {

        try {
            JSONObject objroom = new JSONObject(resultRoom);
            int code = objroom.getInt("code");
            if (code == 1) {
                ArrayList<roomModel> savedPreferredUnit = new ArrayList<>();
                ArrayList<selectedPrefUnitModel> list = helper.getSelectedRoomArrayList();
                for (int i = 0; i < list.size(); i++) {
                    roomModel obj = new roomModel();
                    obj.setUnitCode(list.get(i).getUnitCode());
                    obj.setUnitNo(list.get(i).getUnitNo());
                    obj.setOrder(String.valueOf(list.get(i).getOrder()));
                    savedPreferredUnit.add(obj);
                    Log.i("Selected UnitNo : ", obj.getUnitNo());
                }
                JSONArray arrayroom = objroom.getJSONArray("UnitData");
                for (int i = 0; i < arrayroom.length(); i++) {
                    JSONObject itemroom = arrayroom.getJSONObject(i);
                    roomModel = new roomModel();
                    roomModel.setProject_id(project_id);
                    roomModel.setCluster_id(cluster_id);
                    roomModel.setFloor_id(floor_id);
                    roomModel.setUnitCode(itemroom.getString("UnitCode"));
                    roomModel.setUnitNo(itemroom.getString("UnitNo"));
                    roomModel.setUnitStatus(itemroom.getString("UnitStatus"));
                    roomModel.setSelected(false);
                    if (savedPreferredUnit.contains(roomModel)){
                        roomModel.setSelected(true);
                        Log.d("order", savedPreferredUnit.get(savedPreferredUnit.indexOf(roomModel)).getOrder());
                        roomModel.setOrder(savedPreferredUnit.get(savedPreferredUnit.indexOf(roomModel)).getOrder());
                    }
                    roomModel.setColoring(itemroom.getString("Coloring"));
                    helper.addUpdateRoom(roomModel);
                    roomModels.add(roomModel);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void clearselect() {
        progress = ProgressDialog.show(getActivity(), null,
                "Loading", true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                helper.clearselectedroom();
//                clearselectedprevunit();
//                storeRoomtorealm();
                initrecycleview();
//                insertDataRoom();
//                textinfo.setText("you can choose " + session.getAllowedPrefUnit() + " unit left");
                progress.dismiss();
                booking();
//                AsyncCallWS task = new AsyncCallWS();
//                task.execute();
                Log.d("I", "Love u");
            }   }, 3000);

    }

    private void insertDataRoom() {
        roomModels.clear();
        roomModels.addAll(helper.getRoom(floor_id));
    }

    public void changedtext() {
        insertDataRoom();
        mAdapter.notifyDataSetChanged();
        JSONArray countselctedroom = helper.getSelectedRoom();
        int sisapilih = session.getAllowedPrefUnit() - countselctedroom.length();
        textinfo.setText( sisapilih + " units");
    }


    private class bookingWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.i("getprojectrequeest", "doInBackground");
            bookingRoom = api.saveSelectedPreferedUnit(strings[0], project_id, selectroom, session.getDomainName() + "\\" + session.getUserName(),getContext());
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
                kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
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
                    kotakBuilder.setMessage(booking.getString("message") + failed);
                    kotakBuilder.setPositiveButton("OK", null);
                    kotakBuilder.create().show();
//                Toast.makeText(getActivity(), booking.getString("message"), Toast.LENGTH_SHORT).show();
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
}

