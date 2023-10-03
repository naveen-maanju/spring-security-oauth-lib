package org.d3softtech.oauth.reactive.security.functionaltest;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
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

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/users").build())
            .header(AUTHORIZATION, format("Bearer %s", TOKEN)).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.username").isEqualTo(TEST_USER_NAME)
            .jsonPath("$.roles[0]").isEqualTo("admin")
            .jsonPath("$.roles[1]").isEqualTo("user")
            .jsonPath("$.roles[2]").isEqualTo("env_PROD")
            .jsonPath("$.roles[3]").isEqualTo("env_QA")
            .jsonPath("$.aud").isEqualTo(DUMMY_AUD)
            .jsonPath("$.ssn").isEqualTo(TEST_USER_SSN)
            .jsonPath("$.email").isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    void getPreferredName_ShouldAllowAuthenticatedUser_ToAccessSecuredEndpoint() {
        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/preferred-name").build())
            .header(AUTHORIZATION, format("Bearer %s", TOKEN)).accept(APPLICATION_JSON).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.ssn").isEqualTo(TEST_USER_SSN);

    }


    @Test
    void unauthorizedAccess_ShouldReturn403ForMissingRole() {

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/secure/most-secured").build())
            .header(AUTHORIZATION, format("Bearer %s", TOKEN)).accept(APPLICATION_JSON).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isForbidden();
    }
}
