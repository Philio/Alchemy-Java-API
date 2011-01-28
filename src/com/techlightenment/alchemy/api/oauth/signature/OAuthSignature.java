package com.techlightenment.alchemy.api.oauth.signature;

public interface OAuthSignature {

	/**
	 * Sign a request
	 * 
	 * @param baseStr
	 * @param key
	 * @return
	 * @throws OAuthSignatureException
	 */
	public String sign(OAuthBaseString baseString, String conSecret,
			String tokSecret) throws OAuthSignatureException;

}
