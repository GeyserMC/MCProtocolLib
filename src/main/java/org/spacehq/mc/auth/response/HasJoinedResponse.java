package org.spacehq.mc.auth.response;

import org.spacehq.mc.auth.ProfileProperty;

import java.util.List;

public class HasJoinedResponse extends Response {

	private String id;
	private List<ProfileProperty> properties;

	public String getId() {
		return this.id;
	}

	public List<ProfileProperty> getProperties() {
		return this.properties;
	}

}
