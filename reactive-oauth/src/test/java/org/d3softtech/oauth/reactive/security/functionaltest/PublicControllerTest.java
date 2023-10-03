package org.d3softtech.oauth.reactive.security.functionaltest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.d3softtech.oauth.reactive.security.BaseTest;
import org.d3softtech.oauth.reactive.security.functionaltest.controller.PublicController;
import org.d3softtech.oauth.reactive.security.functionaltest.security.ReactiveSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@SpringJUnitConfig(classes = {PublicController.class, ReactiveSecurityConfig.class})
public class PublicControllerTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @Test
    void actuatorEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/actuator/all").build()).accept(APPLICATION_JSON)
            .exchange().expectStatus().isOk()
            .expectBody().jsonPath("$.response").isEqualTo("actuator");
    }

    @Test
    void swaggerEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/swagger-ui/all").build()).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.response").isEqualTo("swagger-ui");
    }

    @Test
    void openApiEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/open-api/all").build()).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.response").isEqualTo("open-api");

    }

    @Test
    void dummyPublicEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() {

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/dummy-public/all").build()).accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.response").isEqualTo("dummy-public");

    }

}
