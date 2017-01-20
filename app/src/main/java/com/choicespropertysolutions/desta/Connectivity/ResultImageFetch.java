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
import com.choicespropertysolutions.desta.Result;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.app.AppController;
import com.choicespropertysolutions.desta.model.MyVotedImagesListItems;
import com.choicespropertysolutions.desta.model.ResultImagesListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ResultImageFetch {
    private static Context context;
    private static String state;
    private static String category;

    private static String method;
    private static String format;

    public ResultImageFetch(Result result) {
        this.context = result;
    }

    //public static List fetchResultImage(final List<ResultImagesListItems> resultListing, String categoryOfPhoto, String stateofUser, final ProgressDialog progressDialog) {
    public static List fetchResultImage(final List<ResultImagesListItems> resultListing, final RecyclerView.Adapter adapter, String categoryOfPhoto, final ProgressDialog progressDialog) {
        method = "showResultsCategoryWiseTop10";
        format = "json";
        //state = stateofUser;
        category = categoryOfPhoto;

        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            //params.put("state", state);
            params.put("photoCategory", categoryOfPhoto);
        } catch (Exception e) {

        }

        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showResultsResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ResultImagesListItems resultImagesListItems = new ResultImagesListItems();
                                        resultImagesListItems.setImagePath(obj.getString("photoPath"));
                                        resultImagesListItems.setId(obj.getString("photoId"));
                                        resultImagesListItems.setImageCategory(obj.getString("photoCategory"));
                                        resultImagesListItems.setTotalVote(obj.getString("totalVote"));
                                        resultImagesListItems.setUserState(obj.getString("state"));
                                        resultImagesListItems.setUserName(obj.getString("name"));
                                        resultImagesListItems.setEmail(obj.getString("email"));
                                        resultImagesListItems.setMobileNo(obj.getString("mobileNo"));
                                        adapter.notifyDataSetChanged();
                                        resultListing.add(resultImagesListItems);
                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
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
                    }}, new Response.ErrorListener() {
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
        return resultListing;
    }
}
