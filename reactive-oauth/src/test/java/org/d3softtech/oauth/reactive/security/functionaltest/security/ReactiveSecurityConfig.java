package org.d3softtech.oauth.reactive.security.functionaltest.security;


import java.time.Duration;
import java.util.stream.Stream;
import org.d3softtech.oauth.reactive.security.config.AbstractReactiveSecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;


@Configuration
public class ReactiveSecurityConfig extends AbstractReactiveSecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    protected Stream<String> publicEndpoints() {
        return Stream.of("/dummy-public/**", "/open-api/**");
    }


    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder)
            ReactiveJwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
            new JwtTimestampValidator(Duration.ofMinutes(7200)));

        jwtDecoder.setJwtValidator(withClockSkew);

        return jwtDecoder;
    }

}
