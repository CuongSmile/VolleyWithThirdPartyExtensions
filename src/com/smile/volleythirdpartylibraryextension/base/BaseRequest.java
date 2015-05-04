package com.smile.volleythirdpartylibraryextension.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

public abstract class BaseRequest<T> extends Request<T>{

	private Map<String, String> mParams;
	private List<Integer> mAcceptedStatusCodes;

	public BaseRequest(int method, String url,int timeOut) {
		this(method, url, null,timeOut);
	}

	public BaseRequest(int method, String url, Map<String, String> params ,int timeout){
		super(method, url, null);
		setShouldCache(false);
		mAcceptedStatusCodes = new ArrayList<Integer>();
		mAcceptedStatusCodes.add(HttpStatus.SC_OK);
		mAcceptedStatusCodes.add(HttpStatus.SC_NO_CONTENT);

		setRetryPolicy(new DefaultRetryPolicy(timeout, 1, 1));

		if (method == Method.POST || method == Method.PUT) {
			mParams = params;
		}
	}


	/**
	 * Allows you to add additional status codes (besides 200 and 204) that will be parsed.
	 *
	 * @param statusCodes An array of additional status codes to parse network responses for
	 */
	public void addAcceptedStatusCodes(int[] statusCodes) {
		for (int statusCode : statusCodes) {
			mAcceptedStatusCodes.add(statusCode);
		}
	}

	/**
	 * Gets all status codes that will be parsed as successful (Note: some {@link com.android.volley.toolbox.HttpStack}
	 * implementations, including the default, may not allow certain status codes to be parsed. To get around this
	 * limitation, use a custom HttpStack, such as the one provided with the excellent OkHttp library
	 *
	 * @return A list of all status codes that will be counted as successful
	 */
	public List<Integer> getAcceptedStatusCodes() {
		return mAcceptedStatusCodes;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");

		if (getMethod() == Method.POST || getMethod() == Method.PUT) {
			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf8");
		}

		return headers;
	}

	@Override
	public Map<String, String> getParams() {
		return mParams;
	}
}
