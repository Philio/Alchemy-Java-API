package com.techlightenment.alchemy.api.oauth;

public class OAuthToken {

	/** Token key */
	private String key;

	/** Token secret */
	private String secret;

	/**
	 * Constructor with key/secret
	 * 
	 * @param key
	 * @param secret
	 */
	public OAuthToken(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}

	/**
	 * Get the token key
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get the token secret
	 * 
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

}
