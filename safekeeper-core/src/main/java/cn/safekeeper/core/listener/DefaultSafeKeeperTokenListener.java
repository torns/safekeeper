package cn.safekeeper.core.listener;

import cn.safekeeper.common.model.SafeKeeperLoginModel;

/**
 * 默认监听实现
 * @author skylark
 */
public class DefaultSafeKeeperTokenListener implements SafeKeeperTokenListener{
    @Override
    public void doLogin(String loginType, Object loginId, SafeKeeperLoginModel loginModel) {

    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doLogoutByLoginId(String loginType, Object loginId, String tokenValue, String device) {

    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue, String device) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }
}
