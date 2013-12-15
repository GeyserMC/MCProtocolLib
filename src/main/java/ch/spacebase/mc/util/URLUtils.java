package ch.spacebase.mc.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class URLUtils {

	public static URL constantURL(String url) {
		try {
			return new URL(url);
		} catch(MalformedURLException e) {
			throw new Error("Malformed constant url: " + url);
		}
	}
	
	public static URL concatenateURL(URL url, String query) {
		try {
			return url.getQuery() != null && url.getQuery().length() > 0 ? new URL(url.getProtocol(), url.getHost(), url.getFile() + "&" + query) : new URL(url.getProtocol(), url.getHost(), url.getFile() + "?" + query);
		} catch(MalformedURLException e) {
			throw new IllegalArgumentException("Concatenated URL was malformed: " + url.toString() + ", " + query);
		}
	}

	public static String buildQuery(Map<String, Object> query) {
		if(query == null) {
			return "";
		} else {
			StringBuilder builder = new StringBuilder();
			Iterator<Entry<String, Object>> it = query.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if(builder.length() > 0) {
					builder.append("&");
				}

				try {
					builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				} catch(UnsupportedEncodingException e) {
				}

				if(entry.getValue() != null) {
					builder.append("=");
					try {
						builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
					} catch(UnsupportedEncodingException e) {
					}
				}
			}

			return builder.toString();
		}
	}

}
