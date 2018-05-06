package com.parfait.study.starter.namedroutingdatasource.selector;

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
public class SetDataSourceNameAspect {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final BeanFactory factory;

    @Pointcut("@annotation(setDataSourceName)")
    public void hasAnnotation(SetDataSourceName setDataSourceName) {
    }

    @Around("hasAnnotation(setDataSourceName)")
    public Object around(ProceedingJoinPoint joinPoint, SetDataSourceName setDataSourceName) throws Throwable {

        Object getterKey = parser.parseExpression(setDataSourceName.spel()).getValue(joinPoint.getArgs());
        DataSourceNameGetter getter = factory.getBean(setDataSourceName.getterType());
        String dataSourceName = getter.getDataSourceName(getterKey);
        NamedRoutingDataSourceManager.setCurrentDataSourceName(dataSourceName);

        try {
            return joinPoint.proceed();
        } finally {
            NamedRoutingDataSourceManager.removeCurrentDataSourceName();
        }
    }
}
