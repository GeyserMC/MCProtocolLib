package ch.spacebase.mc.auth;

import ch.spacebase.mc.auth.SessionService;
import ch.spacebase.mc.auth.UserAuthentication;
import ch.spacebase.mc.auth.exceptions.AuthenticationException;
import ch.spacebase.mc.auth.exceptions.AuthenticationUnavailableException;
import ch.spacebase.mc.auth.exceptions.InvalidCredentialsException;
import ch.spacebase.mc.auth.exceptions.UserMigratedException;
import ch.spacebase.mc.auth.response.Response;
import ch.spacebase.mc.util.IOUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthenticationService {

	private String clientToken;
	private Gson gson = new Gson();

	public AuthenticationService(String clientToken) {
		this.clientToken = clientToken;
	}

	public UserAuthentication createUserAuthentication() {
		return new UserAuthentication(this);
	}

	public SessionService createMinecraftSessionService() {
		return new SessionService(this);
	}

	public String getClientToken() {
		return this.clientToken;
	}
	
	public <T extends Response> T makeRequest(URL url, Object input, Class<T> clazz) throws AuthenticationException {
		try {
			String jsonString = input == null ? this.performGetRequest(url) : this.performPostRequest(url, this.gson.toJson(input), "application/json");
			T result = this.gson.fromJson(jsonString, clazz);
			if(result == null) {
				return null;
			} else if(result.getError() != null && !result.getError().equals("")) {
				if(result.getCause() != null && result.getCause().equals("UserMigratedException")) {
					throw new UserMigratedException(result.getErrorMessage());
				} else if(result.getError().equals("ForbiddenOperationException")) {
					throw new InvalidCredentialsException(result.getErrorMessage());
				} else {
					throw new AuthenticationException(result.getErrorMessage());
				}
			} else {
				return result;
			}
		} catch(Exception e) {
			throw new AuthenticationUnavailableException("Could not make request to auth server.", e);
		}
	}
	
	private HttpURLConnection createUrlConnection(URL url) throws IOException {
		if(url == null) {
			throw new IllegalArgumentException("URL cannot be null.");
		}
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setUseCaches(false);
		return connection;
	}

	private String performPostRequest(URL url, String post, String type) throws IOException {
		if(url == null) {
			throw new IllegalArgumentException("URL cannot be null.");
		}
		
		if(post == null) {
			throw new IllegalArgumentException("Post cannot be null.");
		}
		
		if(type == null) {
			throw new IllegalArgumentException("Type cannot be null.");
		}
		
		HttpURLConnection connection = this.createUrlConnection(url);
		byte[] bytes = post.getBytes("UTF-8");
		connection.setRequestProperty("Content-Type", type + "; charset=utf-8");
		connection.setRequestProperty("Content-Length", "" + bytes.length);
		connection.setDoOutput(true);
		OutputStream outputStream = null;
		try {
			outputStream = connection.getOutputStream();
			outputStream.write(bytes);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

		InputStream inputStream = null;
		try {
			inputStream = connection.getInputStream();
			return IOUtils.toString(inputStream, "UTF-8");
		} catch(IOException e) {
			IOUtils.closeQuietly(inputStream);
			inputStream = connection.getErrorStream();
			if(inputStream == null) {
				throw e;
			}

			return IOUtils.toString(inputStream,"UTF-8");
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private String performGetRequest(URL url) throws IOException {
		if(url == null) {
			throw new IllegalArgumentException("URL cannot be null.");
		}
		
		HttpURLConnection connection = this.createUrlConnection(url);
		InputStream inputStream = null;
		try {
			inputStream = connection.getInputStream();
			return IOUtils.toString(inputStream, "UTF-8");
		} catch(IOException e) {
			IOUtils.closeQuietly(inputStream);
			inputStream = connection.getErrorStream();
			if(inputStream == null) {
				throw e;
			}

			return IOUtils.toString(inputStream, "UTF-8");
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
}
