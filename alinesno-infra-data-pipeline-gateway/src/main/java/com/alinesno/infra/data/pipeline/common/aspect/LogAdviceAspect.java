// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.common.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alinesno.infra.data.pipeline.common.annotation.LogAccess;
import com.alinesno.infra.data.pipeline.common.annotation.LogOperate;
import com.alinesno.infra.data.pipeline.dao.SystemLogDAO;
import com.alinesno.infra.data.pipeline.entity.SystemLogEntity;
import com.alinesno.infra.data.pipeline.entity.SystemUserEntity;
import com.alinesno.infra.data.pipeline.type.LogTypeEnum;
import com.alinesno.infra.data.pipeline.util.CacheUtils;
import com.alinesno.infra.data.pipeline.util.ServletUtils;
import com.alinesno.infra.data.pipeline.util.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogAdviceAspect {

  private final static ExpressionParser expressionParser = new SpelExpressionParser();

  @Resource
  private SystemLogDAO systemLogDAO;

  private String getParsedDescription(String desc, EvaluationContext ctx) {
    try {
      return expressionParser.parseExpression(desc).getValue(ctx).toString();
    } catch (Exception e) {
      return desc;
    }
  }

  private EvaluationContext getEvaluationContext(ProceedingJoinPoint joinPoint) {
    // 将方法的参数名和参数值一一对应的放入上下文中
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    List<String> paramNameList = Arrays.asList(methodSignature.getParameterNames());
    List<Object> paramList = Arrays.asList(joinPoint.getArgs());
    EvaluationContext ctx = new StandardEvaluationContext();
    for (int i = 0; i < paramNameList.size(); i++) {
      ctx.setVariable(paramNameList.get(i), paramList.get(i));
    }

    return ctx;
  }

  @Pointcut("@annotation(com.alinesno.infra.data.pipeline.common.annotation.LogAccess)")
  public void loginPointCut() {
  }

  @Around("loginPointCut()")
  public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
    long beginTime = System.currentTimeMillis();
    long finishTime = 0L;
    Object returnObject = null;
    Throwable throwable = null;
    try {
      returnObject = joinPoint.proceed();
    } catch (Throwable e) {
      throwable = e;
    } finally {
      finishTime = System.currentTimeMillis();
    }

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    if (method.isAnnotationPresent(LogAccess.class)) {
      EvaluationContext ctx = this.getEvaluationContext(joinPoint);
      this.recordAccessLog(methodSignature, ctx, throwable, finishTime - beginTime);
    }

    if (null != throwable) {
      throw throwable;
    }

    return returnObject;
  }

  private void recordAccessLog(MethodSignature methodSignature, EvaluationContext ctx,
      Throwable throwable, long elapse) {
    Method method = methodSignature.getMethod();
    LogAccess logAnnotation = method.getAnnotation(LogAccess.class);
    String moduleName = logAnnotation.value();
    String description = getParsedDescription(logAnnotation.description(), ctx);
    if (null == throwable) {
      SystemLogEntity systemLogEntity = SystemLogEntity.builder()
          .type(LogTypeEnum.ACCESS_LOG.getValue())
          .username(this.getUsernameFromToken())
          .ipAddress(ServletUtils.getIpAddr())
          .moduleName(moduleName)
          .content(description)
          .urlPath(ServletUtils.getPathUri())
          .userAgent(ServletUtils.getUserAgent())
          .failed(Boolean.FALSE)
          .exception(null)
          .elapseSeconds(elapse)
          .build();
      systemLogDAO.insert(systemLogEntity);
    }
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

  @Pointcut("@annotation(com.alinesno.infra.data.pipeline.common.annotation.LogOperate)")
  public void operatorPointCut() {
  }

  @Around("operatorPointCut()")
  public Object aroundOperator(ProceedingJoinPoint joinPoint) throws Throwable {
    long beginTime = System.currentTimeMillis();
    long finishTime = 0L;
    Object returnObject = null;
    Throwable throwable = null;
    try {
      returnObject = joinPoint.proceed();
    } catch (Throwable e) {
      throwable = e;
    } finally {
      finishTime = System.currentTimeMillis();
    }

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    if (method.isAnnotationPresent(LogOperate.class)) {
      EvaluationContext ctx = this.getEvaluationContext(joinPoint);
      this.recordOperateLog(methodSignature, ctx, throwable, finishTime - beginTime);
    }

    if (null != throwable) {
      throw throwable;
    }

    return returnObject;
  }

  private void recordOperateLog(MethodSignature methodSignature, EvaluationContext ctx,
      Throwable throwable, long elapse) {
    Method method = methodSignature.getMethod();
    LogOperate logAnnotation = method.getAnnotation(LogOperate.class);
    String moduleName = logAnnotation.name();
    String description = getParsedDescription(logAnnotation.description(), ctx);
    if (null == throwable) {
      SystemLogEntity systemLogEntity = SystemLogEntity.builder()
          .type(LogTypeEnum.OPERATE_LOG.getValue())
          .username(this.getUsernameFromToken())
          .ipAddress(ServletUtils.getIpAddr())
          .moduleName(moduleName)
          .content(description)
          .urlPath(ServletUtils.getPathUri())
          .userAgent(ServletUtils.getUserAgent())
          .failed(Boolean.FALSE)
          .exception(null)
          .elapseSeconds(elapse)
          .build();
      systemLogDAO.insert(systemLogEntity);
    } else {
      SystemLogEntity systemLogEntity = SystemLogEntity.builder()
          .type(LogTypeEnum.OPERATE_LOG.getValue())
          .username(this.getUsernameFromToken())
          .ipAddress(ServletUtils.getIpAddr())
          .moduleName(moduleName)
          .content(description)
          .urlPath(ServletUtils.getPathUri())
          .userAgent(ServletUtils.getUserAgent())
          .failed(Boolean.TRUE)
          .exception(ExceptionUtil.stacktraceToString(throwable))
          .elapseSeconds(elapse)
          .build();
      systemLogDAO.insert(systemLogEntity);
    }
  }

  private String getUsernameFromToken() {
    HttpServletRequest request = ServletUtils.getHttpServletRequest();
    String token = TokenUtils.getRequestToken(request);
    if (Objects.isNull(token)) {
      return null;
    }

    Object cache = CacheUtils.get(token);
    if (!Objects.isNull(cache)) {
      SystemUserEntity systemUserEntity = (SystemUserEntity) cache;
      return systemUserEntity.getUsername();
    }

    return null;
  }

}