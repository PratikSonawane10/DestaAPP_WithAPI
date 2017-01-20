package com.choicespropertysolutions.desta;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.choicespropertysolutions.desta.DialogBox.NotifyNetworkConnectionMessage;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;

public class Index extends BaseActivity implements View.OnClickListener {

    private static Index instance = new Index();
    static Context context;
    private Button uploadImagesForm;
    private TextView notifyText;

    public static Index getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        notifyText = (TextView) findViewById(R.id.notifyText);
        notifyText.setSelected(true);

        ConnectivityManager cm =
                (ConnectivityManager)Index.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if(isConnected) {
            uploadImagesForm = (Button) findViewById(R.id.uploadImagesForm);
            uploadImagesForm.setOnClickListener(this);
            isCheckRegister();
        }
        else {
            Intent networkReciever = new Intent(Index.this, NotifyNetworkConnectionMessage.class);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Index.this.startActivity(networkReciever);
        }
    }

    public void isCheckRegister() {
        boolean checkRegister = sessionManager.isLoggedIn();
        if (!checkRegister) {
            // start your home screen
            Intent intent = new Intent(Index.this, Register.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ImageUploadForm.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Index.this.getPackageManager();
        ComponentName component = new ComponentName(Index.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Index.this.getPackageManager();
        ComponentName component = new ComponentName(Index.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
