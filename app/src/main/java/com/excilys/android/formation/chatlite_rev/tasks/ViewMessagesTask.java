package com.excilys.android.formation.chatlite_rev.tasks;


import com.excilys.android.formation.chatlite_rev.ChatApplication;
import com.excilys.android.formation.chatlite_rev.connection.ChatAPIService;

public class ViewMessagesTask extends android.os.AsyncTask<String, Void, String> {

    private final String TAG = ViewMessagesTask.class.getSimpleName();
    private ChatAPIService chatAPIService;
    private ViewMessagesTaskController l;

    public interface ViewMessagesTaskController {
        void onPreExecuteViewMessages();
        void onPostExecuteViewMessages(String messages);
    }

    public void setViewMessagesTaskListener(ViewMessagesTaskController l) {
        this.l = l;
    }

    public ViewMessagesTask(ChatApplication app) {
        this.chatAPIService = app.getService();
    }

    @Override
    protected void onPreExecute() {
        if (l != null) {
            l.onPreExecuteViewMessages();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String result = chatAPIService.getMessages(params[0], params[1]);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (l != null) {
            l.onPostExecuteViewMessages(result);
        }
    }
}
