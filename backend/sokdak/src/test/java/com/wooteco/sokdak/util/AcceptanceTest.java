package com.wooteco.sokdak.util;

import io.restassured.RestAssured;
import java.time.Clock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
