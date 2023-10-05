package org.d3softtech.oauth.reactive.security.user;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthenticatedUser extends JwtAuthenticationToken {

    public static final String SOCIAL_SECURITY_NUMBER_CLAIM = "ssn";
    public static final String USER_NAME_CLAIM = "username";

    public static final String EMAIL_CLAIM="email";
    public static final String ROLES_CLAIM = "roles";
    public AuthenticatedUser(Jwt jwt,
        Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public String getSocialSecurityNumber() {
        return getToken().getClaimAsString(SOCIAL_SECURITY_NUMBER_CLAIM);
    }

    public String getUserName() {
        return getToken().getClaimAsString(USER_NAME_CLAIM);
    }

    public String getEmail() {
        return getToken().getClaimAsString(EMAIL_CLAIM);
    }

    public List<String> getRoles() {
        return getToken().getClaimAsStringList(ROLES_CLAIM);
    }

}
