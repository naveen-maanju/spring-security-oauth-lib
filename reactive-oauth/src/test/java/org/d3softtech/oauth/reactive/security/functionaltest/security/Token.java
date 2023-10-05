package org.d3softtech.oauth.reactive.security.functionaltest.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Token(@JsonProperty("access_token") String accessToken, @JsonProperty("token_type") String tokenType,
                    @JsonProperty("expires_in") int expiresIn) {

}
