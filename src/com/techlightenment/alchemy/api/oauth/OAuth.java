package com.techlightenment.alchemy.api.oauth;

import java.util.HashMap;
import java.util.Map;

import com.techlightenment.alchemy.api.oauth.signature.HmacSha1;
import com.techlightenment.alchemy.api.oauth.signature.OAuthBaseString;
import com.techlightenment.alchemy.api.oauth.signature.OAuthSignatureException;

public class OAuth {

	/** Version of OAuth */
	public static final String VERSION = "1.0";

	/** OAuth params */
	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	public static final String OAUTH_SIGNATURE = "oauth_signature";
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	public static final String OAUTH_NONCE = "oauth_nonce";
	public static final String OAUTH_VERSION = "oauth_version";
	public static final String OAUTH_ERROR = "oauth_error";

	/** OAuth signature methods (note we only support HMAC-SHA1) */
	public static final String OAUTH_SIG_HMAC_SHA1 = "HMAC-SHA1";

	/** OAuth consumer object */
	protected OAuthConsumer consumer;

	/** OAuth token object */
	protected OAuthToken token;

	/** Service url */
	protected String serviceUrl;

	/**
	 * Constructor with consumer key and secret
	 * 
	 * @param consumer
	 * @throws OAuthException
	 */
	protected OAuth(OAuthConsumer consumer) throws OAuthException {
		// Set consumer
		if (consumer != null) {
			this.consumer = consumer;
		} else {
			throw new OAuthException("A valid consumer is required");
		}
	}

	/**
	 * Constructor with consumer key/secret and token key/secret
	 * 
	 * @param consumer
	 * @param token
	 * @throws OAuthException
	 */
	public OAuth(OAuthConsumer consumer, OAuthToken token)
			throws OAuthException {
		// Call other constructor
		this(consumer);

		// Set token
		if (token != null) {
			this.token = token;
		}
	}

	/**
	 * Set the service url
	 * 
	 * @param url
	 */
	public void setServiceUrl(String url) {
		serviceUrl = url;
	}

	/**
	 * Set the token
	 * 
	 * @param token
	 */
	public void setToken(OAuthToken token) {
		// Set token
		if (token != null) {
			this.token = token;
		}
	}

	/**
	 * Send a request to the service
	 * 
	 * @param method
	 * @param params
	 * @throws OAuthException
	 * @throws OAuthSignatureException
	 * @throws OAuthNetworkException
	 */
	public String doRequest(String method, String url,
			Map<String, String> params) throws OAuthException,
			OAuthSignatureException, OAuthNetworkException {
		// Compile the OAuth params
		Map<String, String> oaParams = new HashMap<String, String>();
		oaParams.put(OAUTH_CONSUMER_KEY, consumer.getKey());
		if (token != null) {
			oaParams.put(OAUTH_TOKEN, token.getKey());
		}
		oaParams.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIG_HMAC_SHA1);
		oaParams.put(OAUTH_TIMESTAMP, Long
				.toString(System.currentTimeMillis() / 1000));
		oaParams.put(OAUTH_NONCE, OAuthUtils.generateKey());
		oaParams.put(OAUTH_VERSION, VERSION);

		// Get base string
		OAuthBaseString base = new OAuthBaseString(method, url);
		base.putAll(oaParams);
		if (params != null) {
			base.putAll(params);
		}

		// Get signature
		HmacSha1 hmac = new HmacSha1();
		oaParams.put(OAUTH_SIGNATURE, hmac.sign(base, consumer.getSecret(),
				token == null ? null : token.getSecret()));

		// Send request
		OAuthRequest req = new OAuthRequest(method, url);
		String result = req.send(oaParams, params);

		// Return response body string
		return result;
	}

}