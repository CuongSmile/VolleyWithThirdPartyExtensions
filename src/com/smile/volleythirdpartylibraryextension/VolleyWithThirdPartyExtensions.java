package com.smile.volleythirdpartylibraryextension;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.type.JavaType;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.smile.volleythirdpartylibraryextension.base.ConfigBuilder;
import com.smile.volleythirdpartylibraryextension.base.VolleyActionListener;
import com.smile.volleythirdpartylibraryextension.jackson.JacksonRequest;
import com.smile.volleythirdpartylibraryextension.jackson.JacksonRequestListener;
import com.smile.volleythirdpartylibraryextension.volleystring.StringRequest;

public class VolleyWithThirdPartyExtensions {
	private static VolleyWithThirdPartyExtensions INSTANCE = null;
	private RequestQueue mRequestQueue;
	private ConfigBuilder mVolleyConfigBuilder;

	private VolleyWithThirdPartyExtensions() {
	}

	public synchronized static VolleyWithThirdPartyExtensions getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VolleyWithThirdPartyExtensions();
		}
		return INSTANCE;
	}

	public boolean isConnectNetWork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	private RequestQueue newRequestQueue(Context context) {
		HttpStack stack;
		if (mVolleyConfigBuilder == null) {
			mVolleyConfigBuilder = new ConfigBuilder(context);
		}
		if (Build.VERSION.SDK_INT >= 9) {
			stack = new HurlStack();
		} else {
			stack = new HttpClientStack(
					AndroidHttpClient.newInstance(mVolleyConfigBuilder
							.getUserAgent()));
		}
		return newRequestQueue(context, stack);
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link com.android.volley.toolbox.HttpStack} to use for
	 *            handling network calls
	 * @return A started {@link RequestQueue} instance.
	 */
	private RequestQueue newRequestQueue(Context pContext, HttpStack stack) {
		// RequestQueue queue = new RequestQueue(new DiskBasedCache(new
		// File(context.getCacheDir(), "volley")), new JacksonNetwork(stack));
		Cache cache = new DiskBasedCache(new File(
				mVolleyConfigBuilder.getRootDirectory()),
				mVolleyConfigBuilder.getMaxCacheSizeInBytes());
		Network network = new BasicNetwork(stack);
		RequestQueue pRequestQueue = new RequestQueue(cache, network);
		pRequestQueue.start();
		return pRequestQueue;
	}

	public void setVolleyConfigBuilder(ConfigBuilder pJacksonVolleyConfigBuilder) {
		this.mVolleyConfigBuilder = pJacksonVolleyConfigBuilder;
	}

	public ConfigBuilder getVolleyConfigBuilder() {
		return mVolleyConfigBuilder;
	}

	public void build(Context pContext) {
		this.mRequestQueue = this.newRequestQueue(pContext);
	}

	public <T> void doRequestGETMethodWithJackson(Context pContext,
			String pUrl, final Class<T> pClass,
			final VolleyActionListener<T> listener) {
		this.doRequestWithJackson(pContext, pUrl, Request.Method.GET, null,
				pClass,ThirdPartyType.JACKSON, listener);
	}

	public <T> void doRequestPOSTMethodWithJackson(Context pContext,
			String pUrl, final Class<T> pClass, Map<String, String> params,
			final VolleyActionListener<T> listener) {
		this.doRequestWithJackson(pContext, pUrl, Request.Method.POST, params,
				pClass,ThirdPartyType.JACKSON ,listener);
	}
	
	public <T> void doRequestGETMethod(Context pContext,
			String pUrl, 
			final VolleyActionListener<T> listener) {
		this.doRequestWithJackson(pContext, pUrl, Request.Method.GET, null,
				null,ThirdPartyType.STRING, listener);
	}
	
	public <T> void doRequestPOSTMethod(Context pContext,
			String pUrl, 
			final VolleyActionListener<T> listener) {
		this.doRequestWithJackson(pContext, pUrl, Request.Method.POST, null,
				null,ThirdPartyType.STRING, listener);
	}
	
	

	private <T> void doRequestWithJackson(Context pContext, String pUrl,
			int pMethodType, Map<String, String> params, final Class<T> pClass,
			ThirdPartyType pType, final VolleyActionListener<T> listener) {
		if (pContext == null) {
			return;
		}
		if (this.mRequestQueue == null) {
			this.mRequestQueue = this.newRequestQueue(pContext
					.getApplicationContext());
		}
		if (!this.isConnectNetWork(pContext)) {
			if (listener != null) {
				listener.onNoNetwork(pContext);
				return;
			}
		}
		if (listener != null) {
			listener.onBeforeRequest(pContext);
		}
		switch (pType) {
		case JACKSON:
			this.mRequestQueue.add(new JacksonRequest<T>(pMethodType, pUrl,
					params, this.mVolleyConfigBuilder.getTimeout(),
					new JacksonRequestListener<T>() {

						@Override
						public void onResponse(T response, int statusCode,
								VolleyError error) {
							if (listener != null) {
								listener.onResponse(response, statusCode, error);
							}
						}
						@Override
						public JavaType getReturnType() {
							return SimpleType.construct(pClass);
						}
					}));
			break;
		case STRING:
			this.mRequestQueue.add(new StringRequest(pMethodType, pUrl, params,
					this.mVolleyConfigBuilder.getTimeout(),listener));
			break;

		default:
			break;
		}

	}

	public enum ThirdPartyType {
		STRING, JACKSON
	}
}
