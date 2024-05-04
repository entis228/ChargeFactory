package com.entis.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Routes {

    public static final String API_ROOT = "/api";

    public static final String USERS = API_ROOT + "/users";

    public static final String STATIONS = API_ROOT + "/stations";

    public static final String TOKEN = API_ROOT + "/token";
}
