package org.spacehq.mc.auth;

import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.auth.exception.InvalidCredentialsException;
import org.spacehq.mc.auth.exception.PropertyDeserializeException;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.properties.PropertyMap;
import org.spacehq.mc.auth.request.AuthenticationRequest;
import org.spacehq.mc.auth.request.RefreshRequest;
import org.spacehq.mc.auth.response.AuthenticationResponse;
import org.spacehq.mc.auth.response.RefreshResponse;
import org.spacehq.mc.auth.response.User;
import org.spacehq.mc.util.URLUtils;
import org.spacehq.mc.auth.serialize.UUIDSerializer;

import java.net.URL;
import java.util.*;

public class UserAuthentication {

	private static final String BASE_URL = "https://authserver.mojang.com/";
	private static final URL ROUTE_AUTHENTICATE = URLUtils.constantURL(BASE_URL + "authenticate");
	private static final URL ROUTE_REFRESH = URLUtils.constantURL(BASE_URL + "refresh");
	private static final String STORAGE_KEY_PROFILE_NAME = "displayName";
	private static final String STORAGE_KEY_PROFILE_ID = "uuid";
	private static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";
	private static final String STORAGE_KEY_USER_NAME = "username";
	private static final String STORAGE_KEY_USER_ID = "userid";
	private static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";
	private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";

	private String clientToken;
	private PropertyMap userProperties = new PropertyMap();
	private String userId;
	private String username;
	private String password;
	private String accessToken;
	private boolean isOnline;
	private List<GameProfile> profiles = new ArrayList<GameProfile>();
	private GameProfile selectedProfile;
	private UserType userType;

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

	public UserType getUserType() {
		return this.isLoggedIn() ? (this.userType == null ? UserType.LEGACY : this.userType) : null;
	}

