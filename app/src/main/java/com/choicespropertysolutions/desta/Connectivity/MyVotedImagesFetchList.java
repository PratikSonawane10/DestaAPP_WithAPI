package com.choicespropertysolutions.desta.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.choicespropertysolutions.desta.DialogBox.NullRespone_DialogeBox;
import com.choicespropertysolutions.desta.DialogBox.TimeOut_DialogeBox;
import com.choicespropertysolutions.desta.MyVotedImages;
import com.choicespropertysolutions.desta.Register;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.app.AppController;
import com.choicespropertysolutions.desta.model.MyVotedImagesListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyVotedImagesFetchList {

    private static Context context;
    private static String idOfUser;
    private static String currentPage;
    private static String category;


    private static String method;
    private static String format;

    public MyVotedImagesFetchList(MyVotedImages myVotedImages) {
        this.context = myVotedImages;
    }

    public static List myVotedImagesFetchList(final List<MyVotedImagesListItems> myVotedListingImages, final RecyclerView.Adapter adapter, String categoryOfPhoto,String userId,int current_page, final ProgressDialog progressDialog) {
        method = "showMyVotingList";
        format = "json";
        idOfUser = userId;
        currentPage = String.valueOf(current_page);
        category = categoryOfPhoto;

        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("userId", idOfUser);
            params.put("categoryOfPhoto", categoryOfPhoto);
            params.put("currentPage", currentPage);
        } catch (Exception e) {

        }

        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showMyVotingListResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        progressDialog.hide();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        MyVotedImagesListItems myVotedImagesListItems = new MyVotedImagesListItems();
                                        myVotedImagesListItems.setImagePath(obj.getString("photoPath"));
                                        myVotedImagesListItems.setId("photoId");
                                        myVotedListingImages.add(myVotedImagesListItems);
                                        adapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                progressDialog.hide();
                                Intent gotoNullError = new Intent(context, NullRespone_DialogeBox.class);
                                context.startActivity(gotoNullError);
                            }
                        } catch (JSONException e) {
                            progressDialog.hide();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                VolleyLog.e("Error: ", error.getMessage());
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
        return myVotedListingImages;
    }
}
