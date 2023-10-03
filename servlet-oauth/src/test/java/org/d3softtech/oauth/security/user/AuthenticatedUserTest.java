package org.d3softtech.oauth.security.user;

import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.EMAIL_CLAIM;
import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.SOCIAL_SECURITY_NUMBER_CLAIM;
import static org.d3softtech.oauth.servlet.security.user.AuthenticatedUser.USER_NAME_CLAIM;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.d3softtech.oauth.security.BaseTest;
import org.d3softtech.oauth.servlet.security.user.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthenticatedUserTest extends BaseTest {

    @Test
    void shouldReturnPreferredUserNameFromJWT_FoAuthenticatedUser() {
        Jwt jwt = Jwt.withTokenValue(TOKEN).header("typ", "JWT").header("alg", "RS256")
            .header("kid", "-KI3Q9nNR7bRofxmeZoXqbHZGew").claim(USER_NAME_CLAIM, TEST_USER_NAME)
            .claim(EMAIL_CLAIM, TEST_USER_EMAIL)
            .claim("roles", List.of("admin", "user", "env_PROD", "env_QA"))
            .claim(SOCIAL_SECURITY_NUMBER_CLAIM, TEST_USER_SSN).build();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(jwt, null);

        assertEquals(TEST_USER_SSN, authenticatedUser.getSocialSecurityNumber());
        assertEquals(TEST_USER_NAME, authenticatedUser.getUserName());
        assertEquals(TEST_USER_EMAIL, authenticatedUser.getEmail());
        assertEquals(List.of("admin", "user", "env_PROD", "env_QA"), authenticatedUser.getRoles());
    }
}
