package com.smile.volleythirdpartylibraryextension.base;

import android.content.Context;

public class ConfigBuilder {
	private final int MAX_CACHE_SIZE = 2 * 1024 * 1024;
	private final String USER_AGENT = "volley";
	private  final int DEFAULT_TIMEOUT = 30000;
	private String rootDirectory;
	private int maxCacheSizeInBytes;
	private String userAgent;
	private int mTimeout;
	public String getRootDirectory() {
		return rootDirectory;
	}
	public ConfigBuilder setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
		return this;
	}
	public int getMaxCacheSizeInBytes() {
		return maxCacheSizeInBytes;
	}
	public ConfigBuilder setMaxCacheSizeInBytes(int maxCacheSizeInBytes) {
		this.maxCacheSizeInBytes = maxCacheSizeInBytes;
		return this;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public ConfigBuilder setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	public int getTimeout() {
		return mTimeout;
	}
	public ConfigBuilder setTimeout(int mTimeout) {
		this.mTimeout = mTimeout;
		return this;
	}
	public ConfigBuilder(Context pContext) {
		rootDirectory = pContext.getCacheDir().getAbsolutePath();
		this.maxCacheSizeInBytes =  MAX_CACHE_SIZE;
		this.userAgent = USER_AGENT;
		mTimeout = DEFAULT_TIMEOUT;
	}
}
