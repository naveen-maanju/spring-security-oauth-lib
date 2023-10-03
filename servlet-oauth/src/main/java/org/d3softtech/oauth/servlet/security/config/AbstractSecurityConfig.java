package org.d3softtech.oauth.servlet.security.config;

import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@ComponentScan("org.d3softtech.oauth")
@EnableWebSecurity
@EnableMethodSecurity
public abstract class AbstractSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                request -> request.requestMatchers(permitAllEndpoints()).permitAll().anyRequest().authenticated())
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomAuthenticationConverter())))
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);
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
