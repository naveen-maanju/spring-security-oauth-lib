package org.d3softtech.oauth.reactive.security.config;


import java.util.Collection;
import org.d3softtech.oauth.reactive.security.user.AuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationConverter implements Converter<Jwt, Mono<AuthenticatedUser>> {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomReactiveAuthenticationConverter() {
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    }

    @Override
    public Mono<AuthenticatedUser> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.grantedAuthoritiesConverter.convert(jwt);
        return Mono.just(new AuthenticatedUser(jwt, authorities));
    }

}
