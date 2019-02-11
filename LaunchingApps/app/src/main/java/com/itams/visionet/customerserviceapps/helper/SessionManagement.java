
package com.itams.visionet.customerserviceapps.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alde.asprilla on 05/12/2016.
 */

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DOMAIN_NAME = "domain";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_PPNO = "ppno";
    public static final String KEY_PROJECT_CODE = "projectCode";
    public static final String KEY_ALLOWED_PREF_UNIT = "allowedPrefUnit";
    public static final String KEY_ALLOWED_MENUS = "allowedMenus";
    public static final String KEY_SITE = "SITE";
    public static final String KEY_LINK_INTRANET = "LinkIntranet";
    public static final String KEY_LINK_INTERNET = "LinkInternet";


    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("LaunchingApps", PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setActiveInformation(String email, String password, String domain, ArrayList<String> menus,String Site)  {
        Set<String> set = new HashSet<String>();
        set.addAll(menus);
        // Storing name in pref
        editor.putString(KEY_USER_NAME, email.trim());
        editor.putString(KEY_PASSWORD, password);
        editor.putStringSet(KEY_ALLOWED_MENUS, set);
        editor.putString(KEY_DOMAIN_NAME, domain);
//        editor.putString(KEY_SITE,Site);
//        editor.putString(KEY_USERID, userid);
        // commit changes
        editor.commit();
    }

    public void setLink (String intranet,String internet){
        if(intranet.length() > 5){
            Log.i("substring_session",intranet.substring(intranet.length()-5,intranet.length()));
            if (intranet.substring(intranet.length()-5,intranet.length()).contains("?WSDL")) {

                    editor.putString(KEY_LINK_INTRANET, intranet);
            }
            else{
                editor.putString(KEY_LINK_INTRANET, intranet+"?WSDL");
            }
        }

        if (internet.length() > 5){
            if (internet.substring(internet.length()-5,internet.length()).contains("?WSDL")) {
                editor.putString(KEY_LINK_INTERNET, internet);
            }
            else{
                editor.putString(KEY_LINK_INTERNET, internet+"?WSDL");
            }

        }

        editor.commit();

    }

    public String getIntranet(){
        return pref.getString(KEY_LINK_INTRANET,"");
    }

    public String getinternet(){
        return pref.getString(KEY_LINK_INTERNET,"");

    }

    public  void  setSite(String site){
        editor.putString(KEY_SITE,site);
        editor.commit();
    }




    /* Get stored session data */
    public HashMap<String, String> getActiveInformation() {
        HashMap<String, String> activeUser = new HashMap<String, String>();
        // user id
        activeUser.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        activeUser.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, ""));
//        activeUser.put(KEY_USERID, pref.getString(KEY_USERID, ""));
        return activeUser;
    }

    public void setKeyIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean getKeyIsLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserName(){ return pref.getString(KEY_USER_NAME, ""); }

    public String getPassword(){ return pref.getString(KEY_PASSWORD, ""); }

    public String getDomainName(){ return pref.getString(KEY_DOMAIN_NAME, ""); }

    public String GetSiteName () {return  pref.getString(KEY_SITE,"");}

    public void setPPNo(String PPNo){
        editor.putString(KEY_PPNO, PPNo);
        editor.commit();
    }

    public String getPPNo(){ return pref.getString(KEY_PPNO, ""); }

    public void setProjectCode(String ProjectCode){
        editor.putString(KEY_PROJECT_CODE, ProjectCode);
        editor.commit();
    }

    public String getProjectCode(){ return pref.getString(KEY_PROJECT_CODE, ""); }

    public void setAllowedPrefUnit(int unit){
        editor.putInt(KEY_ALLOWED_PREF_UNIT, unit);
        editor.commit();
    }

    public int getAllowedPrefUnit(){ return pref.getInt(KEY_ALLOWED_PREF_UNIT, 0); }

    public ArrayList<String> getMenus(){
        ArrayList<String> menus = new ArrayList<>();
        menus.addAll(pref.getStringSet(KEY_ALLOWED_MENUS, null));
        return menus;
    }
}
