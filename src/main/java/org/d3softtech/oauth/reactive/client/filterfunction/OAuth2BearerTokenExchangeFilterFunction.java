package org.d3softtech.oauth.reactive.client.filterfunction;


import java.util.Map;
import org.d3softtech.oauth.reactive.security.user.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public final class OAuth2BearerTokenExchangeFilterFunction implements ExchangeFilterFunction {

    static final String SECURITY_REACTOR_CONTEXT_ATTRIBUTES_KEY = "org.springframework.security.SECURITY_CONTEXT_ATTRIBUTES";

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return oauth2Token().map((token) -> bearer(request, token))
            .defaultIfEmpty(request)
            .flatMap(next::exchange);
    }

    private Mono<AuthenticatedUser> oauth2Token() {
        return Mono.deferContextual(Mono::just)
            .cast(Context.class)
            .flatMap(this::currentAuthentication)
            .filter((authentication) -> authentication instanceof AuthenticatedUser)
            .cast(AuthenticatedUser.class);
    }

    private Mono<Authentication> currentAuthentication(Context ctx) {
        return Mono.justOrEmpty(getAttribute(ctx, Authentication.class));
    }

    private <T> T getAttribute(Context ctx, Class<T> clazz) {
        // NOTE: SecurityReactorContextConfiguration.SecurityReactorContextSubscriber adds
        // this key
        if (!ctx.hasKey(SECURITY_REACTOR_CONTEXT_ATTRIBUTES_KEY)) {
            return null;
        }

        Map<Class<T>, T> attributes = ctx.get(SECURITY_REACTOR_CONTEXT_ATTRIBUTES_KEY);
        return attributes.get(clazz);
    }

    private ClientRequest bearer(ClientRequest request, AuthenticatedUser token) {
        return ClientRequest.from(request)
            .headers((headers) -> headers.setBearerAuth(token.getToken().getTokenValue()))
            .build();
    }
}
