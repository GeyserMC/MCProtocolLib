package org.spacehq.mc.auth.properties;

import java.util.HashMap;
import java.util.Map;

public class PropertyMap extends HashMap<String, Property> {

	public PropertyMap() {
		super();
	}

	public PropertyMap(Map<String, Property> copy) {
		super(copy);
	}

}
