package com.excilys.android.formation.litechate.tasks;

import com.excilys.android.formation.litechate.ChatApplication;
import com.excilys.android.formation.litechate.connection.ChatAPIService;
import com.excilys.android.formation.litechate.model.User;

/**
 * Created by excilys on 29/04/16.
 */
public class LogInTask extends android.os.AsyncTask<User, Void, Boolean> {
    private final String TAG = LogInTask.class.getSimpleName();
    private ChatAPIService chatAPIService;
    private User user;
    private AsyncTaskController<Boolean> l;

    public LogInTask(ChatApplication app, User u) {
        this.user = u;
        this.chatAPIService = app.getService();
    }

    public void setLoginTaskListener(AsyncTaskController l) {
        this.l = l;
    }

    @Override
    protected void onPreExecute() {
        if (l != null) {
            l.onPreExecute();
        }
    }

    @Override
    protected Boolean doInBackground(User... params) {
        boolean success = chatAPIService.isValidUser(this.user);
        return success;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (l != null) {
            l.onPostExecute(result);
        }
    }
}
