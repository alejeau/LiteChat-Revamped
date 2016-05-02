package com.excilys.android.formation.chatlite_rev.connection;

import android.content.Context;
import android.util.Log;

import com.excilys.android.formation.chatlite_rev.mappers.JsonMapper;
import com.excilys.android.formation.chatlite_rev.mappers.jackson.JacksonMapper;
import com.excilys.android.formation.chatlite_rev.model.SimpleMessage;
import com.excilys.android.formation.chatlite_rev.model.User;

import java.io.IOException;
import java.net.InetAddress;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class ChatAPIService {
    private static final String TAG = ChatAPIService.class.getSimpleName();
    private static final String ACCESS_URL = "https://training.loicortola.com/chat-rest/2.0";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String login;
    private String password;
    OkHttpClient client = null;

    public ChatAPIService(Context ctx) {
        this.client = new OkHttpClient.Builder().build();
    }

    public static class Builder {
        private ChatAPIService ok;
        private Builder(Context ctx) {
//            ok = new ChatAPIService();
        }
    }

    public void reset() {
        this.login = null;
        this.password = null;
    }

    public void authenticate() {
        client = new OkHttpClient.Builder()
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(login, password);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();
    }

    public boolean isValidUser(User u) {
        this.login = u.getLogin();
        this.password = u.getPassword();

        final String credentials = Credentials.basic(login, password);
        Log.d(TAG, "Will call URL " + ACCESS_URL + "/connect");
        Log.d(TAG, "With credentials " + credentials);
        Request request = new Request.Builder().url(ACCESS_URL + "/connect")
                .header("Authorization", Credentials.basic(login, password))
                .build();

        try {
            // If auth is valid
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {

                // We authenticate
                authenticate();
                return true;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
        }
        return false;
    }

    public String getMessages(String limit, String offset) {
        String url = ACCESS_URL + "/messages?limit=" + limit + "&offset=" + offset;
        return getResult(url);
    }

    public boolean sendMessage(SimpleMessage message) {
        String url = ACCESS_URL + "/messages";
        String json = JacksonMapper.simpleMessageToJSONString(message);
        String res = sendRequest(url, json);
        return JsonMapper.mapStatus(res);
    }

    public boolean register(User u) {
        client = new OkHttpClient();
        String url = ACCESS_URL + "/register";
        String json = JacksonMapper.userToJSONString(u);
        String res = sendRequest(url, json);
        return JsonMapper.mapStatus(res);
    }

    /**
     * Sends a GET request to a server.
     *
     * @param url the URL to reach to.
     * @return the server's response as a JSON String.
     */
    private String getResult(String url) {
        String res = "";
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            response = client.newCall(request).execute();
            res = response.body().string();
            Log.d(TAG, "dibug: resRes = " + res);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return res;
    }

    /**
     * Sends a POST request to a server.
     *
     * @param url the URL to reach to.
     * @param jsonString the JSON object to send as a JSON String.
     * @return the server's response as a JSON String.
     */
    private String sendRequest(String url, String jsonString) {
        String res = "";
        Response response = null;
        RequestBody body = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
            Log.d(TAG, "dibug: resReq = \"" + jsonString + "\"");
            res = response.body().string();
            Log.d(TAG, "dibug: resReq = " + res);
            return res;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return res;
    }

    /**
     * Checks whether the network and the website are available or not.
     *
     * @return true if available, false else.
     */
    public static boolean checkOnlineAvailability() {
        boolean available = false;
        try {
            available = InetAddress.getByName(ACCESS_URL).isReachable(30);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return available;
    }
}

