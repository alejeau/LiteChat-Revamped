package com.excilys.android.formation.chatlite_rev.mappers;

import android.util.Log;

import com.excilys.android.formation.chatlite_rev.model.SimpleMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class JsonMapper {
    private static final String TAG = JsonMapper.class.getSimpleName();

    public static String mapLogIn(String response, String tag) {
        JSONObject reader = null;
        String res = null;
        try {
            reader = new JSONObject(response);
            res = reader.getString(tag);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean mapStatus(String response) {
        JSONObject reader = null;
        String res = "";
        boolean ok = false;

        try {
            reader = new JSONObject(response);
            Log.d(TAG, reader.toString());
            res = reader.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (res.equals("200")) {
            ok = true;
        }

        return ok;
    }

    public static List<SimpleMessage> mapSimpleMessages(String response) {
        LinkedList<SimpleMessage> list = new LinkedList<>();
        JSONArray ja = null;
        try {
            ja = new JSONArray(response);
            int len = ja.length();
            for (int i = 0; i < len; i++) {
                JSONObject jso = ja.getJSONObject(i);
//                Log.d(TAG, "dibug: jso = " + jso);
                list.add(toSimpleMessage(ja.getJSONObject(i)));
            }
        } catch (JSONException e) {
        }

        if (ja != null) {
        }
        return list;
    }

    private static SimpleMessage toSimpleMessage(JSONObject jso) throws JSONException {
        String login, message, uuString;
        UUID uuid;
        login = jso.getString("login");
        message = jso.getString("message");
        uuString = jso.getString("uuid");
        uuid = UUID.fromString(uuString);
//        Log.d(TAG, "dibug: h = " + h.toString());
        return new SimpleMessage(uuid, login, message);
    }

}
