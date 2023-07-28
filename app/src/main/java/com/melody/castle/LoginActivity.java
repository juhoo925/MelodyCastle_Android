package com.melody.castle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.melody.castle.ServerUtil.ResultCallBack;
import com.melody.castle.ServerUtil.ServerManager;
import com.melody.castle.utils.DataUtils;

import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ResultCallBack {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
//                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(i);
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
            }
        });

        Button mForgotButton = (Button) findViewById(R.id.forgot_button);
        mForgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            DataUtils.savePreference(Global.KEY_PASSWORD, password);
            showProgress(true);
            ServerManager.doLogin(email, password, this);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void doAction(String result) {

        showProgress(false);

        try {
            JSONObject json = new JSONObject(result);
            String ret = json.getString(Global.KEY_RESULT);
            if (ret.equals(Global.KEY_SUCCESS)) {

                String data = json.getString(Global.KEY_DATA);
                JSONObject json2 = new JSONObject(data);
                int uid = json2.getInt("id");
                String uname = json2.getString(Global.KEY_UNAME);
                String email = json2.getString(Global.KEY_EMAIL);
                String phone = json2.getString(Global.KEY_PHONE);
                String token = json2.getString(Global.KEY_TOKEN);
                String pushkey = json2.getString(Global.KEY_PUSHKEY);
                int valid = json2.getInt(Global.KEY_VALID);
                int verifyCode = json2.getInt(Global.KEY_VCODE);
                String paypal = json2.getString(Global.KEY_PAYPAL);
                String language = json2.getString(Global.KEY_LANGUAGE);
                String country = json2.getString(Global.KEY_COUNTRY);

                DataUtils.savePreference(Global.KEY_UID, uid);
                DataUtils.savePreference(Global.KEY_UNAME, uname);
                DataUtils.savePreference(Global.KEY_EMAIL, email);
                DataUtils.savePreference(Global.KEY_TOKEN, token);
                DataUtils.savePreference(Global.KEY_VALID, valid);
                DataUtils.savePreference(Global.KEY_VCODE, verifyCode);
                if ( phone != null ) {
                    DataUtils.savePreference(Global.KEY_PHONE, phone);
                }
                if ( pushkey != null ) {
                    DataUtils.savePreference(Global.KEY_PUSHKEY, pushkey);
                }
                if ( paypal != null ) {
                    DataUtils.savePreference(Global.KEY_PAYPAL, paypal);
                }
                if ( language != null ) {
                    DataUtils.savePreference(Global.KEY_LANGUAGE, language);
                }
                if ( country != null ) {
                    DataUtils.savePreference(Global.KEY_COUNTRY, country);
                }

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
            else {
                int code = json.getInt(Global.KEY_CODE);
                if ( code == -1 ) {//Invalid user!
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();
                }
                else if ( code == -2 ) {//Invalid Password!
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
                else if ( code == -3 ) {//This user was not verified! Please check the user email.
                    Intent i = new Intent(LoginActivity.this, VerifyCodeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        } catch (Exception e) {
        }
    }

}

