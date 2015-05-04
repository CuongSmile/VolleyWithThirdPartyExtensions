package com.smile.volleythirdpartylibraryextension.volleystring;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.smile.volleythirdpartylibraryextension.base.BaseRequest;
import com.smile.volleythirdpartylibraryextension.base.VolleyActionListener;

public class StringRequest extends BaseRequest<String> {
	private VolleyActionListener mVolleyActionListener;

	public StringRequest(int method, String url, Map<String, String> params,
			int timeout, VolleyActionListener listener) {
		super(method, url, params, timeout);
		this.mVolleyActionListener = listener;
	}

	public StringRequest(int method, String url, int timeOut,
			VolleyActionListener<String> pVolleyActionListener) {
		super(method, url, timeOut);
		this.mVolleyActionListener = pVolleyActionListener;
	}

	@Override
	protected void deliverResponse(String response) {
		if (this.mVolleyActionListener != null) {
			this.mVolleyActionListener.onResponse(response, HttpStatus.SC_OK,
					null);
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		super.deliverError(error);
		int statusCode;
		if (error != null && error.networkResponse != null) {
			statusCode = error.networkResponse.statusCode;
		} else {
			statusCode = 0;
		}
		if (mVolleyActionListener != null) {
			mVolleyActionListener.onResponse(null, statusCode, error);
		}
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}
}
