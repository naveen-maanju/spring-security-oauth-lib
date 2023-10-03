package org.d3softtech.oauth.reactive.security.functionaltest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class PublicController {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    @GetMapping("/actuator/all")
    public ResponseEntity<String> actuator() throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("response", "actuator");
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @GetMapping("/swagger-ui/all")
    public ResponseEntity<String> swagger() throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("response", "swagger-ui");
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @GetMapping("/open-api/all")
    public ResponseEntity<String> openApi() throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("response", "open-api");
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

    @GetMapping("/dummy-public/all")
    public ResponseEntity<String> dummyPublic() throws JsonProcessingException {
        Map<String, String> responseMap = Map.of("response", "dummy-public");
        return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }
}
