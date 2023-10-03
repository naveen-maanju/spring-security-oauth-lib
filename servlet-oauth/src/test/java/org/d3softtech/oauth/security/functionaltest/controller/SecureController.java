package org.d3softtech.oauth.security.functionaltest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Map;
import org.d3softtech.oauth.servlet.security.annotation.SecurityContextAuthenticatedUser;
import org.d3softtech.oauth.servlet.security.annotation.SocialSecurityNumber;
import org.d3softtech.oauth.servlet.security.user.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure/")
public class SecureController {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    @PreAuthorize("hasAnyRole(@Role.ADMIN,@Role.USER,@Role.DEV,@Role.QA)")
    @GetMapping("/users")
    public ResponseEntity<String> getUserDetails(
        @SecurityContextAuthenticatedUser AuthenticatedUser authenticatedUser)
        throws JsonProcessingException {
        Map<String, Object> responseMap = Map.of("username", authenticatedUser.getUserName(),
            "ssn", authenticatedUser.getSocialSecurityNumber(),
            "roles", authenticatedUser.getRoles(),
            "iss", authenticatedUser.getToken().getClaimAsString("iss"),
            "aud", authenticatedUser.getToken().getClaimAsString("aud"),
            "email", authenticatedUser.getEmail());
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole(@Role.ADMIN) && hasRole(@Role.QA) && hasRole(@Role.USER) && hasRole(@Role.DEV)")
    @GetMapping("/preferred-name")
    public ResponseEntity<String> getPreferredName(@SocialSecurityNumber String ssn)
        throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("ssn", ssn);
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/most-secured")
    public void unauthorizedAccess() {
    }

}
