package org.d3softtech.oauth.reactive.security.user;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthenticatedUser extends JwtAuthenticationToken {

    public static final String PREFERRED_USERNAME_CLAIM = "preferred_username";
    public static final String DISPLAY_NAME_CLAIM = "name";
    public static final String ROLES_CLAIM = "roles";
    private static final long serialVersionUID = -6129314830100514971L;

    public AuthenticatedUser(Jwt jwt,
        Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public String getPreferredUsername() {
        return getToken().getClaimAsString(PREFERRED_USERNAME_CLAIM);
    }

    public String getDisplayName() {
        return getToken().getClaimAsString(DISPLAY_NAME_CLAIM);
    }

    public List<String> getRoles() {
        return getToken().getClaimAsStringList(ROLES_CLAIM);
    }

}
