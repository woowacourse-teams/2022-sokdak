package com.wooteco.sokdak.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;

@TestConfiguration
public class AsyncTestConfig {

    @Bean
    public AsyncExecutorPostProcessor asyncExecutorPostProcessor() {
        return new AsyncExecutorPostProcessor();
    }

    static class AsyncExecutorPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("asyncExecutor")) {
                return new SyncTaskExecutor();
            }
            return bean;
        }
    }
}
