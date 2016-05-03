package com.excilys.android.formation.chatlite_rev.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.excilys.android.formation.chatlite_rev.ChatApplication;
import com.excilys.android.formation.chatlite_rev.R;
import com.excilys.android.formation.chatlite_rev.adapters.SimpleMessageAdapter;
import com.excilys.android.formation.chatlite_rev.mappers.JsonMapper;
import com.excilys.android.formation.chatlite_rev.model.SimpleMessage;
import com.excilys.android.formation.chatlite_rev.tasks.SendMessageTask;
import com.excilys.android.formation.chatlite_rev.tasks.ViewMessagesTask;

import java.util.List;

public class ViewMessagesActivity extends AppCompatActivity implements View.OnClickListener, SendMessageTask.SendMessageTaskController, ViewMessagesTask.ViewMessagesTaskController {
    private ListView listView;
    private String user;
    private String pass;

    private EditText editTextMessage;

    private Button buttonSendMessage;

    private SendMessageTask sendMessageTask;
    private ViewMessagesTask viewMessagesTask;

    private SimpleMessageAdapter simpleMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("View messages");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        this.user = intent.getStringExtra(LogInActivity.EXTRA_LOGIN);
        this.pass = intent.getStringExtra(LogInActivity.EXTRA_PASSWORD);

        this.editTextMessage = (EditText) findViewById(R.id.edit_text_fragment);

        this.buttonSendMessage = (Button) findViewById(R.id.button_send_message);

        simpleMessageAdapter = new SimpleMessageAdapter(this, this.user);

        listView = (ListView) findViewById(R.id.listViewViewMessages);
        this.listView.setAdapter(simpleMessageAdapter);
        this.buttonSendMessage.setOnClickListener(this);
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Loads the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                refresh();
                return true;
            case R.id.menuLogout:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Refreshes the list of messages
     */
    public void refresh() {
        startViewMessageTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_message:
                if (isNotEmpty()) {
                    startSendMessageTask();
                } else {
                    Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPreExecuteViewMessages() {
    }

    @Override
    public void onPostExecuteViewMessages(String messages) {
        List<SimpleMessage> simpleMessageList = JsonMapper.mapSimpleMessages(messages);
        this.simpleMessageAdapter.updateMessages(simpleMessageList);
    }

    @Override
    public void onPreExecuteSendMessages() {
        // Disable sendMessage button
        this.editTextMessage.setText("");
        this.buttonSendMessage.setEnabled(false);
    }

    @Override
    public void onPostExecuteSendMessages(Boolean success) {
        // Enable sendMessage button
        this.buttonSendMessage.setEnabled(true);
        refresh();
    }

    private boolean isNotEmpty() {
        String s = this.editTextMessage.getText().toString();
        if (s.isEmpty()) {
            return false;
        }

        return true;
    }

    private void startSendMessageTask() {
        if (sendMessageTask != null && sendMessageTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            sendMessageTask.cancel(true);
        }
        sendMessageTask = new SendMessageTask((ChatApplication) this.getApplication());
        sendMessageTask.setSendMessageTaskListener(this);

        String s = this.editTextMessage.getText().toString();
        SimpleMessage sm = new SimpleMessage(this.user, s);
        sendMessageTask.execute(sm);
    }

    private void startViewMessageTask() {
        String limit = "1000", offset = "0";
        if (viewMessagesTask != null && viewMessagesTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            viewMessagesTask.cancel(true);
        }
        viewMessagesTask = new ViewMessagesTask((ChatApplication) this.getApplication());
        viewMessagesTask.setViewMessagesTaskListener(this);
        viewMessagesTask.execute(limit, offset);
    }
}
