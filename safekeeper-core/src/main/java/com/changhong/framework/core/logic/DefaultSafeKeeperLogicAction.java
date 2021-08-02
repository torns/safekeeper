package com.changhong.framework.core.logic;

import com.changhong.framework.common.annotations.SafeKeeperHasLogin;
import com.changhong.framework.common.annotations.SafeKeeperHasPermission;
import com.changhong.framework.common.annotations.SafeKeeperHasRole;
import com.changhong.framework.common.utils.SafeKeeperUtils;
import com.changhong.framework.core.manager.SafeKeeperManager;
import com.changhong.framework.core.session.Session;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * 逻辑执行默认实现
 * @author skylark
 */
public class DefaultSafeKeeperLogicAction implements SafeKeeperLogicAction{

    /**
     * 创建一个Token
     */
    @Override
    public String createToken(Object loginId, String loginType) {
        //TODO 根据配置的tokenStyle生成不同风格的token
        String tokenStyle = SafeKeeperManager.getConfig().getTokenType();
        // 默认，还是uuid
        return UUID.randomUUID().toString();
    }

    /**
     * 创建一个Session
     */
    @Override
    public Session createSession(String sessionId) {
        return new Session(sessionId);
    }
    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     */
    @Override
    public boolean hasElement(List<String> list, String element) {
        // 空集合直接返回false
        if(list == null || list.size() == 0) {
            return false;
        }
        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (list.contains(element)) {
            return true;
        }
        // 开始模糊匹配
        for (String patt : list) {
            if(SafeKeeperUtils.vagueMatch(patt, element)) {
                return true;
            }
        }
        // 走出for循环说明没有一个元素可以匹配成功
        return false;
    }
    /**
     * 对一个Method对象进行注解检查（注解鉴权内部实现）
     */
    @Override
    public void checkMethodAnnotation(Method method) {
        // 先校验 Method 所属 Class 上的注解
        validateAnnotation(method.getDeclaringClass());
        // 再校验 Method 上的注解
        validateAnnotation(method);
    }

    /**
     * 从指定元素校验注解
     * @param target see note
     */
    protected void validateAnnotation(AnnotatedElement target) {
        // 校验 @SaCheckLogin 注解
        if(target.isAnnotationPresent(SafeKeeperHasLogin.class)) {
            SafeKeeperHasLogin at = target.getAnnotation(SafeKeeperHasLogin.class);
            SafeKeeperManager.getStpLogic(at.type()).checkByAnnotation(at);
        }

        // 校验 @SaCheckRole 注解
        if(target.isAnnotationPresent(SafeKeeperHasRole.class)) {
            SafeKeeperHasRole at = target.getAnnotation(SafeKeeperHasRole.class);
            SafeKeeperManager.getStpLogic(at.type()).checkByAnnotation(at);
        }

        // 校验 @SaCheckPermission 注解
        if(target.isAnnotationPresent(SafeKeeperHasPermission.class)) {
            SafeKeeperHasPermission at = target.getAnnotation(SafeKeeperHasPermission.class);
            SafeKeeperManager.getStpLogic(at.type()).checkByAnnotation(at);
        }

    }
}
