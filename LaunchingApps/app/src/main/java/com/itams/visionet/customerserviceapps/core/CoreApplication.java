package com.itams.visionet.customerserviceapps.core;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.itams.visionet.customerserviceapps.activity.LoginActivity;
import com.itams.visionet.customerserviceapps.activity.NetworkActivity;
import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by alde.asprilla on 23/11/2016.
 */

public class CoreApplication extends Application {
    public static CoreApplication app;
    SessionManagement session;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("csApps.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        session = new SessionManagement(this);

        app = this;
    }

    public SessionManagement getSession() {
        return session;
    }

    public static CoreApplication getInstance() {
        return app;
    }

    public void showLogoutConfirmation(Context context) {
        AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(context);
        kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        kotakBuilder.setTitle("Logout Confirmation");
        kotakBuilder.setMessage("Do you want to logout ?");
        kotakBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
        kotakBuilder.setNegativeButton("no", null);
        kotakBuilder.create().show();
    }
    public void logout() {
        session.setKeyIsLoggedIn(false);


        DeleteSharepref();


        Intent intent = new Intent(this, NetworkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void DeleteSharepref() {
        SharedPreferences.Editor deleteSharepref =getSharedPreferences("LaunchingApps", 0).edit();

        deleteSharepref.remove(SessionManagement.KEY_USER_NAME).commit();
        deleteSharepref.remove(SessionManagement.KEY_PASSWORD).commit();
        deleteSharepref.remove(SessionManagement.KEY_IS_LOGGED_IN).commit();
        deleteSharepref.remove(SessionManagement.KEY_DOMAIN_NAME).commit();
        deleteSharepref.remove(SessionManagement.KEY_SITE).commit();
        deleteSharepref.remove(SessionManagement.KEY_ALLOWED_MENUS).commit();
        deleteSharepref.remove(SessionManagement.KEY_ALLOWED_PREF_UNIT).commit();
        deleteSharepref.remove(SessionManagement.KEY_PPNO).commit();
        deleteSharepref.remove(SessionManagement.KEY_PROJECT_CODE).commit();

    }
}
