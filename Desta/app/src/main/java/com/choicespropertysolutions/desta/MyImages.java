package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.choicespropertysolutions.desta.Adapter.MyImagesAdapter;
import com.choicespropertysolutions.desta.Connectivity.MyImagesFetchList;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.Listeners.MyImagesFetchScrollListener;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.model.MyImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyImages extends AppCompatActivity {

    private static String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<MyImagesListItems> myImageListItem = new ArrayList<MyImagesListItems>();

    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    static String urlForFetch;

    private int current_page = 1;

    public String phone;
    private MyImagesListItems myImagesListItems;
    Toolbar myImagesToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_images);

        myImagesToolbar = (Toolbar) findViewById(R.id.myImagesToolbar);
        setSupportActionBar(myImagesToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        myImagesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.myImageRecyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        SessionManager sessionManager = new SessionManager(MyImages.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        phone = user.get(SessionManager.KEY_PHONE);

        url = url + "?method=showMyPhotoListCategoryWise&format=json&currentPage=" + current_page + "&mobileNo=" + phone + "";

        recyclerView.addOnScrollListener(new MyImagesFetchScrollListener(layoutManager, current_page) {

            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showMyPhotoListCategoryWise&format=json&currentPage=" + current_page + "&mobileNo=" + phone + "";
                grabURL(url);
            }
        });

        recyclerView.smoothScrollToPosition(0);

        adapter = new MyImagesAdapter(myImageListItem);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching My Images...");
        progressDialog.show();

        grabURL(url);
    }

    public void grabURL(String url) {
        new FetchImageListFromServer().execute(url);
    }

    public class FetchImageListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                MyImagesFetchList myImagesFetchList = new MyImagesFetchList(MyImages.this);
                myImagesFetchList.myImagesFetchList(myImageListItem, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        MyImages.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = MyImages.this.getPackageManager();
        ComponentName component = new ComponentName(MyImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = MyImages.this.getPackageManager();
        ComponentName component = new ComponentName(MyImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
