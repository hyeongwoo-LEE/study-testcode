package com.testcode.studytestcode;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //이 애노테이션을 어디에 쓸 수 있는가. method
@Retention(RetentionPolicy.RUNTIME) //이 애노테이션을 사용한 코드는 runtime까지 유지해야한다.
@Test
@Tag("slow")
public @interface SlowTest {
}
