package com.excilys.android.formation.chatlite_rev.adapters;

/**
 * Created by excilys on 03/05/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.excilys.android.formation.chatlite_rev.R;
import com.excilys.android.formation.chatlite_rev.model.SimpleMessage;

import java.util.Collections;
import java.util.List;

public class SimpleMessageAdapter extends BaseAdapter {

    private List<SimpleMessage> simpleMessagesList = Collections.emptyList();

    private final Context context;
    private String currentLogin;

    // the context is needed to inflate views in getView()
    public SimpleMessageAdapter(Context context, String currentLogin) {
        this.context = context;
        this.currentLogin = currentLogin;
    }

    public void updateMessages(List<SimpleMessage> simpleMessagesList) {
        ThreadPreconditions.checkOnMainThread();
        this.simpleMessagesList = simpleMessagesList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return simpleMessagesList.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public SimpleMessage getItem(int position) {
        return simpleMessagesList.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SimpleMessage simpleMessage = getItem(position);
//        String login = simpleMessage.getLogin();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_message_view, parent, false);
        }

        TextView loginView;
        TextView messageView;
        loginView = (TextView) convertView.findViewById(R.id.simple_message_login);
        messageView = (TextView) convertView.findViewById(R.id.simple_message_text_message);

        loginView.setText(simpleMessage.getLogin());
//        if (login.equals(this.currentLogin)) {
//            loginView.setTextColor("#2EFF2E");
//        }
        messageView.setText(simpleMessage.getMessage());

        return convertView;
    }

}

