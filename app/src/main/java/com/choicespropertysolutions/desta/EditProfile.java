package com.choicespropertysolutions.desta;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.choicespropertysolutions.desta.Adapter.SpinnerItemsAdapter;
import com.choicespropertysolutions.desta.Connectivity.SaveEditProfile;
import com.choicespropertysolutions.desta.InternetConnectivity.NetworkChangeReceiver;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private static EditText txt_name;
    private static EditText txt_phone;
    private static EditText txt_email;
    private static Spinner spinner_state;
    private static Button btn_edit_profile;
    private static Button btn_cancel;

    String name;
    String phone;
    String newPhone;
    String state;
    String email;
    String newEmail;
    String userId;

    ProgressDialog progressDialog = null;

    private long TIME = 5000;
    private String[] stateArrayList;
    private ArrayList<String> stateList;
    private SpinnerItemsAdapter adapter;
    private Toolbar editProfileToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        editProfileToolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setSupportActionBar(editProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editProfileToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_name = (EditText) findViewById(R.id.txtname);
        txt_phone = (EditText) findViewById(R.id.txtphone);
        txt_email = (EditText) findViewById(R.id.txtEmail);
        spinner_state = (Spinner) findViewById(R.id.spinnerstate);
        btn_edit_profile = (Button) findViewById(R.id.btneditprofile);
        btn_cancel = (Button)findViewById(R.id.btncancel);

        btn_edit_profile.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        SessionManager sessionManager = new SessionManager(EditProfile.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        phone = user.get(SessionManager.KEY_PHONE);
        state = user.get(SessionManager.KEY_STATE);
        email = user.get(SessionManager.KEY_EMAIL);
        userId = user.get(SessionManager.KEY_USER_ID);

        txt_name.setText(name);
        txt_phone.setText(phone);

        stateArrayList = new String[]{
                "Select your New State", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
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
        if(!state.equals(null)){
            int spinnerPosition = adapter.getPosition(state);
            spinner_state.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onClick(final View v) {
        if(v.getId() == R.id.btneditprofile) {
            v.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setEnabled(true);
                }
            }, TIME);

            name = txt_name.getText().toString();
            newPhone = txt_phone.getText().toString();
            newEmail = txt_email.getText().toString();

            if (txt_name.getText().toString().isEmpty() || txt_phone.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || state == null) {
                Toast.makeText(EditProfile.this, "All Details are necessory", Toast.LENGTH_LONG).show();
                //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
            } else {
                if(phone.length() != 10) {
                    Toast.makeText(EditProfile.this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
                }

                else {
                    progressDialog = new ProgressDialog(EditProfile.this);
                    progressDialog.setMessage("Editing Profile Wait...");
                    progressDialog.show();
                    try {
                        SaveEditProfile saveEditProfile = new SaveEditProfile(EditProfile.this);
                        saveEditProfile.uploadEditedDetails(name, phone, newPhone,email,newEmail, state,userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        else if(v.getId() == R.id.btncancel) {
            EditProfile.this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = EditProfile.this.getPackageManager();
        ComponentName component = new ComponentName(EditProfile.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = EditProfile.this.getPackageManager();
        ComponentName component = new ComponentName(EditProfile.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
