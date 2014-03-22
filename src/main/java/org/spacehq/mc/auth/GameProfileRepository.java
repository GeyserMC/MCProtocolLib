package org.spacehq.mc.auth;

import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.auth.exception.ProfileNotFoundException;
import org.spacehq.mc.auth.response.ProfileSearchResultsResponse;
import org.spacehq.mc.util.URLUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameProfileRepository {

	private static final String BASE_URL = "https://api.mojang.com/";
	private static final String SEARCH_PAGE_URL = "https://api.mojang.com/profiles/page/";
	private static final int MAX_FAIL_COUNT = 3;
	private static final int DELAY_BETWEEN_PAGES = 100;
	private static final int DELAY_BETWEEN_FAILURES = 750;

	public void findProfilesByNames(String[] names, ProfileLookupCallback callback) {
		Set<ProfileCriteria> criteria = new HashSet<ProfileCriteria>();
		for(String name : names) {
			if(name != null && !name.isEmpty()) {
				criteria.add(new ProfileCriteria(name));
			}
		}

		Set<ProfileCriteria> request = new HashSet<ProfileCriteria>(criteria);
		int page = 1;
		Exception error = null;
		int failCount = 0;
		while(failCount < MAX_FAIL_COUNT && !criteria.isEmpty()) {
			try {
				ProfileSearchResultsResponse response = (ProfileSearchResultsResponse) URLUtils.makeRequest(URLUtils.constantURL("https://api.mojang.com/profiles/page/" + page), request, ProfileSearchResultsResponse.class);
				failCount = 0;
				error = null;
				if(response.getSize() != 0 && response.getProfiles().length != 0) {
					for(GameProfile profile : response.getProfiles()) {
						criteria.remove(new ProfileCriteria(profile.getName()));
						callback.onProfileLookupSucceeded(profile);
					}

					page++;
					try {
						Thread.sleep(DELAY_BETWEEN_PAGES);
					} catch(InterruptedException ignored) {
					}
				}
			} catch(AuthenticationException e) {
				error = e;
				failCount++;
				try {
					Thread.sleep(DELAY_BETWEEN_FAILURES);
				} catch(InterruptedException ignored) {
				}
			}
		}

		if(!criteria.isEmpty()) {
			if(error == null) {
				error = new ProfileNotFoundException("Server did not find the requested profile");
			}

			for(ProfileCriteria profileCriteria : criteria) {
				callback.onProfileLookupFailed(new GameProfile((UUID) null, profileCriteria.getName()), error);
			}
		}
	}

}
