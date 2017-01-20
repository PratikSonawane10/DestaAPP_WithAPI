package com.choicespropertysolutions.desta;


import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.model.ResultImagesListItems;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ResultDetails  extends AppCompatActivity implements View.OnClickListener {
    String photoPath = "";
    String photoId = "";
    String category = "";
    String totalVote = "";
    String userName = "";
    String state = "";
    String email = "";
    String mobileno = "";
    String firstImagePath = "";

    TextView resultCategory;
    //ImageView resultDetailsFirstImageThumbnail;
    TextView txtOwnerName;
    TextView txtOwnerState;
    TextView txtOwnerMobileNo;
    TextView txtOwnerEamil;
    TextView voteOfImage;
    ImageView resultImage;

    Bitmap resultDetailsbitmap;
    Toolbar resultDetailstoolbar;
    CollapsingToolbarLayout resultDetailsCollapsingToolbar;
    CoordinatorLayout resultDetailsCoordinatorLayout;
    AppBarLayout resultDetailsAppBarLayout;
    NestedScrollView resultDetailsNestedScrollView;
    private int mutedColor;
    ResultImagesListItems resultImagesListItems = new ResultImagesListItems();

    LinearLayout resultListDetailsLinearLayout;
    RelativeLayout resultListDetailsFirstRelativeLayout;
    RelativeLayout resultListDetailsSecondRelativeLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_details);

        Intent intent = getIntent();
        if (null != intent) {
            photoPath = intent.getStringExtra("photoPath");
            photoId = intent.getStringExtra("photoId");
            category = intent.getStringExtra("photoCategory");
            totalVote = intent.getStringExtra("totalVote");
            state = intent.getStringExtra("state");
            userName = intent.getStringExtra("name");
            email = intent.getStringExtra("email");
            mobileno = intent.getStringExtra("mobileNo");
        }

        resultDetailstoolbar = (Toolbar) findViewById(R.id.resultDetailsToolbar);
        setSupportActionBar(resultDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resultDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.resultDetailsCollapsingToolbar);
        resultDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.resultDetailsCoordinatorLayout);
        resultDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.resultDetailsAppBar);
        resultDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.resultDetailsNestedScrollView);
        resultImage = (ImageView) findViewById(R.id.resultHeaderImage);
        //resultDetailsImageText = (TextView) findViewById(R.id.resultDetailsImageText);

        resultCategory = (TextView) findViewById(R.id.photoCategory);
        txtOwnerName = (TextView) findViewById(R.id.ownerName);
        txtOwnerState = (TextView) findViewById(R.id.ownerState);
        txtOwnerMobileNo = (TextView) findViewById(R.id.ownerMobileNo);
        txtOwnerEamil = (TextView) findViewById(R.id.ownerEmail);
        voteOfImage = (TextView) findViewById(R.id.totalVote);

        resultListDetailsLinearLayout = (LinearLayout) findViewById(R.id.resultListDetailsLinearLayout);
        resultListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.resultListDetailsSecondRelativeLayout);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, photoPath);
        resultDetailsCollapsingToolbar.setTitle(userName);

        //resultDetailsImageText.setText("Images");

        String categoryOfPhoto =  category;
        resultCategory.setText(Html.fromHtml(categoryOfPhoto));

        String name = userName;
        txtOwnerName.setText(Html.fromHtml(name));

        String stateOfUser = state;
        txtOwnerState.setText(Html.fromHtml(stateOfUser));

        String mobileNoOfUser =  mobileno;
        txtOwnerMobileNo.setText(Html.fromHtml(mobileNoOfUser));

        String emailOfUser =  email;
        txtOwnerEamil.setText(Html.fromHtml(emailOfUser));

        String vote =  totalVote;
        voteOfImage.setText(Html.fromHtml(vote));

    }

    public class FetchImageFromServer extends AsyncTask<String, String, String> {
        String urlForFetch;
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return urlForFetch;
        }

//        @Override
//        protected void onPostExecute(String url) {
//            super.onPostExecute(url);
//            if(urlForFetch.equals(firstImagePath)) {
//                Glide.with(resultImage.getContext()).load(url).centerCrop().crossFade().into(resultImage);
//                Glide.with(resultDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(resultDetailsFirstImageThumbnail);
//                resultDetailsFirstImageThumbnail.setOnClickListener(ResultDetails.this);
//            }
//
//        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) resultDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(resultDetailsCoordinatorLayout, resultDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    @Override
    public void onBackPressed() {
        ResultDetails.this.finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = ResultDetails.this.getPackageManager();
        ComponentName component = new ComponentName(ResultDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = ResultDetails.this.getPackageManager();
        ComponentName component = new ComponentName(ResultDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}