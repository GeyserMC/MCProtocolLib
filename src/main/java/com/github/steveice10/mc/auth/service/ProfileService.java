package com.github.steveice10.mc.auth.service;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.profile.ProfileNotFoundException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.util.HTTP;

import java.net.URI;
import java.util.*;

/**
 * Repository for looking up profiles by name.
 */
public class ProfileService extends Service {
    private static final URI DEFAULT_BASE_URI = URI.create("https://api.mojang.com/profiles/");
    private static final String SEARCH_ENDPOINT = "minecraft";

    private static final int MAX_FAIL_COUNT = 3;
    private static final int DELAY_BETWEEN_PAGES = 100;
    private static final int DELAY_BETWEEN_FAILURES = 750;
    private static final int PROFILES_PER_REQUEST = 100;

    /**
     * Creates a new ProfileService instance.
     */
    public ProfileService() {
        super(DEFAULT_BASE_URI);
    }

    private static Set<Set<String>> partition(Set<String> set, int size) {
        List<String> list = new ArrayList<String>(set);
        Set<Set<String>> ret = new HashSet<Set<String>>();
        for (int i = 0; i < list.size(); i += size) {
            Set<String> s = new HashSet<>(list.subList(i, Math.min(i + size, list.size())));
            ret.add(s);
        }

        return ret;
    }

    /**
     * Locates profiles by their names.
     *
     * @param names    Names to look for.
     * @param callback Callback to pass results to.
     */
    public void findProfilesByName(String[] names, ProfileLookupCallback callback) {
        this.findProfilesByName(names, callback, false);
    }

    /**
     * Locates profiles by their names.
     *
     * @param names    Names to look for.
     * @param callback Callback to pass results to.
     * @param async    Whether to perform requests asynchronously.
     */
    public void findProfilesByName(final String[] names, final ProfileLookupCallback callback, final boolean async) {
        final Set<String> criteria = new HashSet<String>();
        for (String name : names) {
            if (name != null && !name.isEmpty()) {
                criteria.add(name.toLowerCase());
            }
        }

        Runnable runnable = () -> {
            for (Set<String> request : partition(criteria, PROFILES_PER_REQUEST)) {
                Exception error = null;
                int failCount = 0;
                boolean tryAgain = true;
                while (failCount < MAX_FAIL_COUNT && tryAgain) {
                    tryAgain = false;
                    try {
                        GameProfile[] profiles = HTTP.makeRequest(getProxy(), getEndpointUri(SEARCH_ENDPOINT), request, GameProfile[].class);
                        failCount = 0;
                        Set<String> missing = new HashSet<String>(request);
                        for (GameProfile profile : profiles) {
                            missing.remove(profile.getName().toLowerCase());
                            callback.onProfileLookupSucceeded(profile);
                        }

                        for (String name : missing) {
                            callback.onProfileLookupFailed(new GameProfile((UUID) null, name), new ProfileNotFoundException("Server could not find the requested profile."));
                        }

                        try {
                            Thread.sleep(DELAY_BETWEEN_PAGES);
                        } catch (InterruptedException ignored) {
                        }
                    } catch (RequestException e) {
                        error = e;
                        failCount++;
                        if (failCount >= MAX_FAIL_COUNT) {
                            for (String name : request) {
                                callback.onProfileLookupFailed(new GameProfile((UUID) null, name), error);
                            }
                        } else {
                            try {
                                Thread.sleep(DELAY_BETWEEN_FAILURES);
                            } catch (InterruptedException ignored) {
                            }

                            tryAgain = true;
                        }
                    }
                }
            }
        };

        if (async) {
            new Thread(runnable, "ProfileLookupThread").start();
        } else {
            runnable.run();
        }
    }

    /**
     * Callback for reporting profile lookup results.
     */
    public interface ProfileLookupCallback {
        /**
         * Called when a profile lookup request succeeds.
         *
         * @param profile Profile resulting from the request.
         */
        void onProfileLookupSucceeded(GameProfile profile);

        /**
         * Called when a profile lookup request fails.
         *
         * @param profile Profile that failed to be located.
         * @param e       Exception causing the failure.
         */
        void onProfileLookupFailed(GameProfile profile, Exception e);
    }
}
