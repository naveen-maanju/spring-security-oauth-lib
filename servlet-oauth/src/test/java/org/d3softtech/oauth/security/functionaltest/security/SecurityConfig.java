package org.d3softtech.oauth.security.functionaltest.security;

import java.util.stream.Stream;
import org.d3softtech.oauth.servlet.security.config.AbstractSecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;


@TestConfiguration
public class SecurityConfig extends AbstractSecurityConfig {

    protected Stream<String> publicEndpoints() {
        return Stream.of("/dummy-public/**", "/open-api/**");
    }

    @MockBean
    private JwtDecoder jwtDecoder;
}
