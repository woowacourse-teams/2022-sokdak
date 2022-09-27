package com.wooteco.sokdak.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before("execution(public * com.wooteco.sokdak.*.controller.*.*(..))")
    public void logRequest(JoinPoint joinPoint) throws Throwable {
        final Map<String, Object> parameters = extractParameters(joinPoint);
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String methodName = signature.getMethod().getName();

        log.info("======= method: {}, body: {} ==========",
                methodName, objectMapper.writeValueAsString(parameters));
    }

    private Map<String, Object> extractParameters(JoinPoint joinPoint) {
        final CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        final String[] parameterNames = signature.getParameterNames();

        final Map<String, Object> parameters = new HashMap<>();
        final int numOfParameters = parameterNames.length;

        for (int i = 0; i < numOfParameters; i++) {
            parameters.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return parameters;
    }
}
