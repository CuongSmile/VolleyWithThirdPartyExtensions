package com.smile.volleythirdpartylibraryextension.base;

import com.android.volley.VolleyError;

import android.content.Context;

public abstract class VolleyAbstractAction<T> implements VolleyActionListener<T> {
	
	@Override
	public void onBeforeRequest(Context pContext) {
		LoadingDialogController.getInstance().show(pContext);
	}
	@Override
	public void onResponse(T response, int statusCode, VolleyError error) {
		LoadingDialogController.getInstance().dismiss();
	}
	@Override
	public void onNoNetwork(Context pContext) {
		
	}
}
