package com.example.moneyjeju.MONEY;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Trip_detailsRequest extends StringRequest {
    final static private String URL = "http://192.168.0.8/Trip_details.php";
    private Map<String, String> parameters;

    public Trip_detailsRequest(String userID, String place_name, String date_go, String friend_no, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("placename", place_name);
        parameters.put("userdate", date_go);
        parameters.put("userfriend", ""+friend_no);
    }

    protected Map<String, String>getParams() throws AuthFailureError {
        return parameters;
    }
}