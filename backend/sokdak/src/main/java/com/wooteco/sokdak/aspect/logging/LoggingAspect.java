package com.wooteco.sokdak.aspect.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final List<Class<? extends Annotation>> MAPPING_CLASSES = List
            .of(RequestMapping.class, GetMapping.class, PostMapping.class,
                    PutMapping.class, PatchMapping.class, DeleteMapping.class);

    @Pointcut("execution(public * com.wooteco.sokdak.*.controller.*.*(..)) && !@annotation(com.wooteco.sokdak.aspect.logging.NoLogging)")
    private void loggingCondition() {
    }

    @Before("loggingCondition()")
    public void logRequest(JoinPoint joinPoint) {
        final Map<String, Object> parameters = extractParameters(joinPoint);
        final Class<?> clazz = joinPoint.getTarget().getClass();
        final String baseUrl = extractBaseUrl(clazz);

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final String requestUrl = MAPPING_CLASSES.stream()
                .filter(mappingClass -> signature.getMethod().isAnnotationPresent(mappingClass))
                .map(mappingClass -> getRequestUrl(signature.getMethod(), mappingClass, baseUrl))
                .findAny()
                .orElse("");

        try {
            log.info("\n=======\nurl: {}, body: {} \n==========",
                    requestUrl, objectMapper.writeValueAsString(parameters));
        } catch (JsonProcessingException exception) {
            log.warn("logging failed!!");
        }
    }

    private String extractBaseUrl(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequestMapping.class)){
            final RequestMapping requestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
            return extractPathValue(requestMapping.value());
        }
        return "";
    }

    private String getRequestUrl(Method method, Class<? extends Annotation> mappedAnnotation, String baseUrl){
        final Annotation annotation = method.getAnnotation(mappedAnnotation);

        try {
            final String[] values = (String[]) mappedAnnotation.getMethod("value").invoke(annotation);
            final String httpMethod = mappedAnnotation.getSimpleName().split("Mapping")[0].toUpperCase();
            final String format = String.format("%s %s%s", httpMethod, baseUrl, extractPathValue(values));
            return format;
        } catch (Exception e){
            log.warn("logging request url failed!!");
            return null;
        }
    }

    private String extractPathValue(String[] values) {
        if (values.length > 0){
            return values[0];
        }
        return "";
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

    @AfterReturning(value = "loggingCondition()", returning = "responseEntity")
    public void logResponse(JoinPoint joinPoint, ResponseEntity<?> responseEntity){
        if(responseEntity.hasBody()) {
            printResponse(responseEntity);
        }
    }

    private void printResponse(ResponseEntity<?> responseEntity) {
        try {
            log.info("\n=======\nresponse : {} \n========", objectMapper.writeValueAsString(responseEntity.getBody()));
        } catch (JsonProcessingException exception) {
            log.warn("logging failed!!");
        }
    }
}
