package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.choicespropertysolutions.desta.Connectivity.LoginToServer;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView lblregister;
    private TextView lblforgotpassword;
    private Button btnsignIn;
    private Button btncancel;

    private long TIME = 5000;
    private EditText txtemailorphoneno;
    private EditText txtpassword;
    private String emailOrPhone;
    private String password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtemailorphoneno = (EditText) findViewById(R.id.txtemailorphoneno);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        lblforgotpassword = (TextView) findViewById(R.id.lblforgetpassword);
        lblregister = (TextView) findViewById(R.id.lblregister);
        btnsignIn = (Button) findViewById(R.id.btnlogin);
        btncancel = (Button) findViewById(R.id.btncancel);

        lblregister.setPaintFlags(lblregister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnsignIn.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        lblregister.setOnClickListener(this);
        lblforgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        if(view.getId() == R.id.lblregister) {
            Intent register = new Intent(this,Register.class);
            startActivity(register);
        }
        else if(view.getId() == R.id.lblforgetpassword) {
//            Intent register = new Intent(this,Register.class);
//            startActivity(register);
        }
        else if(view.getId() == R.id.btnlogin) {
            view.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, TIME);

            emailOrPhone = txtemailorphoneno.getText().toString();
            password = txtpassword.getText().toString();

            if(txtemailorphoneno.getText().toString().isEmpty()){
                Toast.makeText(Login.this, "Please enter Email", Toast.LENGTH_LONG).show();
            }
            else if(txtpassword.getText().toString().isEmpty()){
                Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Login Data Wait...");
                progressDialog.show();
                try {
                    LoginToServer loginToServer = new LoginToServer(Login.this);
                    loginToServer.uploadToRemoteServer(emailOrPhone, password, progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(view.getId() == R.id.btncancel) {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Login.this.getPackageManager();
        ComponentName component = new ComponentName(Login.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Login.this.getPackageManager();
        ComponentName component = new ComponentName(Login.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
