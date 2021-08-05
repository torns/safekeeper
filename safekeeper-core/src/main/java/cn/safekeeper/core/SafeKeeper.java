package cn.safekeeper.core;

import cn.safekeeper.core.manager.SafeKeeperLogic;
import cn.safekeeper.core.manager.SafeKeeperManager;

/**
 * 用户界面
 * @author skylark
 */
public class SafeKeeper {

    /**
     * 获取逻辑执行对象
     * @param loginType 安全维度
     * @return 执行对象
     */
    public static SafeKeeperLogic safeLogic(String loginType){
        return SafeKeeperManager.getStpLogic(loginType);
    }

}
