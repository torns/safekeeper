package com.changhong.framework.common.function;

/**
 * 异常方法执行抽象
 * @author skylark
 */
public interface SafeKeeperErrorFunction {
    /**
     * 执行方法
     * @return
     */
    Object run(Throwable e);

}
