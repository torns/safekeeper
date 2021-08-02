package com.changhong.framework.core.assist;

import com.changhong.framework.core.manager.SafeKeeperManager;
import com.changhong.framework.core.session.Session;

/**
 * 本工程session辅助工具
 * @author skylark
 */
public class SessionNativeUtil {
    /**
     * 添加上指定前缀，防止恶意伪造Session
     */
    public static String sessionKey = "custom";

    /**
     * 自定义Session的Id
     * @param sessionId 会话id
     * @return sessionId
     */
    public static String splicingSessionKey(String sessionId) {
        return SafeKeeperManager.getConfig().getSafeKeeperName() + ":" + sessionKey + ":session:" + sessionId;
    }

    /**
     * 指定key的Session是否存在
     * @param sessionId Session的id
     * @return 是否存在
     */
    public static boolean isExists(String sessionId) {
        return SafeKeeperManager.getTokenDao().getSession(splicingSessionKey(sessionId)) != null;
    }

    /**
     * 获取指定key的Session
     * @param sessionId key
     * @param isCreate  如果此Session尚未在DB创建，是否新建并返回
     * @return Session
     */
    public static Session getSessionById(String sessionId, boolean isCreate) {
        Session session = SafeKeeperManager.getTokenDao().getSession(splicingSessionKey(sessionId));
        if (session == null && isCreate) {
            session = SafeKeeperManager.getSaTokenAction().createSession(splicingSessionKey(sessionId));
            SafeKeeperManager.getTokenDao().setSession(session, SafeKeeperManager.getConfig().getTimeout());
        }
        return session;
    }

    /**
     * 获取指定key的Session, 如果此Session尚未在DB创建，则新建并返回
     * @param sessionId key
     * @return session对象
     */
    public static Session getSessionById(String sessionId) {
        return getSessionById(sessionId, true);
    }

    /**
     * 删除指定key的Session
     * @param sessionId 指定key
     */
    public static void deleteSessionById(String sessionId) {
        SafeKeeperManager.getTokenDao().deleteSession(splicingSessionKey(sessionId));
    }
}
