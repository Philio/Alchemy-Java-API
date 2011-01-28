package com.techlightenment.alchemy.api.oauth;

public class OAuthConsumer {

	/** Consumer key */
	private String key;

	/** Consumer secret */
	private String secret;

	/**
	 * Constructor with key/secret
	 * 
	 * @param key
	 * @param secret
	 */
	public OAuthConsumer(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}

	/**
	 * Get the consumer key
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get the consumer secret
	 * 
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

}