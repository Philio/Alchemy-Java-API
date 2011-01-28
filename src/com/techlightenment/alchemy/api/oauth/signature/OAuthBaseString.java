package com.techlightenment.alchemy.api.oauth.signature;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import com.techlightenment.alchemy.api.oauth.OAuthUtils;

public class OAuthBaseString {

	/** Request method */
	protected String method;

	/** Request url */
	protected String url;

	/** Param list */
	protected Map<String, String> params = new TreeMap<String, String>();

	/** This is just to allow param-less usage */
	public OAuthBaseString() {
	}

	/**
	 * Constructor setting method and url
	 * 
	 * @param method
	 * @param url
	 * @throws OAuthSignatureException
	 */
	public OAuthBaseString(String method, String url)
			throws OAuthSignatureException {
		// Set method
		setMethod(method);

		// Set url
		setUrl(url);
	}

	/**
	 * Constructor setting method, url and params
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @throws OAuthSignatureException
	 */
	public OAuthBaseString(String method, String url, Map<String, String> params)
			throws OAuthSignatureException {
		// Call other constructor
		this(method, url);

		// Set params
		putAll(params);
	}

	/**
	 * Set the request method
	 * 
	 * @param method
	 * @throws OAuthSignatureException
	 */
	public void setMethod(String method) throws OAuthSignatureException {
		try {
			this.method = OAuthUtils.encodeString(method);
		} catch (UnsupportedEncodingException e) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException(e.getMessage());
		}
	}

	/**
	 * Set the request url
	 * 
	 * @param url
	 * @throws OAuthSignatureException
	 */
	public void setUrl(String url) throws OAuthSignatureException {
		try {
			this.url = OAuthUtils.encodeString(url);
		} catch (UnsupportedEncodingException e) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException(e.getMessage());
		}
	}

	/**
	 * Add a request param
	 * 
	 * @param key
	 * @param value
	 * @throws OAuthSignatureException
	 */
	public void putParam(String key, String value)
			throws OAuthSignatureException {
		if (!OAuthUtils.isString(key)) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException("Invalid param key");
		}
		params.put(key, value);
	}

	/**
	 * Add multiple request params
	 * 
	 * @param map
	 * @throws OAuthSignatureException
	 */
	public void putAll(Map<String, String> map) throws OAuthSignatureException {
		if (map.size() == 0) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException("An empty map was provided");
		}
		params.putAll(map);
	}

	/**
	 * Generate the base string
	 * 
	 * @return
	 * @throws OAuthSignatureException
	 */
	public String getBaseString() throws OAuthSignatureException {
		// Check data
		if (!OAuthUtils.isString(method) || !OAuthUtils.isString(url)
				|| params.size() == 0) {
			throw new OAuthSignatureException("Invalid method or url");
		}

		// Compile base string
		String base = method + "&" + url + "&";

		// Iterate over map
		String query = "";
		for (String key : params.keySet()) {
			// Add & if not 0 length
			if (query.length() != 0) {
				query += "&";
			}
			try {
				query += key + "=" + OAuthUtils.encodeString(params.get(key));
			} catch (UnsupportedEncodingException e) {
				// Throw OAuthSignatureException
				throw new OAuthSignatureException(e.getMessage());
			}
		}

		// Add query string to base
		try {
			base += OAuthUtils.encodeString(query);
		} catch (UnsupportedEncodingException e) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException(e.getMessage());
		}
		return base;
	}

}