package com.testcode.studytestcode;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;
import org.springframework.lang.Nullable;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * junit5는 기본적으로 테스트 메서드를 실행할때 마다 해당 test클래스 자체를 새로운 객체로 만들어 실행하는데
 * 이 기본 전략을 클래스당 하나의 인스턴스를 공유하는 방식으로 수정이 가능하다.
 *
 * 가정 : fast 태그들은 local에서 실행, slow 태그들은 CI환경에서 실행하려고 한다.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //클래스마다 인스턴스를 생성함.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //테스트를 order 라는 애노테이션을 가지고 순서를 정해줌
class StudyTest {

    @Order(2)
    @FastTest //커스텀 애노테이션
    @DisplayName("스터디 만들기")
    @EnabledOnOs({OS.WINDOWS})
    void create() throws Exception{
        Study study = new Study(10);
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        //Assumptions.assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV"))); - 참일 경우에만

        assertTimeout(Duration.ofMillis(400), ()-> {
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

    @Order(1)
    @SlowTest
    @DisplayName("스터디 만들기 slow")
    void create1() throws Exception{
        System.out.println("create1");
    }

    /**
     * 여러번 반복하는 테스트 만들기
     */
    @Order(3)
    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void create_study(RepetitionInfo repetitionInfo){
        System.out.println("test: " + repetitionInfo.getCurrentRepetition() + "/" +
                repetitionInfo.getTotalRepetitions());
    }

    /**
     * 반복하여 테스트할 때 마다 다른 값들로 테스트를 하고 싶을 때
     */
    @ParameterizedTest
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있어요"}) //파라미터 개수만큼 테스트가 수행됨.
    void parameterizedTest(String message){
        System.out.println(message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있어요"}) //파라미터 개수만큼 테스트가 수행됨.
//    @EmptySource //비여있는 문자열을 넣어줌
//    @NullSource//null을 넣어줌
    @NullAndEmptySource
    void parameterizedTest2(String message){
        System.out.println(message);
    }

    @Order(4)
    @ParameterizedTest
    @ValueSource(ints = {10,20,40})
    void parameterizedTest3(Integer limit){
        System.out.println(limit);
    }

    @ParameterizedTest
    @CsvSource({"10, '자바 스터디'","20, '스프링'"})
    void parameterizedTest4(Integer limit, String name){
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    @BeforeAll //무조건 static 이여야 함. Lifecycle.PER_CLASS 경우는 제외
    static void beforeAll(){
        System.out.println("before all");
    }

    @AfterAll //무조건 static 이여야 함. Lifecycle.PER_CLASS 경우는 제외
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