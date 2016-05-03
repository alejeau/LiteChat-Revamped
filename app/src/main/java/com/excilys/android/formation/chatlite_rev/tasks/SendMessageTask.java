package com.excilys.android.formation.chatlite_rev.tasks;

import com.excilys.android.formation.chatlite_rev.ChatApplication;
import com.excilys.android.formation.chatlite_rev.connection.ChatAPIService;
import com.excilys.android.formation.chatlite_rev.model.SimpleMessage;

public class SendMessageTask extends android.os.AsyncTask<SimpleMessage, Boolean, Boolean> {

    private final String TAG = SendMessageTask.class.getSimpleName();
    private ChatAPIService chatAPIService;
    private SendMessageTaskController l;

    public SendMessageTask(ChatApplication app) {
        this.chatAPIService = app.getService();
    }

    public interface SendMessageTaskController {
        void onPreExecuteSendMessages();
        void onPostExecuteSendMessages(Boolean success);
    }

    public void setSendMessageTaskListener(SendMessageTaskController l) {
        this.l = l;
    }

    @Override
    protected void onPreExecute() {
        if (l != null) {
            l.onPreExecuteSendMessages();
        }
    }

    @Override
    protected Boolean doInBackground(SimpleMessage... params) {
        boolean success = chatAPIService.sendMessage(params[0]);
        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (l != null) {
            l.onPostExecuteSendMessages(success);
        }
    }
}
