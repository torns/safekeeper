package com.changhong.framework.plugin.aop;


import com.changhong.framework.common.constant.SafeKeeperConstant;
import com.changhong.framework.core.manager.SafeKeeperManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * SafeKeeper 基于 Spring Aop 的注解鉴权
 * @author skylark
 */
@Aspect
@Order(SafeKeeperConstant.ASSEMBLY_ORDER)
public class SafeKeeperCheckAspect {
	
	/**
	 * 构建
	 */
	public SafeKeeperCheckAspect() {
	}

	/**
	 * 定义AOP签名 (切入所有使用sa-token鉴权注解的方法)
	 */
	public static final String POINTCUT_SIGN = 
			"@within(com.changhong.framework.common.annotations.SafeKeeperHasLogin) || @annotation(com.changhong.framework.common.annotations.SafeKeeperHasLogin) || "
			+ "@within(com.changhong.framework.common.annotations.SafeKeeperHasPermission) || @annotation(com.changhong.framework.common.annotations.SafeKeeperHasPermission) || "
			+ "@within(com.changhong.framework.common.annotations.SafeKeeperHasRole) || @annotation(com.changhong.framework.common.annotations.SafeKeeperHasRole)";

	/**
	 * 声明AOP签名
	 */
	@Pointcut(POINTCUT_SIGN)
	public void pointcut() {
	}

	/**
	 * 环绕切入
	 * 
	 * @param joinPoint 切面对象
	 * @return 底层方法执行后的返回值
	 * @throws Throwable 底层方法抛出的异常
	 */
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		
		// 注解鉴权
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		SafeKeeperManager.getSaTokenAction().checkMethodAnnotation(signature.getMethod());
		try {
			// 执行原有逻辑
			Object obj = joinPoint.proceed();
			return obj;
		} catch (Throwable e) {
			throw e;
		}
	}

}
