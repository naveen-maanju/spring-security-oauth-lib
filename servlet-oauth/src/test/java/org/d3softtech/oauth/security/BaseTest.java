package org.d3softtech.oauth.security;

import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.EMAIL_CLAIM;
import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.ROLES_CLAIM;
import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.SOCIAL_SECURITY_NUMBER_CLAIM;
import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.USER_NAME_CLAIM;

import java.util.Collection;
import java.util.List;
import org.d3softtech.oauth.servlet.security.user.AuthenticatedUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class BaseTest {

    public static final String TEST_USER_NAME = "test-user";
    public static final String TEST_USER_SSN = "197611119877";
    public static final String TEST_USER_EMAIL = "test-user@d3softtech.com";
    public static final String DUMMY_ISSUER = "dummy-issuer";
    public static final String DUMMY_AUD = "dummy-aud";
    public static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_admin");
    public static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_user");
    public static final GrantedAuthority ROLE_QA = new SimpleGrantedAuthority("ROLE_qa");
    public static final GrantedAuthority ROLE_DEV = new SimpleGrantedAuthority("ROLE_dev");
    protected static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJlbWFpbCI6InRlc3QtdXNlckB0aHVuZGVya2ljay5jb20iLCJzc24iOiIxOTc2MTExMTk4NzciLCJ1c2VyX25hbWUiOiJ0ZXN0LXVzZXIifQ.gScIo9KndFEHWtNnEQEgtXKky5jwdoQisx4R4dUoBLA";

    protected AuthenticatedUser authenticatedUser(Collection<? extends GrantedAuthority> authorities) {
        return new AuthenticatedUser(
            Jwt.withTokenValue(TOKEN)
                .claim(SOCIAL_SECURITY_NUMBER_CLAIM, TEST_USER_SSN)
                .claim(USER_NAME_CLAIM, TEST_USER_NAME)
                .claim(EMAIL_CLAIM, TEST_USER_EMAIL)
                .claim(ROLES_CLAIM, List.of("admin", "user", "dev", "qa"))
                .claim("iss", DUMMY_ISSUER)
                .claim("aud", DUMMY_AUD)
                .header("alg", "HS256").header("typ", "JWT").header("kid", "-KI3Q9nNR7bRofxmeZoXqbHZGew").build(),
            authorities);
    }
}
