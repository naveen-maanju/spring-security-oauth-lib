### Common Security module

Can be used for all backend components where authorization is required. This component uses
Spring Security oAuth2 and validate the Bearer token provided as authorization header. This
component will do all required validations of the JWT and provide the authenticated user details as
Principal.

## How to use

1. Define dependency:
   To use this component add it as dependency in ``pom.xml`` as below:

```xml

<dependency>
  <groupId>org.d3softtech</groupId>
  <artifactId>spring-security-oauth-lib</artifactId>
  <version>${lib.version}</version>
</dependency>
```

2. Configure Spring security: By default this component whitelist (permit all) requests
   for ``/actuator/*``,``/swagger-ui/*`` and ``/v3/api-docs/*`` and will check for Bearer token for
   all other endpoints i.e. user should be authenticated to access those endpoints. If you want
   whitelist more endpoints, then can do as below

```java

@Configuration
public class SpringSecurityConfig extends AbstractSecurityConfig {

    protected Stream<String> publicEndpoints() {
        return Stream.of("/dummy-public/**", "/public/**", "/non-secured/**");
    }
}
```

3. Access Secured User details: Authenticated user's details can be access as below:

```java
//Inside controller

//To fetch all the details use @AuthenticationPrincipal AuthenticatedUser authenticatedUser
@GetMapping("/users")
public ResponseEntity<String> getUserDetails(@SecurityContextAuthenticatedUser AuthenticatedUser authenticatedUser)
    throws JsonProcessingException{
    Map<String, Object> responseMap=Map.of(
    "preferred_username",authenticatedUser.getPreferredUsername(),
    "roles",authenticatedUser.getRoles(),
    "iss",authenticatedUser.getToken().getClaimAsString("iss"),
    "aud",authenticatedUser.getToken().getClaimAsString("aud"),
    "name",authenticatedUser.getDisplayName());
    return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }

@GetMapping("/preferred-name")
public ResponseEntity<String> getPreferredName(@PreferredUserName String preferredUserName)
    throws JsonProcessingException{
    Map<String, String> responseMap=Map.of("preferredName",preferredUserName);
    return ResponseEntity.ok(JSON_MAPPER.writeValueAsString(responseMap));
    }
```

Inside service methods can access authenticated user as

```java
AuthenticatedUser authenticatedUser=(AuthenticatedUser)SecurityContextHolder.getContext().getAuthentication();
```

### Granular access

Now authorization can be done at granular level, that is, we can define the access (authorization)
at method level based on the claim "roles". User JWT have claim "roles", which is list of assigned
roles to the user and that can be referred while executing a method whether use has the required
role or not.

To secure a method we can use ``@PreAuthorize`` annotation. Example in test package.