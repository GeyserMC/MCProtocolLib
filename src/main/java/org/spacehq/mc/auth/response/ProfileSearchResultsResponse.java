package org.spacehq.mc.auth.response;

import org.spacehq.mc.auth.GameProfile;

public class ProfileSearchResultsResponse extends Response {

	private GameProfile[] profiles;
	private int size;

	public GameProfile[] getProfiles() {
		return this.profiles;
	}

	public int getSize() {
		return this.size;
	}

}
