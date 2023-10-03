package org.d3softtech.oauth.reactive.security.config;

import java.util.Collection;
import java.util.stream.Stream;
import org.d3softtech.oauth.reactive.security.user.AuthenticatedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@ComponentScan(basePackages = "org.d3softtech.oauth.reactive.security")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public abstract class AbstractReactiveSecurityConfig {

    @Bean
    public SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        http
            .authorizeExchange(
                request -> request.pathMatchers(permitAllEndpoints()).permitAll().anyExchange().authenticated())
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(
                    jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwt -> {
                        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
                        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
                        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
                        return Mono.just(new AuthenticatedUser(jwt, authorities));
                    }).jwtDecoder(jwtDecoder())))
            .csrf(CsrfSpec::disable)
            .formLogin(FormLoginSpec::disable);
        return http.build();
    }

    protected abstract ReactiveJwtDecoder jwtDecoder();

    private String[] permitAllEndpoints() {
        return Stream.concat(documentationEndpoints(), publicEndpoints()).toArray(String[]::new);
    }

    /**
     * Override this method to provide application specific public endpoints
     *
     * @return
     */
    protected Stream<String> publicEndpoints() {
        return Stream.of();
    }

    private Stream<String> documentationEndpoints() {
        return Stream.of("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
