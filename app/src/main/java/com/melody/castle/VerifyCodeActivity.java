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
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.melody.castle.ServerUtil.ResultCallBack;
import com.melody.castle.ServerUtil.ServerManager;
import com.melody.castle.utils.DataUtils;

import org.json.JSONObject;

public class VerifyCodeActivity extends AppCompatActivity implements ResultCallBack {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mVerifyCodeView;
    private View mProgressView;
    private View mVerifyFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifycode);

        // Set up the forgot form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mVerifyCodeView = (EditText) findViewById(R.id.vcode);
        mVerifyCodeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptVerify();
                    return true;
                }
                return false;
            }
        });

        Button mSendButton = (Button) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerify();
            }
        });

        mVerifyFormView = findViewById(R.id.verify_form);
        mProgressView = findViewById(R.id.verify_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptVerify() {

        // Reset errors.
        mEmailView.setError(null);
        mVerifyCodeView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String vcode = mVerifyCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(vcode)) {
            mVerifyCodeView.setError(getString(R.string.error_field_required));
            focusView = mVerifyCodeView;
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
            showProgress(true);
            ServerManager.confirmEmail(email, vcode, this);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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

            mVerifyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mVerifyFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mVerifyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mVerifyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void doAction(String result) {

        showProgress(false);
        try {
            JSONObject json = new JSONObject(result);
            String ret = json.getString(Global.KEY_RESULT);
            if (ret.equals(Global.KEY_SUCCESS)) {
                String token = json.getString(Global.KEY_DATA);
                DataUtils.savePreference(Global.KEY_TOKEN, token);
                Intent i = new Intent(VerifyCodeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            else {
                int code = json.getInt(Global.KEY_CODE);
                if ( code == -1 ) {//Invalid user!
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();
                }
                else if ( code == -2 ) {//Invalid vcode!
                    mVerifyCodeView.setError(getString(R.string.error_invalid_vcode));
                    mVerifyCodeView.requestFocus();
                }
            }
        } catch (Exception e) {
        }
    }

}


