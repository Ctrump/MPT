package com.example.cenoo.ms_detect_text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    public static double getDouble(String jsonString){
        try {
            JSONObject documents = new JSONObject(jsonString);
            JSONArray array = documents.getJSONArray("documents");
            JSONObject object = array.getJSONObject(0);
            return object.getDouble("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1.0;
    }
}
