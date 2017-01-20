package com.choicespropertysolutions.desta.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.choicespropertysolutions.desta.DialogBox.NullRespone_DialogeBox;
import com.choicespropertysolutions.desta.DialogBox.TimeOut_DialogeBox;
import com.choicespropertysolutions.desta.MyImages;
import com.choicespropertysolutions.desta.Register;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.Singleton.UserVoteList;
import com.choicespropertysolutions.desta.app.AppController;
import com.choicespropertysolutions.desta.model.MyImagesListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyImagesFetchList {

    private static Context context;
    private static String idOfUser;
    private static String currentPage;
    private static String category;


    private static String method;
    private static String format;
    private static ArrayList<String> userVoteListId = new ArrayList<String>();
    private static  int checkRequestState;

    public MyImagesFetchList(MyImages myImages) {
        this.context = myImages;
    }

    public static List myImagesFetchList(final List<MyImagesListItems> myListingImages, final RecyclerView.Adapter adapter, String categoryOfPhoto,String userId,int current_page, final ProgressDialog progressDialog) {
        method = "showMyPhotoListCategoryWise";
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
                            if(!response.getJSONArray("showVoteListResponse").equals(JSONObject.NULL)) {
                                if (checkRequestState == 0) {
                                    JSONArray wishListjsonArray = response.getJSONArray("showVoteListResponse");

                                    for (int i = 0; i < wishListjsonArray.length(); i++) {
                                        try {
                                            JSONObject obj = wishListjsonArray.getJSONObject(i);
                                            String item = (obj.getString("photoId"));
                                            userVoteListId.add(item);
                                            UserVoteList userVoteList = new UserVoteList();
                                            userVoteList.setVoteList(userVoteListId);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                    UserVoteList userVoteList = new UserVoteList();
//                                    userVoteList.setVoteListId(userVoteListId);
                                    checkRequestState = 1;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("showMyPhotosListResponse");
                            if (jsonArray.length() == 0) {
                                progressDialog.hide();
                                Toast.makeText(context, "Photos Not available", Toast.LENGTH_SHORT).show();
                                Intent EmptyListError = new Intent(context, NullRespone_DialogeBox.class);
                                context.startActivity(EmptyListError);
                            }else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        progressDialog.hide();
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        MyImagesListItems myImagesListItems = new MyImagesListItems();
                                        myImagesListItems.setImagePath(obj.getString("photoPath"));
                                        myImagesListItems.setId(obj.getString("photoId"));
                                        myImagesListItems.setImageCategory(obj.getString("photoCategory"));
                                        myListingImages.add(myImagesListItems);
                                        adapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            progressDialog.hide();
                            e.printStackTrace();
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
        return myListingImages;
    }
}
