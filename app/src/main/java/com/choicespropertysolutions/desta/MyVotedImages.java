package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.choicespropertysolutions.desta.Adapter.MyVotedImagesAdapter;
import com.choicespropertysolutions.desta.Connectivity.MyVotedImagesFetchList;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.Listeners.MyVotedImagesFetchScrollListener;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.URLInstance;
import com.choicespropertysolutions.desta.model.MyVotedImagesListItems;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyVotedImages extends AppCompatActivity {

    private static String url = "";
    private ProgressDialog progressDialog;
    public List<MyVotedImagesListItems> myVotedImageListItem = new ArrayList<MyVotedImagesListItems>();

    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    static String urlForFetch;

    private int current_page = 1;

    public String userId;
    public String categoryOfPhoto;

    private MyVotedImagesListItems MyVotedImagesListItems;
    Toolbar MyVotedImagesToolbar;
    private ArrayAdapter<String> dialogAdapter;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_voted_images);

        MyVotedImagesToolbar = (Toolbar) findViewById(R.id.myVotedImagesToolbar);
        setSupportActionBar(MyVotedImagesToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        MyVotedImagesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.myVotedImageRecyclerView);

        createTagSelectImageDialogChooser();
    }

    private void createTagSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(MyVotedImages.this, android.R.layout.select_dialog_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.alertDialogListNames));
                return view;
            }
        };
        dialogAdapter.add("Best Farming Family");
        dialogAdapter.add("Technology in Farming");
        dialogAdapter.add("Woman in Farming");
        dialogAdapter.add("Next Generation in Farming");
        dialogAdapter.add("Best Looking Farmer - Male");
        dialogAdapter.add("Best Looking Farmer - Female");
        dialogAdapter.add("Urban Faming");
        dialogAdapter.add("My Farming Friend (Animal)");
        dialogAdapter.add("Healthiest Crop");
        dialogAdapter.add("Weirdest Crop");

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MyVotedImages.this, R.style.AlertDialogCustom));
        builder.setTitle("Select Tags");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String selectedTag = dialogAdapter.getItem(which);
                createListTagWise(selectedTag);
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    private void createListTagWise(final String selectedTag) {
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.smoothScrollToPosition(0);
        adapter = new MyVotedImagesAdapter(myVotedImageListItem);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching My Images...");
        progressDialog.show();

        SessionManager sessionManager = new SessionManager(MyVotedImages.this);
        final HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USER_ID);
        //userId = "8";
        categoryOfPhoto= selectedTag;

        recyclerView.addOnScrollListener(new MyVotedImagesFetchScrollListener(layoutManager, current_page) {

            @Override
            public void onLoadMore(int current_page) {

                MyVotedImagesFetchList myVotedImagesFetchList = new MyVotedImagesFetchList(MyVotedImages.this);
                myVotedImagesFetchList.myVotedImagesFetchList(myVotedImageListItem, adapter, categoryOfPhoto,userId,current_page, progressDialog);

            }
        });

        MyVotedImagesFetchList myVotedImagesFetchList = new MyVotedImagesFetchList(MyVotedImages.this);
        myVotedImagesFetchList.myVotedImagesFetchList(myVotedImageListItem, adapter, categoryOfPhoto,userId,current_page, progressDialog);

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
        MyVotedImages.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = MyVotedImages.this.getPackageManager();
        ComponentName component = new ComponentName(MyVotedImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = MyVotedImages.this.getPackageManager();
        ComponentName component = new ComponentName(MyVotedImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
