package org.d3softtech.oauth.reactive.security.user;

import org.springframework.stereotype.Component;

@Component("R")
public final class RoleContainer {

    public static final String ADMIN = "admin";
    public static final String USER = "user";
    public static final String QA = "env_QA";
    public static final String PROD = "env_PROD";

}
