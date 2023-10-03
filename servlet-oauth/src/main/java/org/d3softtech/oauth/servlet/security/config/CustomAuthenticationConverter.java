package org.d3softtech.oauth.servlet.security.config;



import java.util.Collection;
import org.d3softtech.oauth.servlet.security.user.AuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class CustomAuthenticationConverter implements Converter<Jwt, AuthenticatedUser> {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomAuthenticationConverter() {
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    }

    @Override
    public AuthenticatedUser convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.grantedAuthoritiesConverter.convert(jwt);
        return new AuthenticatedUser(jwt, authorities);
    }

}
