package com.techlightenment.alchemy.api.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class OAuthRequest {

	/** Request methods */
	public static final String METHOD_GET = "GET";
	public static final String METHOD_HEAD = "HEAD";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_DELETE = "DELETE";

	/** Request method */
	protected String method;

	/** Request url */
	protected String url;

	/** HTTP client object */
	protected HttpClient http;

	/**
	 * Constructor with method and url
	 * 
	 * @param method
	 * @param url
	 */
	public OAuthRequest(String method, String url) {
		// Set method
		this.method = method;

		// Set url
		this.url = url;

		// Instantiate HTTP client
		http = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(http.getParams(), 10000);
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000);
	}

	/**
	 * Send OAuth request
	 * 
	 * @param oaParams
	 * @param params
	 * @throws OAuthException
	 * @throws OAuthNetworkException
	 */
	public String send(Map<String, String> oaParams, Map<String, String> params)
			throws OAuthException, OAuthNetworkException {
		try {
			// Compile OAuth header
			String header = header(oaParams);

			// Send request
			HttpResponse resp = null;
			if (method == METHOD_GET) {
				resp = sendGet(url + "?" + queryString(params), header);
			}
			if (method == METHOD_HEAD) {
				resp = sendHead(url + "?" + queryString(params), header);
			}
			if (method == METHOD_POST) {
				resp = sendPost(url, header, queryString(params));
			}
			if (method == METHOD_PUT) {
				resp = sendPut(url, header, queryString(params));
			}
			if (method == METHOD_DELETE) {
				resp = sendDelete(url + "?" + queryString(params), header);
			}

			// Get response entity
			HttpEntity entity = resp.getEntity();

			// Convert to string
			String result;
			if (entity != null) {
				InputStream is = entity.getContent();
				result = convertStreamToString(is);
				is.close();
			} else {
				result = null;
			}

			// Return body of response
			return result;
		} catch (ClientProtocolException e) {
			// Throw communication errors as OAuthNetworkException
			throw new OAuthNetworkException(e.getMessage());
		} catch (IOException e) {
			// Throw communication errors as OAuthNetworkException
			throw new OAuthNetworkException(e.getMessage());
		} catch (Exception e) {
			// Throw any other exceptions as an OAuthException
			throw new OAuthException(e.getMessage());
		}
	}

	/**
	 * Generate the OAuth header
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String header(Map<String, String> params)
			throws UnsupportedEncodingException {
		String header = "";
		for (String key : params.keySet()) {
			// Add comma separator if not 0 length
			if (header.length() != 0) {
				header += ",";
			}
			header += key + "=\"" + OAuthUtils.encodeString(params.get(key))
					+ "\"";
		}
		return "OAuth " + header;
	}

	/**
	 * Create a query string from a param map
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String queryString(Map<String, String> params)
			throws UnsupportedEncodingException {
		String qs = "";
		for (String key : params.keySet()) {
			// Add & if not 0 length
			if (qs.length() != 0) {
				qs += "&";
			}
			qs += key + "=" + OAuthUtils.encodeString(params.get(key));
		}
		return qs;
	}

	/**
	 * Send GET request
	 * 
	 * @param url
	 * @param header
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected HttpResponse sendGet(String url, String header)
			throws ClientProtocolException, IOException {
		// Create request
		HttpGet req = new HttpGet(url);
		req.addHeader("Authorization", header);

		// Return response
		return http.execute(req);
	}

	/**
	 * Send HEAD request
	 * 
	 * @param url
	 * @param header
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected HttpResponse sendHead(String url, String header)
			throws ClientProtocolException, IOException {
		// Create request
		HttpHead req = new HttpHead(url);
		req.addHeader("Authorization", header);

		// Return response
		return http.execute(req);
	}

	/**
	 * Send POST request
	 * 
	 * @param url
	 * @param header
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected HttpResponse sendPost(String url, String header, String data)
			throws ClientProtocolException, IOException {
		// Create request
		HttpPost req = new HttpPost(url);
		req.setEntity(new StringEntity(data));
		req.addHeader("Authorization", header);
		req.addHeader("Content-Type", "application/x-www-form-urlencoded");

		// Return response
		return http.execute(req);
	}

	/**
	 * Send PUT request
	 * 
	 * @param url
	 * @param header
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected HttpResponse sendPut(String url, String header, String data)
			throws ClientProtocolException, IOException {
		// Create request
		HttpPut req = new HttpPut(url);
		req.setEntity(new StringEntity(data));
		req.addHeader("Authorization", header);
		req.addHeader("Content-Type", "application/x-www-form-urlencoded");

		// Return response
		return http.execute(req);
	}

	/**
	 * Send DELETE request
	 * 
	 * @param url
	 * @param header
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected HttpResponse sendDelete(String url, String header)
			throws ClientProtocolException, IOException {
		// Create request
		HttpDelete req = new HttpDelete(url);
		req.addHeader("Authorization", header);

		// Return response
		return http.execute(req);
	}

	/**
	 * Convert stream to string
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	protected String convertStreamToString(InputStream is) throws IOException {
		// Create buffered reader and string builder
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		// Read data line by line
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		// Return string
		return sb.toString();
	}

}