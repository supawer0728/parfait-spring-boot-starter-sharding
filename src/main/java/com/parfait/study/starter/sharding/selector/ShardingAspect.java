package com.parfait.study.starter.sharding.selector;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@RequiredArgsConstructor
@Aspect
public class ShardingAspect {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final BeanFactory factory;

    @Pointcut("@annotation(sharding)")
    public void hasAnnotation(Sharding sharding) {
    }

    @Around("hasAnnotation(sharding)")
    public Object around(ProceedingJoinPoint joinPoint, Sharding sharding) throws Throwable {

        Object shardKey = parser.parseExpression(sharding.spel()).getValue(joinPoint.getArgs());
        ShardKeyGettable shardKeyGetter = factory.getBean(sharding.keyResolverType());
        String dataSourceName = shardKeyGetter.getShardKey(shardKey);
        ShardingDataSourceManager.setCurrentDataSourceName(dataSourceName);

        try {
            return joinPoint.proceed();
        } finally {
            ShardingDataSourceManager.removeCurrentDataSourceName();
        }
    }
}
