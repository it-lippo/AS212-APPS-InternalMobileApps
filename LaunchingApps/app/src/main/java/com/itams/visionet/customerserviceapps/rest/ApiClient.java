package com.itams.visionet.customerserviceapps.rest;

import android.content.Context;
import android.content.pm.PackageInstaller;
import android.os.StrictMode;
import android.util.Log;

import com.itams.visionet.customerserviceapps.helper.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * Created by alde.asprilla on 21/12/2016.
 */

public class ApiClient {
    //private static final String SOAP_ACTION = "http://tempuri.org/getInfoPriorityPass";
    //private static final String METHOD_NAME = "getInfoPriorityPass";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL_LOGIN= "http://erp.sandiegohills.co.id/InternalMobileAppsServices/WS_General.asmx?WSDL";

    private String NetworkSite;
    private String username ;
    private String password ;
    private String domain ;

//    SessionManagement sessionManagement = new SessionManagement(getAp);



//    http://erp.sandiegohills.co.id/InternalMobileAppsServices/WS_Launching.asmx


    public String getCluster(String projectCode, Context context){
        try {
            SoapObject Request = new SoapObject(NAMESPACE, "GetDataClusterLaunching");
            Request.addProperty("projectCode", projectCode);

            SoapObject result = (SoapObject) soapEnvelope(Request, "GetDataClusterLaunching",context).bodyIn;


            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String getProject(Context context){
        try {
            JSONObject input = new JSONObject();
            input.put(" "," ");
            SoapObject Request = new SoapObject(NAMESPACE, "GetDataProjectLaunching");
//            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "GetDataProjectLaunching",context).bodyIn;
//            Log.i("result testing", String.valueOf(result));
            Log.i("PP", "PP : " + result.getProperty(0).toString());

            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String doRegisterPP(String PPNo, String ProjectCode,Context context){
        try {
            JSONObject input = new JSONObject();
            input.put("PPNo", PPNo);
            input.put("ProjectCode", ProjectCode);
            SoapObject Request = new SoapObject(NAMESPACE, "doRegisterPriorityPass");
            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "doRegisterPriorityPass",context).bodyIn;

            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String doCheckedPP(String PPNo, String ProjectCode,Context context){
        try {
            JSONObject input = new JSONObject();
            input.put("PPNo", PPNo);
            input.put("ProjectCode", ProjectCode);
            SoapObject Request = new SoapObject(NAMESPACE, "doCheckedPriorityPass");
            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "doCheckedPriorityPass",context).bodyIn;

            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String getDataPP(String PPNo, String ProjectCode,Context context) {
        try {
            JSONObject input = new JSONObject();
            input.put("PPNo", PPNo);
            input.put("ProjectCode", ProjectCode);
            SoapObject Request = new SoapObject(NAMESPACE, "getInfoPriorityPass");
            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "getInfoPriorityPass",context).bodyIn;

            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String getInfoTower(String projectCode,String clusterCode,Context context){
        try {
            JSONObject input = new JSONObject();
            input.put("ProjectCode",projectCode);
            input.put("ClusterCode",clusterCode);
            SoapObject Request = new SoapObject(NAMESPACE, "getInfoTower");
            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "getInfoTower",context).bodyIn;


            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String getInfoUnitEachTower(String projectCode,String clusterCode,String floorCode,Context context){
        try {
            JSONObject input = new JSONObject();
            input.put("ProjectCode",projectCode);
            input.put("ClusterCode",clusterCode);
            input.put("Floor",floorCode);
            SoapObject Request = new SoapObject(NAMESPACE, "getInfoUnitEachTower");
            Request.addProperty("JSON_IN", input.toString());

            SoapObject result = (SoapObject) soapEnvelope(Request, "getInfoUnitEachTower",context).bodyIn;


            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String doLogin(String username,String password, String domainName,Context context){
        try {

            this.username = username;
            this.password = password;
            this.domain = domainName;

            JSONObject input = new JSONObject();
            input.put("DomainName", domainName);
            input.put("userName",username);
            input.put("password",password);
            SoapObject Request = new SoapObject(NAMESPACE, "doLogin");
            Request.addProperty("JSON_IN", input.toString());
            SoapObject result = (SoapObject) soapEnvelope(Request, "doLogin",context).bodyIn;
            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    public String saveSelectedPreferedUnit(String PPNo, String ProjectCode, JSONArray preferredUnit, String username,Context context){
        try {
            JSONObject input = new JSONObject();
            input.put("PPNo", PPNo);
            input.put("ProjectCode", ProjectCode);
            input.put("preferredUnit", preferredUnit);
            input.put("username", username);
            Log.i("Input Booking", input.toString());
            SoapObject Request = new SoapObject(NAMESPACE, "saveSelectedPreferedUnit");
            Request.addProperty("JSON_IN", input.toString());
            SoapObject result = (SoapObject) soapEnvelope(Request, "saveSelectedPreferedUnit",context).bodyIn;
            Log.i("PP", "Result : " + result.getProperty(0).toString());
            return result.getProperty(0).toString();
        } catch (Exception ex) {
            Log.e("PP", "Error: " + ex.getMessage());
        }
        return null;
    }

    private SoapSerializationEnvelope soapEnvelope(SoapObject Request, String Method,Context context){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SessionManagement sessionManagement = new SessionManagement (context);

        try {
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.headerOut = new Element[1];
            if (Method.equals("doLogin")){
                soapEnvelope.headerOut[0] = buildAuthHeader(username,password,domain);
            }else{
                soapEnvelope.headerOut[0] = buildAuthHeader(sessionManagement.getUserName(),sessionManagement.getPassword(),sessionManagement.getDomainName());
            }

            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport;

            if (Method.equals("doLogin")){
                transport = new HttpTransportSE(URL_LOGIN);
            }else {
                if(sessionManagement.GetSiteName().equals("INTERNET")) {
                    transport = new HttpTransportSE(sessionManagement.getinternet());
                }else{
                    transport = new HttpTransportSE(sessionManagement.getIntranet());

                }
            }

            transport.call(NAMESPACE + Method, soapEnvelope);
//            SoapPrimitive resultst =

            return soapEnvelope;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Element buildAuthHeader (String username,String Strpassword,String domain) {

        Element h = new Element().createElement(NAMESPACE, "AuthHeader");
        Element domainName = new Element().createElement(NAMESPACE, "domainName");
        domainName.addChild(Node.TEXT, domain);
        h.addChild(Node.ELEMENT, domainName);
        Element userName = new Element().createElement(NAMESPACE, "userName");
        userName.addChild(Node.TEXT,username);
        h.addChild(Node.ELEMENT, userName);
        Element password = new Element().createElement(NAMESPACE, "password");
        password.addChild(Node.TEXT,Strpassword);
        h.addChild(Node.ELEMENT, password);

        return h;
    }
}
