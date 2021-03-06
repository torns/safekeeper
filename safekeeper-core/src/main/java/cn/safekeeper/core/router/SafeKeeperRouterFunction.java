package cn.safekeeper.core.router;

import cn.safekeeper.common.model.SafeKeeperRequest;
import cn.safekeeper.common.model.SafeKeeperResponse;

/**
 * 路由拦截器验证方法Lambda
 * @author skylark
 */
@FunctionalInterface
public interface SafeKeeperRouterFunction {

	/**
	 * 执行验证的方法
	 * 
	 * @param request  Request包装对象
	 * @param response Response包装对象
	 * @param handler  处理对象
	 */
	public void run(SafeKeeperRequest request, SafeKeeperResponse response, Object handler);

}
