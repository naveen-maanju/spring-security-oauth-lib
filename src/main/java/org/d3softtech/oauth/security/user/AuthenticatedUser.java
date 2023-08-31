package org.d3softtech.oauth.security.user;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthenticatedUser extends JwtAuthenticationToken {

    public static final String SOCIAL_SECURITY_NUMBER_CLAIM = "ssn";
    public static final String USER_NAME_CLAIM = "user_name";

    public static final String EMAIL_CLAIM="email";
    public static final String ROLES_CLAIM = "roles";
    private static final long serialVersionUID = -6129314830100514971L;

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

    public List<String> getRoles() {
        return getToken().getClaimAsStringList(ROLES_CLAIM);
    }

}
