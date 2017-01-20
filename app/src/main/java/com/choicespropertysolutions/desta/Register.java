package com.choicespropertysolutions.desta;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.choicespropertysolutions.desta.Adapter.SpinnerItemsAdapter;
import com.choicespropertysolutions.desta.Connectivity.RegisterToServer;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private static EditText txt_name;
    private static EditText txt_email;
    private static EditText txt_phone;
    private static EditText txt_password;
    private static EditText txt_confirmpassword;
    private static Spinner spinner_state;
    private static Button btn_register;
    private static Button btn_cancel;

    String name;
    String email;
    String phone;
    String password;
    String confirmpassword;
    String state;
    ProgressDialog progressDialog = null;

    private long TIME = 5000;
    private String[] stateArrayList;
    private ArrayList<String> stateList;
    private SpinnerItemsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        txt_name = (EditText) findViewById(R.id.txtname);
        txt_email = (EditText) findViewById(R.id.txtemail);
        txt_phone = (EditText) findViewById(R.id.txtphone);
        txt_password = (EditText) findViewById(R.id.txtpassword);
        txt_confirmpassword = (EditText) findViewById(R.id.txtconfirmpassword);
        spinner_state = (Spinner) findViewById(R.id.spinnerstate);
        btn_register = (Button) findViewById(R.id.btnregister);
        btn_cancel = (Button)findViewById(R.id.btncancel);

        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        stateArrayList = new String[]{
                "Select State", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
        };
        stateList = new ArrayList<>(Arrays.asList(stateArrayList));
        adapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, stateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    state = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(final View v) {
        if(v.getId() == R.id.btnregister) {
            v.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setEnabled(true);
                }
            }, TIME);

            name = txt_name.getText().toString();
            email = txt_email.getText().toString();
            phone = txt_phone.getText().toString();
            password = txt_password.getText().toString();
            confirmpassword = txt_confirmpassword.getText().toString();

            if (txt_name.getText().toString().isEmpty()) {
                Toast.makeText(Register.this, "Please enter Name", Toast.LENGTH_LONG).show();
                //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
            }
            if (state == null) {
                Toast.makeText(Register.this, "Select your state", Toast.LENGTH_LONG).show();
                //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
            }
            if(txt_email.getText().toString().isEmpty() && txt_phone.getText().toString().isEmpty()){
                Toast.makeText(Register.this, "Please enter Email or Mobile No.", Toast.LENGTH_LONG).show();
            }
            if(!Objects.equals(password, confirmpassword)){
                Toast.makeText(Register.this, "Password does not Match.", Toast.LENGTH_LONG).show();
            }
            if(!phone.isEmpty() && phone.length() != 10 ){
                Toast.makeText(Register.this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
            }
            else {
                    progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setMessage("Registering Data Wait...");
                    progressDialog.show();
                    try {
                        RegisterToServer registerToServer = new RegisterToServer(Register.this);
                        registerToServer.uploadToRemoteServer(name, phone, state,email,confirmpassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
            }
        }
        else if(v.getId() == R.id.btncancel) {
            Intent intent = new Intent(getApplicationContext(), Index.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Register.this.getPackageManager();
        ComponentName component = new ComponentName(Register.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Register.this.getPackageManager();
        ComponentName component = new ComponentName(Register.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
