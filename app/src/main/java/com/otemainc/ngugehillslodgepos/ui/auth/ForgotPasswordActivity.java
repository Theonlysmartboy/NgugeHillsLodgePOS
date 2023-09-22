package com.otemainc.ngugehillslodgepos.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.otemainc.ngugehillslodgepos.R;
import com.otemainc.ngugehillslodgepos.network.CheckInternetConnection;
import com.otemainc.ngugehillslodgepos.network.InputValidator;
import com.otemainc.ngugehillslodgepos.network.NetworkConstants;
import com.otemainc.ngugehillslodgepos.utils.Config;
import com.otemainc.ngugehillslodgepos.utils.NgugeHillsLodge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnSubmit;
    TextView login;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txtEmail = findViewById(R.id.textEmail);
        btnSubmit = findViewById(R.id.resetButton);
        login = findViewById(R.id.txtLogin);
        back = findViewById(R.id.btnBack);
        //if user wants to cancel
        login.setOnClickListener(this::onLoginClick);
        back.setOnClickListener(this::onLoginClick);
        btnSubmit.setOnClickListener(v -> {
            //initialize input
            String email = txtEmail.getText().toString().trim();
            InputValidator validator = new InputValidator();
            if(validator.validateText(email)&&validator.validateEmail(email)){
                //Progress Bar while connection establishes
                final KProgressHUD progressDialog=  KProgressHUD.create(ForgotPasswordActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                //submit request for password reset
                submitRequest(email,progressDialog);
            }else{
                txtEmail.setError("Please enter a valid email address as used during account creation");
            }
        });
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    private void submitRequest(String email, KProgressHUD progressDialog) {
        StringRequest passwordResetStringRequest = new StringRequest(Request.Method.POST, NetworkConstants.URL_FORGOT,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("success")){
                            progressDialog.dismiss();
                            Toasty.success(ForgotPasswordActivity.this, jsonObject.getString("message"), Config.SHORT_TOAST).show();
                            Intent main = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(main);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Error", "Failed with error msg:\t" + e.getMessage());
                        Log.d("Error", "Error StackTrace: \t" + Arrays.toString(e.getStackTrace()));
                        progressDialog.dismiss();
                        Toasty.error(ForgotPasswordActivity.this,"Request Failed " + e, Config.SHORT_TOAST).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.d("Error", "Failed with error msg:\t" + error.getMessage());
                    Log.d("Error", "Error StackTrace: \t" + Arrays.toString(error.getStackTrace()));
                    // edited here
                    try {
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("Reset Exception", new String(htmlBodyBytes), error);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Toasty.error(ForgotPasswordActivity.this,"Request Error " + error, Config.SHORT_TOAST).show();
                }){
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        passwordResetStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*5,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue passwordResetRequestQueue = Volley.newRequestQueue(this);
        passwordResetRequestQueue.add(passwordResetStringRequest);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Password Reset CheckPoint","PasswordResetActivity resumed");
        //check Internet Connection
        new CheckInternetConnection(NgugeHillsLodge.getContext()).checkConnection();
    }
    @Override
    protected void onStop () {
        super.onStop();
        Log.d("Password Reset CheckPoint","PasswordResetActivity stopped");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}