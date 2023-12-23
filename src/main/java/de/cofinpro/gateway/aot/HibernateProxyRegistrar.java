package de.cofinpro.gateway.aot;

import java.util.Arrays;
import java.util.Set;

import org.hibernate.query.CommonQueryContract;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.hql.spi.SqmQueryImplementor;
import org.hibernate.query.spi.DomainQueryExecutionContext;
import org.hibernate.query.sqm.internal.SqmInterpretationsKey;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BodyFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerHandlerSupplier;
import org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

public class HibernateProxyRegistrar implements RuntimeHintsRegistrar {

    private final Set<Class<?>> functionProviders = Set.of(
            HandlerFunctions.class, LoadBalancerHandlerSupplier.class,
            FilterFunctions.class, BeforeFilterFunctions.class,
            AfterFilterFunctions.class, TokenRelayFilterFunctions.class,
            BodyFilterFunctions.class, CircuitBreakerFilterFunctions.class,
            GatewayRouterFunctions.class, LoadBalancerFilterFunctions.class,
            GatewayRequestPredicates.class, Bucket4jFilterFunctions.class
    );

    @Override
    public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {
        ProxyHints proxies = hints.proxies();
        proxies.registerJdkProxy(
                SqmQueryImplementor.class,
                SqmInterpretationsKey.InterpretationsKeySource.class,
                DomainQueryExecutionContext.class,
                SelectionQuery.class,
                CommonQueryContract.class
        );

        final var reflectionHints = hints.reflection();
        functionProviders.forEach(c -> addHintsForClass(reflectionHints, c));
    }

    private static void addHintsForClass(ReflectionHints reflectionHints, Class<?> clazz) {
        if (!ClassUtils.isPresent(clazz.getName(), ClassUtils.getDefaultClassLoader())) {
            return; // safety net
        }
        Arrays.stream(clazz.getMethods())
                .forEach(m -> reflectionHints.registerMethod(m, ExecutableMode.INVOKE));
    }
}