package org.geysermc.mcprotocollib.auth.service;

import org.geysermc.mcprotocollib.auth.data.GameProfile;
import org.geysermc.mcprotocollib.auth.exception.request.InvalidCredentialsException;
import org.geysermc.mcprotocollib.auth.exception.request.RequestException;
import org.geysermc.mcprotocollib.auth.util.HTTP;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Service used for authenticating users.
 */
public abstract class AuthenticationService extends Service {
    protected String accessToken;
    protected boolean loggedIn;
    protected String username;
    protected String password;
    protected GameProfile selectedProfile;
    protected List<GameProfile.Property> properties = new ArrayList<>();
    protected List<GameProfile> profiles = new ArrayList<>();

    public AuthenticationService(URI defaultURI) {
        super(defaultURI);
    }

    /**
     * Gets the access token of the service.
     *
     * @return The user's access token.
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Gets whether the service has been used to log in.
     *
     * @return Whether the service is logged in.
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    /**
     * Gets the username of the service.
     *
     * @return The service's username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the password of the service.
     *
     * @return The user's ID.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the username of the service.
     *
     * @param username Username to set.
     */
    public void setUsername(String username) {
        if(this.loggedIn && this.selectedProfile != null) {
            throw new IllegalStateException("Cannot change username while user is logged in and profile is selected.");
        } else {
            this.username = username;
        }
    }

    /**
     * Sets the password of the service.
     *
     * @param password Password to set.
     */
    public void setPassword(String password) {
        if(this.loggedIn && this.selectedProfile != null) {
            throw new IllegalStateException("Cannot change password while user is logged in and profile is selected.");
        } else {
            this.password = password;
        }
    }

    /**
     * Gets the properties of the user logged in with the service.
     *
     * @return The user's properties.
     */
    public List<GameProfile.Property> getProperties() {
        return Collections.unmodifiableList(this.properties);
    }

    /**
     * Gets the available profiles of the user logged in with the service.
     *
     * @return The user's available profiles.
     */
    public List<GameProfile> getAvailableProfiles() {
        return Collections.unmodifiableList(this.profiles);
    }

    /**
     * Gets the selected profile of the user logged in with the service.
     *
     * @return The user's selected profile.
     */
    public GameProfile getSelectedProfile() {
        return this.selectedProfile;
    }

    /**
     * Sets the access token of the service.
     *
     * @param accessToken Access token to set.
     */
    public void setAccessToken(String accessToken) {
        if(this.loggedIn && this.selectedProfile != null) {
            throw new IllegalStateException("Cannot change access token while user is logged in and profile is selected.");
        } else {
            this.accessToken = accessToken;
        }
    }

    /**
     * Logs the service in.
     * The current access token will be used if set. Otherwise, password-based authentication will be used.
     *
     * @throws RequestException If an error occurs while making the request.
     */
    public abstract void login() throws RequestException;

    /**
     * Logs the service out.
     *
     * @throws RequestException If an error occurs while making the request.
     */
    public void logout() throws RequestException {
        if(!this.loggedIn) {
            throw new IllegalStateException("Cannot log out while not logged in.");
        }

        this.accessToken = null;
        this.loggedIn = false;
        this.properties.clear();
        this.profiles.clear();
        this.selectedProfile = null;
    }
}
