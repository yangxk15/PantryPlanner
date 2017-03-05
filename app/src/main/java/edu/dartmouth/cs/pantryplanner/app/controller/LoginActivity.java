package edu.dartmouth.cs.pantryplanner.app.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.util.EmailValidator;
import edu.dartmouth.cs.pantryplanner.app.util.RequestCode;
import edu.dartmouth.cs.pantryplanner.app.util.ServiceBuilderHelper;
import edu.dartmouth.cs.pantryplanner.app.util.Session;
import edu.dartmouth.cs.pantryplanner.backend.entity.user.User;
import edu.dartmouth.cs.pantryplanner.backend.registration.Registration;
import lombok.AllArgsConstructor;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mLoginTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    private View mFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView      = (EditText) findViewById(R.id.login_activity_email);
        mPasswordView   = (EditText) findViewById(R.id.login_activity_password);

        mFormView       = findViewById(R.id.login_activity_form);
        mProgressView   = findViewById(R.id.login_activity_progress);

        findViewById(R.id.login_activity_login).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                }
        );

        findViewById(R.id.login_activity_register).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(
                                new Intent(LoginActivity.this, RegisterActivity.class),
                                RequestCode.REGISTER.ordinal()
                        );
                    }
                }
        );

        String email = new Session(this).getString("email");
        String password = new Session(this).getString("password");
        if (email != null && password != null) {
            mEmailView.setText(email);
            mPasswordView.setText(password);
            new UserLoginTask(email, password).execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.REGISTER.ordinal()) {
                String email = data.getStringExtra("email");
                String password = data.getStringExtra("password");
                mEmailView.setText(email);
                mPasswordView.setText(password);
                new UserLoginTask(email, password).execute();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mLoginTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email cannot be empty");
            mEmailView.requestFocus();
        } else if (!EmailValidator.validate(email)) {
            mEmailView.setError("This email address is invalid");
            mEmailView.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password cannot be empty");
            mPasswordView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mLoginTask = new UserLoginTask(email, password);
            mLoginTask.execute((Void) null);
        }
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

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @AllArgsConstructor(suppressConstructorProperties = true)
    private class UserLoginTask extends AsyncTask<Void, Void, IOException> {

        String mEmail;
        String mPassword;

        @Override
        protected IOException doInBackground(Void... params) {
            IOException ex = null;

            try {
                User userService = ServiceBuilderHelper.getBuilder(
                        LoginActivity.this,
                        User.Builder.class
                ).build();

                userService.login(mEmail, mPassword).execute();
            } catch (IOException e) {
                ex = e;
            }

            return ex;
        }

        @Override
        protected void onPostExecute(IOException ex) {
            mLoginTask = null;
            showProgress(false);

            if (ex == null) {
                new Session(LoginActivity.this).putString("email", mEmail);
                new Session(LoginActivity.this).putString("password", mPassword);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                if (ex instanceof GoogleJsonResponseException) {
                    GoogleJsonError error = ((GoogleJsonResponseException) ex).getDetails();
                    if (ex.getMessage().contains("email")) {
                        mEmailView.setError(error.getMessage());
                        mEmailView.requestFocus();
                    } else if (ex.getMessage().contains("password")) {
                        mPasswordView.setError(error.getMessage());
                        mPasswordView.requestFocus();
                    }
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Please check your internet connection and restart the app",
                            Toast.LENGTH_LONG
                    ).show();
                }
                Log.d(this.getClass().getName(), ex.toString());
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
            showProgress(false);
        }
    }
}

