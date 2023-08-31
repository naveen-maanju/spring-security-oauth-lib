package org.d3softtech.oauth.security.functionaltest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.d3softtech.oauth.security.BaseTest;
import org.d3softtech.oauth.security.functionaltest.controller.SecureController;
import org.d3softtech.oauth.security.functionaltest.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@SpringJUnitConfig(classes = {SecureController.class, SecurityConfig.class})
public class SecuredControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserDetails_ShouldAllowAuthenticatedUser_ToAccessSecuredEndpoint() throws Exception {

        mockMvc.perform(get("/secure/users").with(authentication(authenticatedUser(List.of(ROLE_USER, ROLE_ADMIN))))
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ssn", is(TEST_USER_SSN)))
            .andExpect(jsonPath("$.roles", is(List.of("admin", "user", "PROD", "QA"))))
            .andExpect(jsonPath("$.aud", is(DUMMY_AUD)))
            .andExpect(jsonPath("$.name", is(TEST_USER_NAME)));
    }

    @Test
    void getPreferredName_ShouldAllowAuthenticatedUser_ToAccessSecuredEndpoint() throws Exception {
        mockMvc.perform(
                get("/secure/ssn").with(
                        authentication(authenticatedUser(List.of(ROLE_USER, ROLE_ADMIN, ROLE_QA, ROLE_PROD))))
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ssn", is(TEST_USER_SSN)));
    }

    @Test
    void unauthorizedAccess_ShouldReturn403ForMissingRole() throws Exception {
        mockMvc.perform(
                get("/secure/most-secured").with(authentication(authenticatedUser(List.of(ROLE_USER, ROLE_ADMIN))))
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}