	public PropertyMap getUserProperties() {
		return this.isLoggedIn() ? new PropertyMap(this.userProperties) : new PropertyMap();
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

	public void loadFromStorage(Map<String, Object> credentials) throws PropertyDeserializeException {
		this.logout();
		this.setUsername((String) credentials.get(STORAGE_KEY_USER_NAME));
		if(credentials.containsKey(STORAGE_KEY_USER_ID)) {
			this.userId = (String) credentials.get(STORAGE_KEY_USER_ID);
		} else {
			this.userId = this.username;
		}

		if(credentials.containsKey(STORAGE_KEY_USER_PROPERTIES)) {
			try {
				List<Map<String, String>> list = (List<Map<String, String>>) credentials.get(STORAGE_KEY_USER_PROPERTIES);
				for(Map<String, String> propertyMap : list) {
					String name = propertyMap.get("name");
					String value = propertyMap.get("value");
					String signature = propertyMap.get("signature");
					if(signature == null) {
						this.userProperties.put(name, new Property(name, value));
					} else {
						this.userProperties.put(name, new Property(name, value, signature));
					}
				}
			} catch(Throwable t) {
				throw new PropertyDeserializeException("Couldn't deserialize user properties", t);
			}
		}

		if(credentials.containsKey(STORAGE_KEY_PROFILE_NAME) && credentials.containsKey(STORAGE_KEY_PROFILE_ID)) {
			GameProfile profile = new GameProfile(UUIDSerializer.fromString((String) credentials.get(STORAGE_KEY_PROFILE_ID)), (String) credentials.get(STORAGE_KEY_PROFILE_NAME));
			if(credentials.containsKey(STORAGE_KEY_PROFILE_PROPERTIES)) {
				try {
					List<Map<String, String>> list = (List<Map<String, String>>) credentials.get(STORAGE_KEY_PROFILE_PROPERTIES);
					for(Map<String, String> propertyMap : list) {
						String name = propertyMap.get("name");
						String value = propertyMap.get("value");
						String signature = propertyMap.get("signature");
						if(signature == null) {
							profile.getProperties().put(name, new Property(name, value));
						} else {
							profile.getProperties().put(name, new Property(name, value, signature));
						}
					}
				} catch(Throwable t) {
					throw new PropertyDeserializeException("Couldn't deserialize profile properties", t);
				}
			}

			this.selectedProfile = profile;
		}

		this.accessToken = (String) credentials.get(STORAGE_KEY_ACCESS_TOKEN);
	}

	public Map<String, Object> saveForStorage() {
		Map<String, Object> result = new HashMap<String, Object>();
		if(this.username != null) {
			result.put(STORAGE_KEY_USER_NAME, this.username);
		}

		if(this.getUserID() != null) {
			result.put(STORAGE_KEY_USER_ID, this.userId);
		}

		if(!this.getUserProperties().isEmpty()) {
			List<Map<String, String>> properties = new ArrayList<Map<String, String>>();
			for(Property userProperty : this.getUserProperties().values()) {
				Map<String, String> property = new HashMap<String, String>();
				property.put("name", userProperty.getName());
				property.put("value", userProperty.getValue());
				property.put("signature", userProperty.getSignature());
				properties.add(property);
			}

			result.put(STORAGE_KEY_USER_PROPERTIES, properties);
		}

		GameProfile selectedProfile = this.getSelectedProfile();
		if(selectedProfile != null) {
			result.put(STORAGE_KEY_PROFILE_NAME, selectedProfile.getName());
			result.put(STORAGE_KEY_PROFILE_ID, selectedProfile.getId());
			List<Map<String, String>> properties = new ArrayList<Map<String, String>>();
			for(Property profileProperty : selectedProfile.getProperties().values()) {
				Map<String, String> property = new HashMap<String, String>();
				property.put("name", profileProperty.getName());
				property.put("value", profileProperty.getValue());
				property.put("signature", profileProperty.getSignature());
				properties.add(property);
			}

			if(!properties.isEmpty()) {
				result.put(STORAGE_KEY_PROFILE_PROPERTIES, properties);
			}
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
				throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
			} else {
				if(response.getSelectedProfile() != null) {
					this.userType = response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG;
				} else if(response.getAvailableProfiles() != null && response.getAvailableProfiles().length != 0) {
					this.userType = response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG;
				}

				if(response.getUser() != null && response.getUser().getId() != null) {
					this.userId = response.getUser().getId();
				} else {
					this.userId = this.username;
				}

				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.profiles = Arrays.asList(response.getAvailableProfiles());
				this.selectedProfile = response.getSelectedProfile();
				this.updateProperties(response.getUser());
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
				throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
			} else {
				if(response.getSelectedProfile() != null) {
					this.userType = response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG;
				} else if(response.getAvailableProfiles() != null && response.getAvailableProfiles().length != 0) {
					this.userType = response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG;
				}

				if(response.getUser() != null && response.getUser().getId() != null) {
					this.userId = response.getUser().getId();
				} else {
					this.userId = this.username;
				}

				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.profiles = Arrays.asList(response.getAvailableProfiles());
				this.selectedProfile = response.getSelectedProfile();
				this.updateProperties(response.getUser());
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
		this.userType = null;
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
				throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
			} else {
				this.isOnline = true;
				this.accessToken = response.getAccessToken();
				this.selectedProfile = response.getSelectedProfile();
			}
		} else {
			throw new IllegalArgumentException("Invalid profile '" + profile + "'");
		}
	}

	@Override
	public String toString() {
		return "UserAuthentication{profiles=" + this.profiles + ", selectedProfile=" + this.getSelectedProfile() + ", username=" + this.username + ", isLoggedIn=" + this.isLoggedIn() + ", canPlayOnline=" + this.canPlayOnline() + ", accessToken=" + this.accessToken + ", clientToken=" + this.getClientToken() + "}";
	}

	private void updateProperties(User user) {
		this.userProperties.clear();
		if(user != null && user.getProperties() != null) {
			this.userProperties.putAll(user.getProperties());
		}
	}

}
