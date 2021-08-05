package cn.safekeeper.core.manager;

import cn.safekeeper.common.configuration.SafeKeeperCodeMsgConfiguration;
import cn.safekeeper.common.configuration.SafeKeeperConfiguration;
import cn.safekeeper.common.exception.SafeKeeperException;
import cn.safekeeper.common.model.SafeKeeperAuthorizationCallBack;
import cn.safekeeper.common.model.SafeKeeperContext;
import cn.safekeeper.common.utils.SafeKeeperUtils;
import cn.safekeeper.core.logic.DefaultSafeKeeperLogicAction;
import cn.safekeeper.core.logic.SafeKeeperLogicAction;
import cn.safekeeper.core.listener.DefaultSafeKeeperTokenListener;
import cn.safekeeper.core.listener.SafeKeeperTokenListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SafeKeeper管理中心
 * @author skylark
 */
public class SafeKeeperManager {

    /**
     * 配置文件 Bean
     */
    private static SafeKeeperConfiguration config;
    public static void setConfig(SafeKeeperConfiguration config) {
        SafeKeeperManager.config = config;
        if(config.isPrint()) {
            //打印logo
            SafeKeeperUtils.printSaToken();
        }
        //TODO 调用一次StpUtil中的方法，保证其可以尽早的初始化 StpLogic
        //StpUtil.getLoginType();
    }
    public static SafeKeeperConfiguration getConfig() {
        if (config == null) {
            synchronized (SafeKeeperConfiguration.class) {
                if (config == null) {
                    //加入没有配置初始化，就直接fatal中断
                    System.err.println("config 不能为空，没有默认初始化，程序直接停止...");
                    System.exit(1);
                }
            }
        }
        return config;
    }

    /**
     * 持久化 Bean
     */
    private static SafeKeeperTokenRealm safeKeeperTokenRealm;
    public static void setTokenDao(SafeKeeperTokenRealm safeKeeperTokenRealm) {
        SafeKeeperManager.safeKeeperTokenRealm = safeKeeperTokenRealm;
    }
    public static SafeKeeperTokenRealm getTokenDao() {
        if (safeKeeperTokenRealm == null) {
            synchronized (SafeKeeperManager.class) {
                if (safeKeeperTokenRealm == null) {
                    System.err.println("safeKeeperTokenDaoStorage 不能为空，没有默认初始化，程序直接停止...");
                    System.exit(1);
                }
            }
        }
        return safeKeeperTokenRealm;
    }
    /**
     * 权限认证 Bean
     */
    private static SafeKeeperAuthorizationCallBack authorizationCallBack;
    public static void setStpInterface(SafeKeeperAuthorizationCallBack authorizationCallBack) {
        SafeKeeperManager.authorizationCallBack = authorizationCallBack;
    }
    public static SafeKeeperAuthorizationCallBack getStpInterface() {
        if (authorizationCallBack == null) {
            synchronized (SafeKeeperManager.class) {
                if (authorizationCallBack == null) {
                    System.err.println("authorizationCallBack 不能为空，没有默认初始化，程序直接停止...");
                    System.exit(1);
                }
            }
        }
        return authorizationCallBack;
    }

    /**
     * 框架行为逻辑执行 Bean
     */
    private static SafeKeeperLogicAction safeKeeperLogicAction;
    public static void setSaTokenAction(SafeKeeperLogicAction safeKeeperLogicAction) {
        SafeKeeperManager.safeKeeperLogicAction = safeKeeperLogicAction;
    }
    public static SafeKeeperLogicAction getSaTokenAction() {
        if (safeKeeperLogicAction == null) {
            synchronized (SafeKeeperManager.class) {
                if (safeKeeperLogicAction == null) {
                    setSaTokenAction(new DefaultSafeKeeperLogicAction());
                }
            }
        }
        return safeKeeperLogicAction;
    }

    /**
     * 容器操作 Bean
     */
    private static SafeKeeperContext safeKeeperContext;
    public static void setSaTokenContext(SafeKeeperContext safeKeeperContext) {
        SafeKeeperManager.safeKeeperContext = safeKeeperContext;
    }
    public static SafeKeeperContext getSaTokenContext() {
        if (safeKeeperContext == null) {
            synchronized (SafeKeeperManager.class) {
                if (safeKeeperContext == null) {
                    System.err.println("safeKeeperContext 不能为空，没有默认初始化，程序直接停止...");
                    System.exit(1);
                }
            }
        }
        return safeKeeperContext;
    }

    /**
     * 侦听器 Bean
     */
    private static SafeKeeperTokenListener tokenListener;
    private static volatile boolean isTokenListenerInit=false;
    public static void setSaTokenListener(SafeKeeperTokenListener tokenListener) {
        if(isTokenListenerInit){
            return;
        }
        isTokenListenerInit=true;
        SafeKeeperManager.tokenListener = tokenListener;
    }
    public static SafeKeeperTokenListener getSaTokenListener() {
        if (tokenListener == null) {
            synchronized (SafeKeeperManager.class) {
                if (tokenListener == null) {
                    //初始化默认的监听器
                    setSaTokenListener(new DefaultSafeKeeperTokenListener());
                }
            }
        }
        return tokenListener;
    }

    /**
     * 状态消息配置 Bean
     */
    private static SafeKeeperCodeMsgConfiguration safeKeeperCodeMsgConfiguration;
    public static void setSafeKeeperCodeMsgConfiguration(SafeKeeperCodeMsgConfiguration safeKeeperCodeMsgConfiguration) {
        SafeKeeperManager.safeKeeperCodeMsgConfiguration = safeKeeperCodeMsgConfiguration;
    }
    public static SafeKeeperCodeMsgConfiguration getSafeKeeperCodeMsg() {
        if (safeKeeperCodeMsgConfiguration == null) {
            synchronized (SafeKeeperManager.class) {
                if (safeKeeperCodeMsgConfiguration == null) {
                    //初始化默认的监听器
                    setSafeKeeperCodeMsgConfiguration(new SafeKeeperCodeMsgConfiguration());
                }
            }
        }
        return safeKeeperCodeMsgConfiguration;
    }



    /**
     * StpLogic集合, 记录框架所有成功初始化的StpLogic
     */
    public static Map<String, SafeKeeperLogic> stpLogicMap = new ConcurrentHashMap<>();

    /**
     * 向集合中 put 一个 StpLogic
     * @param stpLogic StpLogic
     */
    public static void putStpLogic(SafeKeeperLogic stpLogic) {
        stpLogicMap.put(stpLogic.getLoginType(), stpLogic);
    }

    /**
     * 根据 LoginType 获取对应的StpLogic，如果不存在则抛出异常
     * @param loginType 对应的账号类型
     * @return 对应的StpLogic
     */
    public static SafeKeeperLogic getStpLogic(String loginType) {
        if(SafeKeeperUtils.isEmpty(loginType)){
            throw new SafeKeeperException("loginType must not null");
        }
        // 如果type为空则返回框架内置的
        SafeKeeperLogic stpLogic = stpLogicMap.get(loginType);
        if(stpLogic==null){
            return new SafeKeeperLogic(loginType);
        }
        return stpLogic;
    }

}
