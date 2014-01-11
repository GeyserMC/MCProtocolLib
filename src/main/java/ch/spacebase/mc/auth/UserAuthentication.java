package ch.spacebase.mc.auth;

import ch.spacebase.mc.auth.exceptions.AuthenticationException;
import ch.spacebase.mc.auth.exceptions.InvalidCredentialsException;
import ch.spacebase.mc.auth.request.AuthenticationRequest;
import ch.spacebase.mc.auth.request.RefreshRequest;
import ch.spacebase.mc.auth.response.AuthenticationResponse;
import ch.spacebase.mc.auth.response.RefreshResponse;
import ch.spacebase.mc.auth.response.User;
import ch.spacebase.mc.util.URLUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserAuthentication {

	private static final String BASE_URL = "https://authserver.mojang.com/";
	private static final URL ROUTE_AUTHENTICATE = URLUtils.constantURL(BASE_URL + "authenticate");
	private static final URL ROUTE_REFRESH = URLUtils.constantURL(BASE_URL + "refresh");
	private static final String STORAGE_KEY_PROFILE_NAME = "displayName";
	private static final String STORAGE_KEY_PROFILE_ID = "uuid";
	private static final String STORAGE_KEY_USER_NAME = "username";
	private static final String STORAGE_KEY_USER_ID = "userid";
	private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
	
	private String clientToken;
	private Map<String, String> userProperties = new HashMap<String, String>();
	private String userId;
	private String username;
	private String password;
	private String accessToken;
	private boolean isOnline;
	private List<GameProfile> profiles = new ArrayList<GameProfile>();
	private GameProfile selectedProfile;

	public UserAuthentication(String clientToken) {
		if(clientToken == null) {
			throw new IllegalArgumentException("ClientToken cannot be null.");
		}
		
		this.clientToken = clientToken;
	}
	
	public String getClientToken() {
		return this.clientToken;
	}
	
	public String getUserID() {
		return this.userId;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public List<GameProfile> getAvailableProfiles() {
		return this.profiles;
	}
	
	public GameProfile getSelectedProfile() {
		return this.selectedProfile;
	}
	
	public Map<String, String> getUserProperties() {
		return this.isLoggedIn() ? Collections.unmodifiableMap(this.userProperties) : Collections.unmodifiableMap(new HashMap<String, String>());
	}
	
	public boolean isLoggedIn() {
		return this.accessToken != null && !this.accessToken.equals("");
	}

	public boolean canPlayOnline() {
		return this.isLoggedIn() && this.getSelectedProfile() != null && this.isOnline;
	}
	
	public boolean canLogIn() {
		return !this.canPlayOnline() && this.username != null && !this.username.equals("") && ((this.password != null && !this.password.equals("")) || (this.accessToken != null && !this.accessToken.equals("")));
	}
	
	public void setUsername(String username) {
		if(this.isLoggedIn() && this.canPlayOnline()) {
			throw new IllegalStateException("Cannot change username whilst logged in & online");
		} else {
			this.username = username;
		}
	}

	public void setPassword(String password) {
		if(this.isLoggedIn() && this.canPlayOnline() && this.password != null && !this.password.equals("")) {
			throw new IllegalStateException("Cannot set password whilst logged in & online");
		} else {
			this.password = password;
		}
	}
	
	public void setAccessToken(String accessToken) {
		if(this.isLoggedIn() && this.canPlayOnline()) {
			throw new IllegalStateException("Cannot change accessToken whilst logged in & online");
		} else {
			this.accessToken = accessToken;
		}
	}
	
	public void loadFromStorage(Map<String, String> credentials) {
		this.logout();
		this.setUsername(credentials.get(STORAGE_KEY_USER_NAME));
		if(credentials.containsKey(STORAGE_KEY_USER_ID)) {
			this.userId = credentials.get(STORAGE_KEY_USER_ID);
		} else {
			this.userId = this.username;
		}

		if(credentials.containsKey(STORAGE_KEY_PROFILE_NAME) && credentials.containsKey(STORAGE_KEY_PROFILE_ID)) {
			this.selectedProfile = new GameProfile(credentials.get(STORAGE_KEY_PROFILE_ID), credentials.get(STORAGE_KEY_PROFILE_NAME));
		}
		
		this.accessToken = credentials.get(STORAGE_KEY_ACCESS_TOKEN);
	}

	public Map<String, String> saveForStorage() {
		Map<String, String> result = new HashMap<String, String>();
		if(this.username != null) {
			result.put(STORAGE_KEY_USER_NAME, this.username);
		}

		if(this.getUserID() != null) {
			result.put(STORAGE_KEY_USER_ID, this.userId);
		}

		if(this.selectedProfile != null) {
			result.put(STORAGE_KEY_PROFILE_NAME, this.selectedProfile.getName());
			result.put(STORAGE_KEY_PROFILE_ID, this.selectedProfile.getId());
		}
		
		if(this.accessToken != null && !this.accessToken.equals("")) {
			result.put(STORAGE_KEY_ACCESS_TOKEN, this.accessToken);
		}

		return result;
	}

	public void login() throws AuthenticationException {
		if(this.username == null || this.username.equals("")) {
			throw new InvalidCredentialsException("Invalid username");
		} else {
			if(this.accessToken != null && !this.accessToken.equals("")) {
				this.loginWithToken();
			} else {
				if(this.password == null || this.password.equals("")) {
					throw new InvalidCredentialsException("Invalid password");
				}

				this.loginWithPassword();
			}
		}
	}

	private void loginWithPassword() throws AuthenticationException {
		if(this.username == null || this.username.equals("")) {
			throw new InvalidCredentialsException("Invalid username");
		} else if(this.password == null || this.password.equals("")) {
			throw new InvalidCredentialsException("Invalid password");
		} else {
			AuthenticationRequest request = new AuthenticationRequest(this, this.username, this.password);
			AuthenticationResponse response = URLUtils.makeRequest(ROUTE_AUTHENTICATE, request, AuthenticationResponse.class);
			if(!response.getClientToken().equals(this.getClientToken())) {
				throw new AuthenticationException("Server requested we change our client token. Don\'t know how to handle this!");
			} else {
				if(response.getUser() != null && response.getUser().getId() != null) {
					this.userId = response.getUser().getId();
				} else {
					this.userId = this.username;
				}

				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.profiles = Arrays.asList(response.getAvailableProfiles());
				this.selectedProfile = response.getSelectedProfile();
				this.userProperties.clear();
				if(response.getUser() != null && response.getUser().getProperties() != null) {
					Iterator<User.Property> it = response.getUser().getProperties().iterator();
					while(it.hasNext()) {
						User.Property property = it.next();
						this.userProperties.put(property.getKey(), property.getValue());
					}
				}
			}
		}
	}

	private void loginWithToken() throws AuthenticationException {
		if(this.userId == null || this.userId.equals("")) {
			if(this.username == null || this.username.equals("")) {
				throw new InvalidCredentialsException("Invalid uuid & username");
			}

			this.userId = this.username;
		}

		if(this.accessToken == null || this.accessToken.equals("")) {
			throw new InvalidCredentialsException("Invalid access token");
		} else {
			RefreshRequest request = new RefreshRequest(this);
			RefreshResponse response = URLUtils.makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
			if(!response.getClientToken().equals(this.getClientToken())) {
				throw new AuthenticationException("Server requested we change our client token. Don\'t know how to handle this!");
			} else {
				if(response.getUser() != null && response.getUser().getId() != null) {
					this.userId = response.getUser().getId();
				} else {
					this.userId = this.username;
				}

				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.profiles = Arrays.asList(response.getAvailableProfiles());
				this.selectedProfile = response.getSelectedProfile();
				this.userProperties.clear();
				if(response.getUser() != null && response.getUser().getProperties() != null) {
					Iterator<User.Property> it = response.getUser().getProperties().iterator();
					while(it.hasNext()) {
						User.Property property = it.next();
						this.userProperties.put(property.getKey(), property.getValue());
					}
				}

			}
		}
	}

	public void logout() {
		this.password = null;
		this.userId = null;
		this.selectedProfile = null;
		this.userProperties.clear();
		this.accessToken = null;
		this.profiles = null;
		this.isOnline = false;
	}

	public void selectGameProfile(GameProfile profile) throws AuthenticationException {
		if(!this.isLoggedIn()) {
			throw new AuthenticationException("Cannot change game profile whilst not logged in");
		} else if(this.getSelectedProfile() != null) {
			throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
		} else if(profile != null && this.profiles.contains(profile)) {
			RefreshRequest request = new RefreshRequest(this, profile);
			RefreshResponse response = URLUtils.makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
			if(!response.getClientToken().equals(this.getClientToken())) {
				throw new AuthenticationException("Server requested we change our client token. Don\'t know how to handle this!");
			} else {
				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.selectedProfile = response.getSelectedProfile();
			}
		} else {
			throw new IllegalArgumentException("Invalid profile \'" + profile + "\'");
		}
	}

	@Override
	public String toString() {
		return "UserAuthentication{profiles=" + this.profiles + ", selectedProfile=" + this.getSelectedProfile() + ", username=" + this.username + ", isLoggedIn=" + this.isLoggedIn() + ", canPlayOnline=" + this.canPlayOnline() + ", accessToken=" + this.accessToken + ", clientToken=" + this.getClientToken() + "}";
	}

}
