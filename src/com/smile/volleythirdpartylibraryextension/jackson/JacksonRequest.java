/*
 * Copyright (C) 2013 SpotHero
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smile.volleythirdpartylibraryextension.jackson;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.smile.volleythirdpartylibraryextension.base.BaseRequest;

public class JacksonRequest<T> extends BaseRequest<T> {

	private Map<String, String> mParams;
	private List<Integer> mAcceptedStatusCodes;
	private final JacksonRequestListener<T> mListener;

	public JacksonRequest(int method, String url,int timeout, JacksonRequestListener<T> listener) {
		this(method, url, null,timeout, listener);
	}

	public JacksonRequest(int method, String url, Map<String, String> params, int timeout,JacksonRequestListener<T> listener) {
		super(method, url, null,timeout);
		mListener = listener;
	}

	@Override
	protected void deliverResponse(T response) {
		if(mListener != null){
			mListener.onResponse(response, HttpStatus.SC_OK, null);
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		int statusCode;
		if (error != null && error.networkResponse != null) {
			statusCode = error.networkResponse.statusCode;
		} else {
			statusCode = 0;
		}
		if(mListener != null){
			mListener.onResponse(null, statusCode, error);
		}
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		JavaType returnType = mListener.getReturnType();
		T returnData = null;
		if (returnType != null) {
			try {
//				if (response.data != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	            mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	            mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
					returnData = mapper.readValue(response.data, returnType);
//				} else if (response instanceof JacksonNetworkResponse) {
//					returnData = OBJECT_MAPPER.readValue(((JacksonNetworkResponse)response).inputStream, returnType);
//				}
			} catch (Exception e) {
				VolleyLog.e(e, "An error occurred while parsing network response:");
				return Response.error(new ParseError(response));
			}
		}
		return Response.success(returnData, HttpHeaderParser.parseCacheHeaders(response));
	}
}
