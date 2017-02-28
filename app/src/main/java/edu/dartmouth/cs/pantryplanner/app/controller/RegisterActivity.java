package edu.dartmouth.cs.pantryplanner.app.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.model.User;
import edu.dartmouth.cs.pantryplanner.app.util.EmailValidator;
import lombok.AllArgsConstructor;

/**
 * A register screen that offers registration with user information.
 */
public class RegisterActivity extends AppCompatActivity {
    private UserRegisterTask mRegisterTask = null;

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;

    private View mFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstNameView  = (EditText) findViewById(R.id.register_activity_first_name);
        mLastNameView   = (EditText) findViewById(R.id.register_activity_last_name);
        mEmailView      = (EditText) findViewById(R.id.register_activity_email);
        mPasswordView   = (EditText) findViewById(R.id.register_activity_password);
        mRePasswordView = (EditText) findViewById(R.id.register_activity_re_enter_password);

        mFormView       = findViewById(R.id.register_activity_form);
        mProgressView   = findViewById(R.id.register_activity_progress);

        findViewById(R.id.register_activity_register).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptRegister();
                    }
                }
        );
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);

        // Store values at the time of the login attempt.
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = mRePasswordView.getText().toString();

        // Register form check logic
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError("First name cannot be empty");
            mFirstNameView.requestFocus();
        } else if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError("Last name cannot be empty");
            mLastNameView.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email cannot be empty");
            mEmailView.requestFocus();
        } else if (!EmailValidator.validate(email)) {
            mEmailView.setError("This email address is invalid");
            mEmailView.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            mPasswordView.setError("Password cannot be empty");
            mPasswordView.requestFocus();
        } else if (!TextUtils.equals(password, rePassword)) {
            mRePasswordView.setError("Re-entered password doesn't match password");
            mRePasswordView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);
            mRegisterTask = new UserRegisterTask(new User(firstName, lastName, email, password));
            mRegisterTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the register form.
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
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final User mUser;

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                Toast.makeText(
                        RegisterActivity.this,
                        "Registration failed. Please check your network.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}

