package com.techlightenment.alchemy.api.oauth;

import java.util.HashMap;
import java.util.Map;

import com.techlightenment.alchemy.api.oauth.signature.OAuthSignatureException;

public class XAuth extends OAuth {

	/** XAuth params */
	public static final String XAUTH_MODE = "x_auth_mode";
	public static final String XAUTH_USERNAME = "x_auth_username";
	public static final String XAUTH_PASSWORD = "x_auth_password";

	/** XAuth modes */
	public static final String XAUTH_MODE_CLIENT_AUTH = "client_auth";

	/** Access url */
	protected String accessUrl;

	/**
	 * Constructor with consumer key and secret
	 * 
	 * @param consumer
	 * @throws OAuthException
	 */
	public XAuth(OAuthConsumer consumer) throws OAuthException {
		// Call superclass
		super(consumer);
	}

	/**
	 * Constructor with consumer key/secret and token key/secret
	 * 
	 * @param consumer
	 * @param token
	 * @throws OAuthException
	 */
	public XAuth(OAuthConsumer consumer, OAuthToken token)
			throws OAuthException {
		// Call superclass
		super(consumer, token);
	}

	/**
	 * Set acccess url
	 * 
	 * @param url
	 */
	public void setAccessUrl(String url) {
		accessUrl = url;
	}

	/**
	 * Get access token
	 * 
	 * @param method
	 * @param username
	 * @param password
	 * @throws OAuthException
	 * @throws OAuthSignatureException
	 * @throws OAuthNetworkException
	 */
	public String getAccessToken(String method, String username, String password)
			throws OAuthException, OAuthSignatureException,
			OAuthNetworkException {
		// Compile XAuth params
		Map<String, String> xaParams = new HashMap<String, String>();
		xaParams.put(XAUTH_MODE, XAUTH_MODE_CLIENT_AUTH);
		xaParams.put(XAUTH_USERNAME, username);
		xaParams.put(XAUTH_PASSWORD, password);

		// Do the request and return response body
		return doRequest(method, accessUrl, xaParams);
	}

}