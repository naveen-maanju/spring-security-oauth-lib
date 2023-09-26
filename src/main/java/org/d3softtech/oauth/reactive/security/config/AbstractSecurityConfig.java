package org.d3softtech.oauth.reactive.security.config;

import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableMethodSecurity
public abstract class AbstractSecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers(permitAllEndpoints()).permitAll()
                    .anyExchange().authenticated())
            .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(
                jwtSpec -> jwtSpec.jwtAuthenticationConverter(new CustomAuthenticationConverter())))
            .csrf(CsrfSpec::disable)
            .formLogin(FormLoginSpec::disable);
        return http.build();
    }

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
