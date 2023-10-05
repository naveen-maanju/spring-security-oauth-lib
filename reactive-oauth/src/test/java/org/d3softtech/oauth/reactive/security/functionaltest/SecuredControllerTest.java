package org.d3softtech.oauth.reactive.security.functionaltest;

import static org.d3softtech.oauth.reactive.security.user.RoleContainer.ADMIN;
import static org.d3softtech.oauth.reactive.security.user.RoleContainer.PROD;
import static org.d3softtech.oauth.reactive.security.user.RoleContainer.QA;
import static org.d3softtech.oauth.reactive.security.user.RoleContainer.USER;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.d3softtech.oauth.reactive.security.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
public class SecuredControllerTest extends BaseTest {


    @Autowired
    private ApplicationContext testApplication;
    private WebTestClient webTestClient;

    @BeforeEach
    public void beforeTest() {
        webTestClient = WebTestClient.bindToApplicationContext(testApplication).build();
    }

    @Test
    void getUserDetails_ShouldAllowAuthenticatedUser_ToAccessSecuredEndpoint() {

        final String token = getToken().accessToken();
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/users").build())
            .headers(headers -> headers.setBearerAuth(token)).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.username").isEqualTo(TEST_USER_NAME)
            .jsonPath("$.roles[*]").value(containsInAnyOrder("admin", "user"))
            .jsonPath("$.aud").isEqualTo(DUMMY_AUD)
            .jsonPath("$.ssn").isEqualTo(TEST_USER_SSN)
            .jsonPath("$.email").isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    void getPreferredName_ShouldAllowAuthenticatedUser_ToAccessSecuredEndpoint() {
        final String token = getToken(ADMIN, USER, QA, PROD).accessToken();

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/preferred-name").build())
            .headers(headers -> headers.setBearerAuth(token)).accept(APPLICATION_JSON).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.ssn").isEqualTo(TEST_USER_SSN);

    }


    @Test
    void unauthorizedAccess_ShouldReturn403ForMissingRole() {
        final String token = getToken(ADMIN, USER, QA, PROD).accessToken();

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/most-secured").build())
            .headers(headers -> headers.setBearerAuth(token)).accept(APPLICATION_JSON).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isForbidden();
    }
}
