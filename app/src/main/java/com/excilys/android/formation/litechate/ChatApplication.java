package com.excilys.android.formation.litechate;

import android.app.Application;

import com.excilys.android.formation.litechate.connection.ChatAPIService;

/**
 * Created by excilys on 29/04/16.
 */
public class ChatApplication extends Application {

    private ChatAPIService chatAPIService;


    @Override
    public void onCreate() {
        super.onCreate();
        chatAPIService = new ChatAPIService(this.getApplicationContext());
    }

    public ChatAPIService getService() {
        return chatAPIService;
    }
}
