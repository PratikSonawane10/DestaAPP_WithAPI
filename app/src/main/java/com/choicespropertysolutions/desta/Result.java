package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.choicespropertysolutions.desta.Adapter.MyImagesAdapter;
import com.choicespropertysolutions.desta.Adapter.ResultImagesAdapter;
import com.choicespropertysolutions.desta.Connectivity.MyImagesFetchList;
import com.choicespropertysolutions.desta.Connectivity.ResultImageFetch;
import com.choicespropertysolutions.desta.Listeners.MyImagesFetchScrollListener;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.model.MyImagesListItems;
import com.choicespropertysolutions.desta.model.ResultImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Result extends AppCompatActivity {

    private TextView emptyText;
    private Toolbar resultToolbar;
    public String stateofUser;
    public String categoryOfPhoto;
    public List<ResultImagesListItems> resultImageListItem = new ArrayList<ResultImagesListItems>();
    private ArrayAdapter<String> dialogAdapter;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;


    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        resultToolbar = (Toolbar) findViewById(R.id.resultImagesToolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //emptyText = (TextView) findViewById(R.id.emptyText);

        recyclerView = (RecyclerView) findViewById(R.id.resultImageRecyclerView);

        createTagSelectImageDialogChooser();

    }

    private void createTagSelectImageDialogChooser() {

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.smoothScrollToPosition(0);
        adapter = new ResultImagesAdapter(resultImageListItem);
        recyclerView.setAdapter(adapter);

        dialogAdapter = new ArrayAdapter<String>(Result.this, android.R.layout.select_dialog_item) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Result.this, R.style.AlertDialogCustom));
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
        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching Result...");
        progressDialog.show();

        SessionManager sessionManager = new SessionManager(Result.this);
        final HashMap<String, String> user = sessionManager.getUserDetails();
        //stateofUser = user.get(SessionManager.KEY_STATE);
        stateofUser = "Maharashtra";
        categoryOfPhoto= selectedTag;
        try{
            ResultImageFetch resultImageFetch = new ResultImageFetch(Result.this);
           // resultImageFetch.fetchResultImage(resultImageListItem,adapter,categoryOfPhoto,stateofUser, progressDialog);
            resultImageFetch.fetchResultImage(resultImageListItem,adapter,categoryOfPhoto, progressDialog);
        }catch (Exception e){
            progressDialog.hide();
            e.printStackTrace();
        }
    }
}
