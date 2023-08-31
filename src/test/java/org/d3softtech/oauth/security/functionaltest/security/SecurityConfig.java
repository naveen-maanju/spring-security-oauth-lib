package org.d3softtech.oauth.security.functionaltest.security;

import java.util.stream.Stream;
import org.d3softtech.oauth.security.config.AbstractSecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;


@TestConfiguration
public class SecurityConfig extends AbstractSecurityConfig {

    protected Stream<String> publicEndpoints() {
        return Stream.of("/dummy-public/**", "/open-api/**");
    }

}
