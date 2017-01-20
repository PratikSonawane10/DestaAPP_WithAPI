package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.choicespropertysolutions.desta.Adapter.AllImagesAdapter;
import com.choicespropertysolutions.desta.Connectivity.AllImagesFetchList;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.Listeners.AllImagesFetchScrollListener;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.model.AllImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllImages  extends AppCompatActivity {

    private static String url = "";
    private ProgressDialog progressDialog;
    public List<AllImagesListItems> allImageListItem = new ArrayList<AllImagesListItems>();

    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    static String urlForFetch;

    private int current_page = 1;

    public String userId;
    public String categoryOfPhoto;

    private AllImagesListItems AllImagesListItems;
    Toolbar AllImagesToolbar;
    private ArrayAdapter<String> dialogAdapter;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_images);

        AllImagesToolbar = (Toolbar) findViewById(R.id.allImagesToolbar);
        setSupportActionBar(AllImagesToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        AllImagesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.allImageRecyclerView);

        createTagSelectImageDialogChooser();
    }

    private void createTagSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(AllImages.this, android.R.layout.select_dialog_item) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AllImages.this, R.style.AlertDialogCustom));
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
        adapter = new AllImagesAdapter(allImageListItem);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching Images...");
        progressDialog.show();

        SessionManager sessionManager = new SessionManager(AllImages.this);
        final HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USER_ID);
        categoryOfPhoto= selectedTag;

        recyclerView.addOnScrollListener(new AllImagesFetchScrollListener(layoutManager, current_page) {

            @Override
            public void onLoadMore(int current_page) {

                AllImagesFetchList AllImagesFetchList = new AllImagesFetchList(AllImages.this);
                AllImagesFetchList.allImagesFetchList(allImageListItem, adapter, categoryOfPhoto,userId,current_page, progressDialog);
            }
        });
        AllImagesFetchList AllImagesFetchList = new AllImagesFetchList(AllImages.this);
        AllImagesFetchList.allImagesFetchList(allImageListItem, adapter, categoryOfPhoto,userId,current_page, progressDialog);
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
        AllImages.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = AllImages.this.getPackageManager();
        ComponentName component = new ComponentName(AllImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = AllImages.this.getPackageManager();
        ComponentName component = new ComponentName(AllImages.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}