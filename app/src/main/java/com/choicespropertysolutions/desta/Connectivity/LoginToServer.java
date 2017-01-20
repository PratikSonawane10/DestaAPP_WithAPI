package com.choicespropertysolutions.desta.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.choicespropertysolutions.desta.DialogBox.TimeOut_DialogeBox;
import com.choicespropertysolutions.desta.Index;
import com.choicespropertysolutions.desta.Login;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginToServer {
    private static Context context = null;
    private static String useremailOrPhone;
    private static String userpassword;
    private static String method;
    private static String format;
    private static String loginResponse;

    static String userId;
    static String name;
    static String state;
    static String mobileNo;
    static String email;

    public static String uploadToRemoteServer(String emailOrPhone, String password, final ProgressDialog progressDialog) throws Exception {
        method = "userLogin";
        format = "json";
        useremailOrPhone = emailOrPhone;
        userpassword = password;
        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("emailOrPhone", useremailOrPhone);
            params.put("confirmpassword", userpassword);
        } catch (Exception e) {

        }
        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        Login login = new Login();
                        try {
                            progressDialog.hide();
                             JSONObject jsonObject = response.getJSONObject("loginDetailsResponse");
                            returnResponse(jsonObject.getString("0"), jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressDialog.hide();
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        }
        );
        registerRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(registerRequest);
        return loginResponse;
    }

    public LoginToServer(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response, JSONObject responseObject) {
        SessionManager sessionManager;
        sessionManager = new SessionManager(context);
        if (response.equals("LOGIN_SUCCESSFULL")) {
            JSONArray details = null;
            try {
                details = responseObject.getJSONArray("details");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < details.length(); i++) {
                try {
                    JSONObject obj = details.getJSONObject(i);
                    userId = obj.getString("userId");
                    name = obj.getString("name");
                    state = obj.getString("state");
                    mobileNo = obj.getString("mobileNo");
                    email = obj.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Successfully Login.", Toast.LENGTH_SHORT).show();
            sessionManager.createUserLoginSession(userId, name, state, mobileNo, email);
            Intent gotoindexpage = new Intent(context, Index.class);
            gotoindexpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotoindexpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(gotoindexpage);
        } else if (response.equals("No_Is_Already_Registered")) {
            Toast.makeText(context, "This no is already registered!", Toast.LENGTH_SHORT).show();
            //sessionManager.createUserLoginSession(userphone, username, userstate);
            Intent gotoindexpage = new Intent(context, Index.class);
            gotoindexpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotoindexpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(gotoindexpage);
        } else {
            Intent gotosignupgae = new Intent(context, Login.class);
            context.startActivity(gotosignupgae);
        }
    }
}
