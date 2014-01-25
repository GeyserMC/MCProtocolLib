package ch.spacebase.mc.auth.response;

import java.util.List;

import ch.spacebase.mc.auth.ProfileProperty;
import ch.spacebase.mc.auth.response.Response;

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
