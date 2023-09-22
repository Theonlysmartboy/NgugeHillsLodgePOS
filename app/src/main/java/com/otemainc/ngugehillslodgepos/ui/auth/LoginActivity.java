package com.otemainc.ngugehillslodgepos.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.otemainc.ngugehillslodgepos.R;
import com.otemainc.ngugehillslodgepos.network.CheckInternetConnection;
import com.otemainc.ngugehillslodgepos.ui.MainActivity;
import com.otemainc.ngugehillslodgepos.utils.Config;
import com.otemainc.ngugehillslodgepos.utils.Db;
import com.otemainc.ngugehillslodgepos.utils.NgugeHillsLodge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import com.otemainc.ngugehillslodgepos.network.NetworkConstants;
import com.otemainc.ngugehillslodgepos.network.InputValidator;
import com.otemainc.ngugehillslodgepos.utils.UserSession;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private TextInputEditText password;
    private String emailText, passwordText;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor(NgugeHillsLodge.getContext());
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.loginButton);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView registerNow = findViewById(R.id.registerNow);
        ImageView addBtn = findViewById(R.id.addBtn);
        ImageView facebookBtn = findViewById(R.id.facebookBtn);
        ImageView googleBtn = findViewById(R.id.googleBtn);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        session= new UserSession(getApplicationContext());
        //on click listeners
        login.setOnClickListener(v -> {
            emailText = email.getText().toString().trim();
            passwordText = Objects.requireNonNull(password.getText()).toString().trim();
            InputValidator validator = new InputValidator();
            if (!validator.validateText(emailText)){
                email.setError("Email Or Phone required");
            }
            else if(!validator.validateEmail(emailText)&&!validator.validatePhone(emailText)){
                email.setError("Invalid Phone number or email address");
            }
            else if(!validator.validatePasswordLength(passwordText)){
                password.setError("Password Should be at least 6 characters long ");
            }
            else{
                //Progress Bar while connection establishes
                final KProgressHUD progressDialog=  KProgressHUD.create(LoginActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Authenticating... \n Please wait")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
                performLogin(emailText,passwordText,progressDialog);
            }
        });
        forgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        });
        registerNow.setOnClickListener(v -> onRegisterClick());
        addBtn.setOnClickListener(v -> onRegisterClick());
        facebookBtn.setOnClickListener(view -> setComingSoon("Login with Facebook option is coming " +
                "soon . Please use the default login option"));
        googleBtn.setOnClickListener(view -> setComingSoon("Login with Google option is coming soon. " +
                "Please use the default login option"));
    }
    private void performLogin(String emailText, String passwordText, KProgressHUD progressDialog) {
        StringRequest loginStringRequest = new StringRequest(Request.Method.POST, NetworkConstants.URL_LOGIN,
                response -> {
                    try {
                        JSONObject registerObject = new JSONObject(response);
                        if(registerObject.getBoolean("success")){
                            JSONObject object = registerObject.getJSONObject("user");
                            final  int id = object.getInt("id".trim());
                            final String name = object.getString("name").trim();
                            final String tel = object.getString("tel".trim());
                            final String email = object.getString("email").trim();
                            final String town = object.getString("town").trim();
                            final String token = object.getString("api_token").trim();
                            final String code = object.getString("referral_code").trim();
                            boolean isAdded = new Db(this).addUser(id, name, email, tel, token, code, town);
                            if (isAdded) {
                                session.createLoginSession(name,email,tel,"");
                                progressDialog.dismiss();
                                if(object.getInt("temp_password")!=1) {
                                    Toasty.success(this,"Login successful\n Welcome!!",Config.LONG_TOAST).show();
                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(main);
                                }else{
                                    Toasty.success(this,"Login successful\n please change your password before proceeding",Config.LONG_TOAST).show();
                                    Intent reset = new Intent(LoginActivity.this, ProfileActivity.class);
                                    startActivity(reset);
                                }
                                finish();
                            } else {
                                Toasty.error(this,"Login failed Unable to update local db",Config.LONG_TOAST).show();
                                progressDialog.dismiss();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toasty.error(LoginActivity.this,"Login Failed Invalid email, phone number or Password" , Config.LONG_TOAST).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toasty.error(LoginActivity.this,"Login Failed " + e, 5000).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.d("Login Error", "Failed with error msg:\t" + error.getMessage());
                    Log.d("Login Error", "Error StackTrace: \t" + Arrays.toString(error.getStackTrace()));
                    // edited here
                    try {
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("Login Error", new String(htmlBodyBytes), error);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Toasty.error(LoginActivity.this,"Login Error " + error, 5000).show();
                }){
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailText);
                params.put("password", passwordText);
                return params;
            }
        };
        loginStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*5,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        RequestQueue loginRequestQueue = Volley.newRequestQueue(this);
        loginRequestQueue.add(loginStringRequest);
    }
    private void setComingSoon(String text) {
        Toasty.info(NgugeHillsLodge.getContext(), text, Config.SHORT_TOAST).show();
    }
    public void onRegisterClick(){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    private void changeStatusBarColor( Context context) {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor( context, R.color.primary));
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Login CheckPoint","LoginActivity resumed");
        //check Internet Connection
        new CheckInternetConnection(NgugeHillsLodge.getContext()).checkConnection();
    }
    @Override
    protected void onStop () {
        super.onStop();
        Log.d("Login CheckPoint","LoginActivity stopped");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}