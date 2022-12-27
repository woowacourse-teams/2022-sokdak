package com.wooteco.sokdak.util;

import com.wooteco.sokdak.config.AsyncTestConfig;
import io.restassured.RestAssured;
import java.time.Clock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(AsyncTestConfig.class)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @SpyBean
    protected Clock clock;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleaner.insertInitialData();
    }

    @AfterEach
    void clearDatabase() {
        databaseCleaner.clear();
    }
}
