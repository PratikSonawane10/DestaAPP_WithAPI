package com.choicespropertysolutions.desta.Connectivity;

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
import com.choicespropertysolutions.desta.Register;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddVoteForPhoto {

    private static Context context = null;
    private static String idOfUser;
    private static String PhotoId;
    private static String photoCategory;
    private static String method;
    private static String format;
    private static String addVote;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String addVoteOfPhoto(String userId, String photoListid, String categoryOfPhoto) throws Exception {
        method = "voteToPhoto";
        format = "json";
        idOfUser = userId;
        PhotoId = photoListid;
        photoCategory = categoryOfPhoto;
        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("userId", idOfUser);
            params.put("photoId", PhotoId);
            params.put("photoCategory", photoCategory);
        } catch (Exception e) {

        }
        JsonObjectRequest addVoteRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        try {
                            returnResponse(response.getString("saveVoteResponse"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        }
        );
        addVoteRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(addVoteRequest);
        return addVote;
    }

    public AddVoteForPhoto(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {

        if (response.equals("VOTING_SUCCESS")) {
            Toast.makeText(context, "You successfully vote for these photo.", Toast.LENGTH_SHORT).show();
//            Intent gotoindexpage = new Intent(context, Index.class);
//            context.startActivity(gotoindexpage);
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "There somthing problem please try after some times!", Toast.LENGTH_SHORT).show();
//            Intent gotoindexpage = new Intent(context, Index.class);
//            context.startActivity(gotoindexpage);
        }
    }
}
