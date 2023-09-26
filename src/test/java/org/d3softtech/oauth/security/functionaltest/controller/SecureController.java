package org.d3softtech.oauth.security.functionaltest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Map;
import org.d3softtech.oauth.security.annotation.SecurityContextAuthenticatedUser;
import org.d3softtech.oauth.security.annotation.SocialSecurityNumber;
import org.d3softtech.oauth.security.user.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure/")
public class SecureController {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    @PreAuthorize("hasAnyRole('ADMIN','QA','DEV','USER')")
    @GetMapping("/users")
    public ResponseEntity<String> getUserDetails(@SecurityContextAuthenticatedUser AuthenticatedUser authenticatedUser)
        throws JsonProcessingException {
        Map<String, Object> responseMap = Map.of("ssn", authenticatedUser.getSocialSecurityNumber(),
            "username", authenticatedUser.getUserName(),
            "roles", authenticatedUser.getRoles(),
            "iss", authenticatedUser.getToken().getClaimAsString("iss"),
            "aud", authenticatedUser.getToken().getClaimAsString("aud"),
            "name", authenticatedUser.getUserName());
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole('ADMIN') && hasRole('ROLE_QA') && hasRole('ROLE_PROD') && hasRole('ROLE_USER')")
    @GetMapping("/ssn")
    public ResponseEntity<String> getSocialSecurityNumber(@SocialSecurityNumber String socialSecurityNumber)
        throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("ssn", socialSecurityNumber);
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/most-secured")
    public void unauthorizedAccess() {
    }

}
