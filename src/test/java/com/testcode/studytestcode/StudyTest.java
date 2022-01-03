package com.testcode.studytestcode;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @Test
    @DisplayName("스터디 만들기")
    @EnabledOnOs({OS.WINDOWS})
    void create() throws Exception{
        Study study = new Study(10);
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        Assumptions.assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));

        assertTimeout(Duration.ofMillis(100), ()-> {
            new Study(10);
            Thread.sleep(300);
        });

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야 한다.", e.getMessage());

        assertAll(
                ()->assertNotNull(study),
                ()->assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        ()-> "스터디를 처음 만들면 상태값이 DRAFT이여야 한다."),
                ()->assertTrue(study.getLimit()>0, "스터디 최대 참석 가능 인원은 0보다 커야한다."));
    }

    @Test
    void create1() throws Exception{
        System.out.println("create1");
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }
    @BeforeEach
    void beforeEach(){
        System.out.println("before each");
    }

    @AfterEach
    void afterEach(){
        System.out.println("after each");
    }


}