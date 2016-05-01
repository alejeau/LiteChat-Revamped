package com.excilys.android.formation.litechate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.excilys.android.formation.litechate.ChatApplication;
import com.excilys.android.formation.litechate.R;
import com.excilys.android.formation.litechate.model.User;
import com.excilys.android.formation.litechate.tasks.AsyncTaskController;
import com.excilys.android.formation.litechate.tasks.LogInTask;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskController<Boolean> {
    private final String TAG = LogInActivity.class.getSimpleName();
    public final static String EXTRA_USERNAME = "com.excilys.android.formation.chatlite.USERNAME";
    public final static String EXTRA_PASSWORD = "com.excilys.android.formation.chatlite.PASSWORD";

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button clearButton;
    private Button sendButton;

    private ProgressBar progressBar;

    private User user;
    private LogInTask logInTask;


    private SharedPreferences settings;
    private boolean silentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Restore preferences
        settings = getPreferences(MODE_PRIVATE);
        this.silentMode = settings.getBoolean("silentMode", false);
        String username = settings.getString(EXTRA_USERNAME, "@string/edit_text_username");
        String password = settings.getString(EXTRA_PASSWORD, "@string/edit_text_password");
        this.user = new User(username, password);

        this.editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        this.clearButton = (Button) findViewById(R.id.buttonClear);
        this.sendButton = (Button) findViewById(R.id.buttonSend);

        this.progressBar = (ProgressBar) findViewById(R.id.loadingWheel);

        this.clearButton.setOnClickListener(this);
        this.sendButton.setOnClickListener(this);
    }


    /**
     * Checks whether the fields are valid to start a connection (i.e. not empty).
     *
     * @return true if the params are valid, false else
     */
    private boolean isValid() {
        String u = this.editTextUsername.getText().toString();
        if (u.equals("")) {
            return false;
        }

        String p = this.editTextPassword.getText().toString();
        if (p.equals("")) {
            return false;
        }
        this.user = new User(u, p);
        return true;
    }

    @Override
    public void onPreExecute() {
        // Show progress bar
        this.progressBar.setVisibility(View.VISIBLE);
        // Disable login button
        this.sendButton.setEnabled(false);
    }

    @Override
    public void onPostExecute(Boolean success) {
        if (success) {
            saveUser();
            startMainMenuActivity();
        } else {
            Toast.makeText(this, R.string.error_invalid_user, Toast.LENGTH_LONG).show();
        }
        // Hide progress bar
        this.progressBar.setVisibility(View.INVISIBLE);
        // Enable login button
        this.sendButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSend:
                if (isValid()) {
                    updateUser();
                    saveUser();
                    startLoginTask();
                } else {
                    Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_LONG).show();
                }
                break;
            case  R.id.buttonClear:
                clear();
                break;
            default:
                break;
        }
    }

    private void clear() {
        this.editTextUsername.setHint(R.string.edit_text_username);
        this.editTextPassword.setHint(R.string.edit_text_password);
        this.user = null;
    }

    private void startLoginTask() {
        if (logInTask != null && logInTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            logInTask.cancel(true);
        }
        logInTask = new LogInTask((ChatApplication) this.getApplication(), this.user);
        logInTask.setLoginTaskListener(this);
        logInTask.execute();
    }

    private void startMainMenuActivity() {
        Intent intent = new Intent(this, ViewMessagesActivity.class);
        intent.putExtra(EXTRA_USERNAME, user.getLogin());
        intent.putExtra(EXTRA_PASSWORD, user.getPassword());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUser();
        cancelTasks();
    }

    private void cancelTasks() {
        if (logInTask != null) {
            logInTask.cancel(true);
        }
    }

    private void updateUser(){
        String u = this.editTextUsername.getText().toString();
        String p = this.editTextPassword.getText().toString();
        this.user = new User(u, p);
    }

    private void saveUser() {
        Log.i(TAG, "Saving user name and password...");

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", this.silentMode);
        editor.putString(EXTRA_USERNAME, user.getLogin());
        editor.putString(EXTRA_PASSWORD, user.getPassword());

        // Commit the edits!
        editor.commit();
        Log.i(TAG, "Done.");
    }
}
