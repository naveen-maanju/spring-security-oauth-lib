package org.d3softtech.oauth.reactive.security;

import static org.d3softtech.oauth.reactive.security.user.RoleContainer.ADMIN;
import static org.d3softtech.oauth.reactive.security.user.RoleContainer.USER;

import java.time.Duration;
import java.util.Set;
import org.d3softtech.oauth.reactive.security.functionaltest.security.Token;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@ActiveProfiles("test")
public abstract class BaseTest {

    public static final String TEST_USER_NAME = "test-user";
    public static final String TEST_USER_SSN = "197611119877";
    public static final String TEST_USER_EMAIL = "test-user@d3softtech.com";
    public static final String DUMMY_AUD = "spring-test";
    public static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_admin");
    public static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_user");
    public static final GrantedAuthority ROLE_QA = new SimpleGrantedAuthority("ROLE_qa");
    public static final GrantedAuthority ROLE_DEV = new SimpleGrantedAuthority("ROLE_dev");
    protected static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJlbWFpbCI6InRlc3QtdXNlckB0aHVuZGVya2ljay5jb20iLCJzc24iOiIxOTc2MTExMTk4NzciLCJ1c2VyX25hbWUiOiJ0ZXN0LXVzZXIifQ.gScIo9KndFEHWtNnEQEgtXKky5jwdoQisx4R4dUoBLA";

    protected Token getToken() {
        return getToken(ADMIN, USER);
    }

    protected Token getToken(String... roles) {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:6060").build();
        Token token = webClient.post()
            .uri(uriBuilder -> uriBuilder.path("/oauth2/token").queryParam("grant_type", "client_credentials")
                .queryParam("email", TEST_USER_EMAIL).queryParam("ssn", TEST_USER_SSN)
                .queryParam("username", TEST_USER_NAME).queryParam("roles", Set.of(roles)).build())
            .headers(httpHeaders -> httpHeaders.setBasicAuth("spring-test", "test-secret")).retrieve()
            .bodyToMono(Token.class).block(
                Duration.ofSeconds(2));
        System.out.println("----------" + token);
        return token;
    }
}
