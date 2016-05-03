package com.excilys.android.formation.chatlite_rev.tasks;

import android.app.Notification;

import com.excilys.android.formation.chatlite_rev.ChatApplication;
import com.excilys.android.formation.chatlite_rev.connection.ChatAPIService;
import com.excilys.android.formation.chatlite_rev.enums.NetworkAction;
import com.excilys.android.formation.chatlite_rev.model.User;

/**
 * Created by excilys on 29/04/16.
 */
public class LogInTask extends android.os.AsyncTask<NetworkAction, Void, Boolean> {
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
    protected Boolean doInBackground(NetworkAction... params) {
        NetworkAction n = params[0];
        boolean success = false;
        switch(n){
            case IS_VALID_USER:
                success = chatAPIService.isValidUser(this.user);
                break;
            case LOG_IN:
//                success = chatAPIService. (this.user);
                break;
            case REGISTER:
                success = chatAPIService.register(this.user);
                break;
            case CLEAR:
                chatAPIService.reset();
                success = true;
                break;
            default:
                break;
        }

        return success;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (l != null) {
            l.onPostExecute(result);
        }
    }
}
