package ch.spacebase.mc.auth.response;

import java.util.List;

public class User {

	private String id;
	private List<Property> properties;

	public String getId() {
		return this.id;
	}

	public List<Property> getProperties() {
		return this.properties;
	}

	public class Property {
		private String name;
		private String value;

		public String getKey() {
			return this.name;
		}

		public String getValue() {
			return this.value;
		}
	}

}
