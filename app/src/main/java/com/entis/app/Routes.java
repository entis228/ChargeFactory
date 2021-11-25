package com.entis.app;

import java.util.UUID;

public final class Routes {

    private Routes() {
    }

    public static final String API_ROOT = "/api";

    public static final String USERS = API_ROOT + "/users";

    public static final String STATIONS = API_ROOT + "/stations";

    public static final String CHARGES = API_ROOT + "/charges";

    public static final String TOKEN = API_ROOT + "/token";

    public static String user(UUID id) {
        return USERS + '/' + id;
    }

    public static String station(UUID id) {
        return STATIONS + '/' + id;
    }

    public static String charge(UUID id) {
        return CHARGES + '/' + id;
    }
}
