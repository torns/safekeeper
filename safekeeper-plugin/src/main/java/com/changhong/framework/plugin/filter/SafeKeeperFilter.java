package com.changhong.framework.plugin.filter;

import com.changhong.framework.common.constant.SafeKeeperConstant;
import com.changhong.framework.common.exception.SafeKeeperLockedException;
import com.changhong.framework.common.exception.SafeKeeperLoginException;
import com.changhong.framework.common.exception.SafeKeeperPermissionException;
import com.changhong.framework.common.exception.SafeKeeperRoleException;
import com.changhong.framework.common.function.SafeKeeperErrorFunction;
import com.changhong.framework.common.function.SafeKeeperFilterFunction;
import com.changhong.framework.common.function.SafeKeeperFunction;
import com.changhong.framework.plugin.context.SafeKeeperRouter;
import com.changhong.framework.plugin.web.CodeEnum;
import com.changhong.framework.plugin.web.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet全局过滤器 
 * @author skylark
 *
 */
@Order(SafeKeeperConstant.ASSEMBLY_ORDER)
public class SafeKeeperFilter implements Filter {
	/**
	 * 安全拦截路由
	 */
	private List<String> includeList = new ArrayList<>();

	/**
	 * 安全放行路由
	 */
	private List<String> excludeList = new ArrayList<>();

	/**
	 * 对象json
	 */
	private final ObjectMapper mapper=new ObjectMapper();

	/**
	 * 添加 拦截路由
	 * @param paths 路由
	 * @return 对象
	 */
	public SafeKeeperFilter addInclude(String... paths) {
		includeList.addAll(Arrays.asList(paths));
		return this;
	}
	
	/**
	 * 添加 放行路由
	 * @param paths 路由
	 * @return 对象
	 */
	public SafeKeeperFilter addExclude(String... paths) {
		excludeList.addAll(Arrays.asList(paths));
		return this;
	}

	/**
	 * 写入 拦截路由 集合
	 * @param pathList 路由集合 
	 * @return 对象
	 */
	public SafeKeeperFilter setIncludeList(List<String> pathList) {
		includeList.addAll(pathList);
		return this;
	}
	
	/**
	 * 写入 放行路由 集合
	 * @param pathList 路由集合 
	 * @return 对象
	 */
	public SafeKeeperFilter setExcludeList(List<String> pathList) {
		excludeList.addAll(pathList);
		return this;
	}
	
	/**
	 * 获取 拦截路由 集合
	 * @return see note 
	 */
	private List<String> getIncludeList() {
		return includeList;
	}
	
	/**
	 * 获取 [放行路由] 集合
	 * @return see note 
	 */
	private List<String> getExcludeList() {
		return excludeList;
	}


	/**
	 * 认证函数：每次请求执行 
	 */
	private SafeKeeperFilterFunction auth = auth -> {};

	/**
	 * 异常处理函数：每次[认证函数]发生异常时执行此函数
	 */
	public SafeKeeperErrorFunction error = e-> null;

	/**
	 * 前置函数：在每次[认证函数]之前执行 
	 */
	private SafeKeeperFilterFunction beforeAuth =auth -> {};

	/**
	 * 写入[认证函数]: 每次请求执行 
	 * @param auth see note 
	 * @return 对象自身
	 */
	public SafeKeeperFilter setAuth(SafeKeeperFilterFunction auth) {
		this.auth = auth;
		return this;
	}

	/**
	 * 写入[异常处理函数]：每次[认证函数]发生异常时执行此函数 
	 * @param error see note 
	 * @return 对象自身
	 */
	public SafeKeeperFilter setError(SafeKeeperErrorFunction error) {
		this.error = error;
		return this;
	}

	/**
	 * 写入[前置函数]：在每次[认证函数]之前执行
	 * @param beforeAuth see note 
	 * @return 对象自身
	 */
	public SafeKeeperFilter setBeforeAuth(SafeKeeperFilterFunction beforeAuth) {
		this.beforeAuth = beforeAuth;
		return this;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			// 执行全局过滤器
			SafeKeeperRouter.match(includeList, excludeList, () -> {
				beforeAuth.run(request);
				auth.run(request);
			});
			chain.doFilter(request, response);
		} catch (Throwable e) {
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().print(deserializeExceptionToJson(e));
			return;
		}

	}

	private String deserializeExceptionToJson(Throwable e){
		Object result = error.run(e);
		String resultString = String.valueOf(result);
		if(e instanceof SafeKeeperErrorFunction){
			Result<String> objectResult = Result.failedWith(null, CodeEnum.ERROR.getCode(), resultString);
			try {
				return mapper.writeValueAsString(objectResult);
			} catch (JsonProcessingException jsonProcessingException) {
				jsonProcessingException.printStackTrace();
			}
		}else if(e instanceof SafeKeeperLockedException){
			SafeKeeperLockedException ex=(SafeKeeperLockedException)e;
			Result<String> objectResult = Result.failedWith(null, ex.getCode(), resultString);
			try {
				return mapper.writeValueAsString(objectResult);
			} catch (JsonProcessingException jsonProcessingException) {
				jsonProcessingException.printStackTrace();
			}
		}else if(e instanceof SafeKeeperLoginException){
			SafeKeeperLoginException ex=(SafeKeeperLoginException)e;
			Result<String> objectResult = Result.failedWith(null, ex.getCode(), resultString);
			try {
				return mapper.writeValueAsString(objectResult);
			} catch (JsonProcessingException jsonProcessingException) {
				jsonProcessingException.printStackTrace();
			}
		}else if(e instanceof SafeKeeperPermissionException){
			SafeKeeperPermissionException ex=(SafeKeeperPermissionException)e;
			Result<String> objectResult = Result.failedWith(null, ex.getCode(), resultString);
			try {
				return mapper.writeValueAsString(objectResult);
			} catch (JsonProcessingException jsonProcessingException) {
				jsonProcessingException.printStackTrace();
			}
		}else if(e instanceof SafeKeeperRoleException){
			SafeKeeperRoleException ex=(SafeKeeperRoleException)e;
			Result<String> objectResult = Result.failedWith(null, ex.getCode(), resultString);
			try {
				return mapper.writeValueAsString(objectResult);
			} catch (JsonProcessingException jsonProcessingException) {
				jsonProcessingException.printStackTrace();
			}
		}
		Result<String> objectResult = Result.failedWith(null,
				CodeEnum.ERROR.getCode(),
				resultString);
		try {
			return mapper.writeValueAsString(objectResult);
		} catch (JsonProcessingException jsonProcessingException) {
			jsonProcessingException.printStackTrace();
		}
		return resultString;
	}



	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}

	
	
}
