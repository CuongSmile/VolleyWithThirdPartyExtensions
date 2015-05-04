package com.smile.volleythirdpartylibraryextension.base;

import android.content.Context;

import com.android.volley.VolleyError;

public interface VolleyActionListener<T> {
	void onBeforeRequest(Context pContext);
	
	void onResponse(T response, int statusCode, VolleyError error);

	void onNoNetwork(Context pContext);
}
