package com.techlightenment.alchemy.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.techlightenment.alchemy.api.oauth.OAuth;
import com.techlightenment.alchemy.api.oauth.OAuthConsumer;
import com.techlightenment.alchemy.api.oauth.OAuthException;
import com.techlightenment.alchemy.api.oauth.OAuthNetworkException;
import com.techlightenment.alchemy.api.oauth.OAuthRequest;
import com.techlightenment.alchemy.api.oauth.OAuthToken;
import com.techlightenment.alchemy.api.oauth.OAuthUtils;
import com.techlightenment.alchemy.api.oauth.XAuth;

public class API {

	/** Alchemy service URLs */
	public static final String URL_ACCESS_TOKEN = "http://api-staging.alchemysocial.com/oauth/access_token";
	public static final String URL_WEB_SERVICE = "http://api-staging.alchemysocial.com/";

	/** Internal logging settings */
	public static final boolean LOG_GENERAL_ENABLED = true;
	public static final boolean LOG_ERROR_ENABLED = true;
	public static final boolean LOG_NONCE_ENABLED = true;

	/** Application context */
	protected Context context;

	/** OAuth/XAuth object for requests */
	protected XAuth xauth;

	/**
	 * Setup API with consumer credentials for XAuth
	 * 
	 * @param context
	 * @param consumerKey
	 * @param consumerSecret
	 * @throws APIException
	 */
	public API(Context context, String consumerKey, String consumerSecret)
			throws APIException {
		// Set context
		this.context = context;

		try {
			// Setup XAuth
			xauth = new XAuth(new OAuthConsumer(consumerKey, consumerSecret));
			xauth.setServiceUrl(URL_WEB_SERVICE);
			xauth.setAccessUrl(URL_ACCESS_TOKEN);
		} catch (OAuthException e) {
			// Catch any exceptions and throw as APIException
			throw new APIException(e.getMessage());
		}
	}

	/**
	 * Setup API with consumer credentials and token for OAuth
	 * 
	 * @param context
	 * @param consumerKey
	 * @param consumerSecret
	 * @param tokenKey
	 * @param tokenSecret
	 * @throws APIException
	 */
	public API(Context context, String consumerKey, String consumerSecret,
			String tokenKey, String tokenSecret) throws APIException {
		// Set context
		this.context = context;

		try {
			// Setup XAuth
			xauth = new XAuth(new OAuthConsumer(consumerKey, consumerSecret),
					new OAuthToken(tokenKey, tokenSecret));
			xauth.setServiceUrl(URL_WEB_SERVICE);
		} catch (OAuthException e) {
			// Catch any exceptions and throw as APIException
			throw new APIException(e.getMessage());
		}
	}

	/**
	 * Get access token via XAuth
	 * 
	 * @param username
	 * @param password
	 * @throws APIException
	 * @throws APINetworkException
	 */
	public OAuthToken getAccessToken(String username, String password)
			throws APIException, APINetworkException {
		// Check username/password
		if (!OAuthUtils.isString(username) || !OAuthUtils.isString(password)) {
			throw new APIException("Invalid username or password");
		}

		try {
			// Send request
			String result = xauth.getAccessToken(OAuthRequest.METHOD_POST,
					username, password);

			// Parse result as url encoded form/query string format
			Map<String, String> params = parseQueryString(result);

			// Some error checking
			if (params.get(OAuth.OAUTH_ERROR) != null) {
				throw new APIException(params.get(OAuth.OAUTH_ERROR));
			}
			if (params.get(OAuth.OAUTH_TOKEN) == null
					|| params.get(OAuth.OAUTH_TOKEN_SECRET) == null) {
				throw new APIException("An invalid response was received");
			}

			// Create the token
			OAuthToken token = new OAuthToken(params.get(OAuth.OAUTH_TOKEN),
					params.get(OAuth.OAUTH_TOKEN_SECRET));

			// Return the access token
			return token;
		} catch (OAuthNetworkException e) {
			// Catch network errors and throw as APINetworkException
			throw new APINetworkException(e.getMessage());
		} catch (Exception e) {
			// Catch any other exceptions and throw as APIException
			throw new APIException(e.getMessage());
		}
	}

	/**
	 * Parse a query string or url encoded form body
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected Map<String, String> parseQueryString(String str)
			throws UnsupportedEncodingException {
		// Split string by & to get key=value pairings
		String[] pairs = str.split("&");

		// Create a map of the params
		Map<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < pairs.length; i++) {
			String[] keyvals = pairs[i].split("=");
			params.put(keyvals[0], URLDecoder.decode(keyvals[1], "UTF-8"));
		}

		return params;
	}

}