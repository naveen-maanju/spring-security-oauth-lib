package org.d3softtech.oauth.security.functionaltest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.d3softtech.oauth.security.BaseTest;
import org.d3softtech.oauth.security.functionaltest.controller.PublicController;
import org.d3softtech.oauth.security.functionaltest.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@SpringJUnitConfig(classes = {PublicController.class, SecurityConfig.class})
public class PublicControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void actuatorEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/all").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.response", is("actuator")));
    }

    @Test
    void swaggerEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() throws Exception {
        mockMvc.perform(get("/swagger-ui/all").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.response", is("swagger-ui")));
    }

    @Test
    void openApiEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() throws Exception {
        mockMvc.perform(get("/open-api/all").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.response", is("open-api")));
    }

    @Test
    void dummyPublicEndpoint_ShouldAllowNonAuthenticatedUser_ToAccessPublicEndpoint() throws Exception {
        mockMvc.perform(
                get("/dummy-public/all").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.response", is("dummy-public")));
    }

}
