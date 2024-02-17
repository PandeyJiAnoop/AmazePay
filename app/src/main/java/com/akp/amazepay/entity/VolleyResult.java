package com.akp.amazepay.entity;

import com.android.volley.VolleyError;

import org.json.JSONObject;
/**
 * Created by Anoop Pandey on 9696381023.
 */
public interface VolleyResult {
    public void volleySuccess(String requestType, JSONObject response);
    public void volleyError(String requestType, VolleyError error);
}
