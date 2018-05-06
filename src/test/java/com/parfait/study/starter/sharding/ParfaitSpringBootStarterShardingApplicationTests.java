package com.parfait.study.starter.sharding;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
public class ParfaitSpringBootStarterShardingApplicationTests {

    @Test
    public void testSpel() {
        String spel = "T(java.lang.Math).random() * 100.0";
        ExpressionParser parser = new SpelExpressionParser();
        log.info("result: {}", parser.parseExpression(spel).getValue(new String[]{"a", "b", "c"}));
    }

    @Test
    public void testSpelArray() {
        String spel = "#this[1]";
        ExpressionParser parser = new SpelExpressionParser();
        log.info("result: {}", parser.parseExpression(spel).getValue(new String[]{"a", "b", "c"}));
    }

    @Test
    public void testSpelObjectArray() {
        String spel = "#this[0].id";
        ExpressionParser parser = new SpelExpressionParser();

        TestClass[] testClasses = new TestClass[1];
        testClasses[0] = new TestClass("b");

        log.info("result: {}", parser.parseExpression(spel).getValue(testClasses));
    }

    @Value
    public static class TestClass {
        String id;
    }
}
