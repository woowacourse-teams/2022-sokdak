package com.wooteco.sokdak.aspect.performance;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    private final ThreadLocal<QueryCounter> queryCounter = new ThreadLocal<>();

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    public void performancePointcut() {
    }

    @Around("performancePointcut()")
    public Object start (ProceedingJoinPoint point) throws Throwable {
        final Connection connection = (Connection) point.proceed();
        queryCounter.set(new QueryCounter());
        final QueryCounter counter = this.queryCounter.get();

        final Connection proxyConnection = getProxyConnection(connection, counter);
        queryCounter.remove();
        return proxyConnection;
    }

    private Connection getProxyConnection(Connection connection, QueryCounter counter) {
        return (Connection) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionHandler(connection, counter)
        );
    }
}
