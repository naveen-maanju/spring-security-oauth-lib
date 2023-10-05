package org.d3softtech.oauth.reactive.security.functionaltest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.d3softtech.oauth.reactive.security.annotation.SecurityContextAuthenticatedUser;
import org.d3softtech.oauth.reactive.security.annotation.SocialSecurityNumber;
import org.d3softtech.oauth.reactive.security.user.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/secure/")
public class SecureController {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    @PreAuthorize("hasAnyRole(@R.ADMIN, @R.USER,@R.QA,@R.PROD)")
    @GetMapping("/users")
    public Mono<String> getUserDetails(
        @SecurityContextAuthenticatedUser AuthenticatedUser authenticatedUser)
        throws JsonProcessingException {
        Map<String, Object> responseMap = Map.of("ssn", authenticatedUser.getSocialSecurityNumber(),
            "username", authenticatedUser.getUserName(),
            "roles", authenticatedUser.getRoles(),
            "iss", authenticatedUser.getToken().getClaimAsString("iss"),
            "aud", authenticatedUser.getToken().getAudience().get(0),
            "email", authenticatedUser.getEmail());
        return Mono.just(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole(@R.ADMIN) && hasRole(@R.QA) && hasRole(@R.PROD) && hasRole(@R.USER)")
    @GetMapping("/preferred-name")
    public Mono<String> getPreferredName(@SocialSecurityNumber String socialSecurityNumber)
        throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("ssn", socialSecurityNumber);
        return Mono.just(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/most-secured")
    public Mono<ResponseEntity<?>> unauthorizedAccess() {

        log.error("Ooops!! I got called!!!!");
        return Mono.just(ResponseEntity.of(Optional.empty()));
    }

}
