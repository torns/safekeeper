package com.changhong.framework.plugin.context;

import com.changhong.framework.common.model.SafeKeeperContext;
import com.changhong.framework.common.model.SafeKeeperRequest;
import com.changhong.framework.common.model.SafeKeeperResponse;
import com.changhong.framework.common.model.SafeKeeperStorage;
import com.changhong.framework.plugin.model.SafeKeeperRequestForServlet;
import com.changhong.framework.plugin.model.SafeKeeperResponseForServlet;
import com.changhong.framework.plugin.model.SafeKeeperStorageForServlet;

/**
 * 上下文实现类
 * @author skylark
 */
public class SpringSafeKeeperContext implements SafeKeeperContext {
    @Override
    public SafeKeeperRequest getRequest() {
        return new SafeKeeperRequestForServlet(SpringMvcUtils.getRequest());
    }

    @Override
    public SafeKeeperResponse getResponse() {
        return new SafeKeeperResponseForServlet(SpringMvcUtils.getResponse());
    }

    @Override
    public SafeKeeperStorage getStorage() {
        return new SafeKeeperStorageForServlet(SpringMvcUtils.getRequest());
    }

    @Override
    public boolean matchPath(String pattern, String path) {
        return SafeKeeperPathMatcherHolder.getPathMatcher().match(pattern,path);
    }
}
