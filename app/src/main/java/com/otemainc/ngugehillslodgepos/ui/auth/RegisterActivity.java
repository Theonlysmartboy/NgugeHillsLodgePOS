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
import android.widget.CheckBox;
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
import com.otemainc.ngugehillslodgepos.network.InputValidator;
import com.otemainc.ngugehillslodgepos.network.NetworkConstants;
import com.otemainc.ngugehillslodgepos.ui.MainActivity;
import com.otemainc.ngugehillslodgepos.utils.Config;
import com.otemainc.ngugehillslodgepos.utils.Db;
import com.otemainc.ngugehillslodgepos.utils.NgugeHillsLodge;
import com.otemainc.ngugehillslodgepos.utils.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, email, phone, town, referralCode;
    private TextInputEditText password, cPassword;
    private Db myDb;
    private UserSession session;
    private CheckBox acceptPrivacy,acceptPolicy;
    private String nameText, emailText, phoneText, townText, passwordText, cPasswordText, referralCodeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor(this);
        name = findViewById(R.id.textName);
        email = findViewById(R.id.textEmail);
        phone = findViewById(R.id.textMobile);
        town = findViewById(R.id.textTown);
        password = findViewById(R.id.textPassword);
        cPassword = findViewById(R.id.textCPassword);
        referralCode = findViewById(R.id.textReferralCode);
        Button register = findViewById(R.id.registerButton);
        ImageView back = findViewById(R.id.btnBack);
        ImageView facebookRegisterBtn = findViewById(R.id.btnFacebookRegister);
        ImageView googleRegisterBtn = findViewById(R.id.btnGoogleRegister);
        TextView login = findViewById(R.id.txtLogin);
        myDb = new Db(this);
        session= new UserSession(getApplicationContext());
        TextView privacy = findViewById(R.id.privacy_policy);
        acceptPrivacy = findViewById(R.id.accept_policy);
        acceptPolicy = findViewById(R.id.accept_tlc);
        TextView terms = findViewById(R.id.terms);
        privacy.setOnClickListener(view -> {
            Intent showPrivacy = new Intent(RegisterActivity.this, PrivacyActivity.class);
            startActivity(showPrivacy);
        });
        terms.setOnClickListener(view -> {
            Intent showTerms = new Intent(RegisterActivity.this, TermsActivity.class);
            startActivity(showTerms);
        });
        login.setOnClickListener(this::onLoginClick);
        back.setOnClickListener(this::onLoginClick);
        register.setOnClickListener(v -> {
            InputValidator validator = new InputValidator();
            nameText = name.getText().toString().trim();
            emailText = email.getText().toString().trim();
            phoneText = phone.getText().toString().trim();
            townText = town.getText().toString().trim();
            passwordText = Objects.requireNonNull(password.getText()).toString().trim();
            cPasswordText = Objects.requireNonNull(cPassword.getText()).toString().trim();
            referralCodeText = referralCode.getText().toString().trim();
            if (!validator.validateText(nameText)) {
                name.setError("Name is required");
            } else if (!validator.validateText(emailText)) {
                email.setError("Email is required");
            } else if (!validator.validateText(phoneText)) {
                phone.setError("Phone number is required");
            } else if (!validator.validatePasswordLength(passwordText)) {
                password.setError("Password Should be at least 6 characters long.");
            } else if (!validator.validateText(cPasswordText)) {
                cPassword.setError("You have to confirm your password");
            } else if (!passwordText.equals(cPasswordText)) {
                password.setError("Password do not match");
                cPassword.setError("Password do not match");
            } else if (!validator.validateEmail(emailText)) {
                email.setError("Enter a valid email address ");
            } else if (!validator.validatePhone(phoneText)) {
                phone.setError("The phone number is invalid.\n Ensure your phone number is at least 10 characters long");
            } else if(!validator.validateText(townText)){
                town.setError("Enter a valid town or village name ");
            } else {
                if (acceptPolicy.isChecked() && acceptPrivacy.isChecked()) {
                    final KProgressHUD progressDialog = KProgressHUD.create(RegisterActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Creating account...\n Please wait.")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    saveUser(nameText, emailText, phoneText, townText, passwordText, referralCodeText, progressDialog);
                } else {
                    Toasty.warning(this, "You have to accept the terms and conditions and agree to the privacy policy to continue", Config.SHORT_TOAST).show();
                }
            }
        });
        facebookRegisterBtn.setOnClickListener(view -> setComingSoon("Login with Facebook option is coming " +
                "soon . Please use the default login option"));
        googleRegisterBtn.setOnClickListener(view -> setComingSoon("Login with Google option is coming soon. " +
                "Please use the default login option"));
    }
    private void setComingSoon(String text) {
        Toasty.info(NgugeHillsLodge.getContext(), text, Config.SHORT_TOAST).show();
    }
    private void saveUser(String nameText, String emailText, String phoneText, String townText, String passwordText, String referralCodeText,KProgressHUD progressDialog) {
        StringRequest registerStringRequest = new StringRequest(Request.Method.POST, NetworkConstants.URL_REGISTER,
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
                            boolean isAdded=  myDb.addUser(id, name, email, tel, token, code,town);
                            if(isAdded){
                                session.createLoginSession(name,email,tel,"");
                                progressDialog.dismiss();
                                Toasty.success(RegisterActivity.this,"Registration successful\n Welcome!!",Config.SHORT_TOAST).show();
                                Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(main);
                                finish();
                            }else{
                                Toasty.error(RegisterActivity.this, "Login failed Unable to update local db", Config.SHORT_TOAST).show();
                                progressDialog.dismiss();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toasty.error(RegisterActivity.this,"Login Failed "+registerObject.getString("msg") , Config.LONG_TOAST).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toasty.error(RegisterActivity.this,"Registration Failed " + e, Config.SHORT_TOAST).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.d("Registration Error", "Failed with error msg:\t" + error.getMessage());
                    Log.d("Registration Error", "Error StackTrace: \t" + Arrays.toString(error.getStackTrace()));
                    // edited here
                    try {
                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.e("Registration Error", new String(htmlBodyBytes), error);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    error.printStackTrace();
                    Log.d("this", "Registration Error " + error);
                    Toasty.error(RegisterActivity.this,"Registration Error " + error, Config.SHORT_TOAST).show();
                }){
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameText);
                params.put("email", emailText);
                params.put("tel", phoneText);
                params.put("town",townText);
                params.put("password", passwordText);
                params.put("referralCode", referralCodeText);
                return params;
            }
        };
        registerStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000*5,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue registerRequestQueue = Volley.newRequestQueue(this);
        registerRequestQueue.add(registerStringRequest);
    }
    private void changeStatusBarColor( Context context) {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor( context, R.color.primary));
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Registration CheckPoint","RegisterActivity resumed");
        new CheckInternetConnection(NgugeHillsLodge.getContext()).checkConnection();
    }
    @Override
    protected void onStop () {
        super.onStop();
        Log.d("Registration CheckPoint","RegisterActivity stopped");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}