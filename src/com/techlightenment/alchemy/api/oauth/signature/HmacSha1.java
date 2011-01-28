package com.techlightenment.alchemy.api.oauth.signature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.techlightenment.alchemy.api.oauth.OAuthUtils;

public class HmacSha1 implements OAuthSignature {

	/** Settings for hash generation */
	public static final String ALGORITHM = "HmacSHA1";
	public static final String CHARSET = "UTF-8";

	/**
	 * Sign a request
	 * 
	 * @param baseStr
	 * @param key
	 * @return
	 * @throws OAuthSignatureException
	 */
	public String sign(OAuthBaseString baseString, String conSecret,
			String tokSecret) throws OAuthSignatureException {
		// Check that baseStr and conSecret are valid
		if (!OAuthUtils.isString(conSecret)) {
			throw new OAuthSignatureException("Invalid consumer secret");
		}

		// Generate key
		String key = conSecret + '&';
		if (OAuthUtils.isString(tokSecret)) {
			key += tokSecret;
		}

		// Generate signature
		try {
			// Key spec
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(CHARSET),
					ALGORITHM);

			// Mac
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(spec);
			byte[] bytes = mac.doFinal(baseString.getBaseString().getBytes(
					CHARSET));

			// Return signature
			return Base64.encodeToString(bytes, Base64.NO_WRAP);
		} catch (Exception e) {
			// Throw OAuthSignatureException
			throw new OAuthSignatureException(e.getMessage());
		}
	}

}
