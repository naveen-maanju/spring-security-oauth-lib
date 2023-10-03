package org.d3softtech.oauth.servlet.security.user;

import org.springframework.stereotype.Component;

@Component("Role")
public final class RoleContainer {

    public static final String ADMIN = "admin";
    public static final String USER = "user";
    public static final String QA = "qa";
    public static final String DEV = "dev";

}
