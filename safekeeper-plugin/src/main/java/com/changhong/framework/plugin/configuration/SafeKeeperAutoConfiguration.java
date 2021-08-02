package com.changhong.framework.plugin.configuration;

import com.changhong.framework.common.configuration.SafeKeeperCodeMsgConfiguration;
import com.changhong.framework.common.configuration.SafeKeeperConfiguration;
import com.changhong.framework.common.exception.SafeKeeperException;
import com.changhong.framework.common.model.SafeKeeperAuthorizationCallBack;
import com.changhong.framework.common.model.SafeKeeperContext;
import com.changhong.framework.common.utils.SafeKeeperUtils;
import com.changhong.framework.core.SafeKeeper;
import com.changhong.framework.core.listener.SafeKeeperTokenListener;
import com.changhong.framework.core.manager.SafeKeeperManager;
import com.changhong.framework.core.manager.SafeKeeperTokenDaoStorage;
import com.changhong.framework.plugin.aop.SafeKeeperCheckAspect;
import com.changhong.framework.plugin.context.SpringSafeKeeperContext;
import com.changhong.framework.plugin.filter.SafeKeeperFilter;
import com.changhong.framework.plugin.redis.config.SafeRedisProperties;
import com.changhong.framework.plugin.redis.dao.SafeKeeperTokenDaoStorageRedis;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * SafeKeeper配置启动类
 * @author skylark
 */
@Configuration
public class SafeKeeperAutoConfiguration {

    /**
     * 切面初始化
     */
    @Bean
    public SafeKeeperCheckAspect getSafeKeeperCheckAspect(){
        return new SafeKeeperCheckAspect();
    }

    /**
     * context初始化
     */
    @Bean
    public SafeKeeperContext getSafeKeeperContext(){
        return new SpringSafeKeeperContext();
    }

    @Bean
    @ConfigurationProperties(prefix ="spring.safekeeper")
    public SafeKeeperConfiguration getSafeKeeperConfiguration(){
        return new SafeKeeperConfiguration();
    }


    /**
     * SafeKeeperManager 注入 SafeKeeperConfiguration
     * 配置文件 Bean
     */
    @Autowired
    public void setConfig(SafeKeeperConfiguration config){
        SafeKeeperManager.setConfig(config);
    }

    @Bean
    public SafeRedisProperties getSafeRedisProperties(){
        return new SafeRedisProperties();
    }

    /**
     * 初始化持久化SafeKeeperTokenDaoStorage接口实现
     * @param cfg
     * @return 实现结果
     */
    @Bean
    public SafeKeeperTokenDaoStorage init(SafeRedisProperties cfg){
        // 1. Redis配置
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(cfg.getHost());
        redisConfig.setPort(cfg.getPort());
        redisConfig.setDatabase(cfg.getDatabase());
        redisConfig.setPassword(RedisPassword.of(cfg.getPassword()));

        // 2. 连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // pool配置
        RedisProperties.Lettuce lettuce = cfg.getLettuce();
        if(lettuce.getPool() != null) {
            RedisProperties.Pool pool = cfg.getLettuce().getPool();
            // 连接池最大连接数
            poolConfig.setMaxTotal(pool.getMaxActive());
            // 连接池中的最大空闲连接
            poolConfig.setMaxIdle(pool.getMaxIdle());
            // 连接池中的最小空闲连接
            poolConfig.setMinIdle(pool.getMinIdle());
            // 连接池最大阻塞等待时间（使用负值表示没有限制）
            poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        // timeout
        if(cfg.getTimeout() != null) {
            builder.commandTimeout(cfg.getTimeout());
        }
        // shutdownTimeout
        if(lettuce.getShutdownTimeout() != null) {
            builder.shutdownTimeout(lettuce.getShutdownTimeout());
        }
        // 创建Factory对象
        LettuceClientConfiguration clientConfig = builder.poolConfig(poolConfig).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
        factory.afterPropertiesSet();
        return new SafeKeeperTokenDaoStorageRedis(factory);
    }

    /**
     * SafeKeeperManager SafeKeeperTokenDaoStorage
     * 持久化Dao Bean
     */
    @Autowired
    public void setSaTokenDao(SafeKeeperTokenDaoStorage safeKeeperTokenDaoStorage) {
        SafeKeeperManager.setTokenDao(safeKeeperTokenDaoStorage);
    }

    /**
     *
     * SafeKeeperManager SafeKeeperAuthorizationCallBack
     * 权限认证 Bean
     */
    @Autowired(required = false)
    public void setStpInterface(SafeKeeperAuthorizationCallBack authorizationCallBack) {
        SafeKeeperManager.setStpInterface(authorizationCallBack);
    }

    /**
     * 容器操作 Bean
     */
    @Autowired
    public void setSaTokenContext(SafeKeeperContext safeKeeperContext) {
        SafeKeeperManager.setSaTokenContext(safeKeeperContext);
    }

    /**
     * SafeKeeperManager SafeKeeperTokenListener
     * 侦听器 Bean
     */
    @Autowired(required = false)
    public void setSaTokenListener(SafeKeeperTokenListener tokenListener) {
        SafeKeeperManager.setSaTokenListener(tokenListener);
    }

    /**
     * SafeKeeperManager SafeKeeperCodeMsgConfiguration
     * 状态消息配置 Bean
     */
    @Autowired(required = false)
    public void setSafeKeeperCodeMsgConfiguration(SafeKeeperCodeMsgConfiguration safeKeeperCodeMsgConfiguration) {
        SafeKeeperManager.setSafeKeeperCodeMsgConfiguration(safeKeeperCodeMsgConfiguration);
    }

    @Bean
    public SafeKeeperFilter getSaServletFilter(SafeKeeperConfiguration config) {
        return new SafeKeeperFilter()
                // 指定 拦截路由 与 放行路由
                .addInclude("/**").addExclude("/favicon.ico").setIncludeList(config.getIncludeList())
                .addExclude("/login").setExcludeList(config.getExcludeList())
                // 认证函数: 每次请求执行
                .setAuth(r -> {
                    HttpServletRequest request=(HttpServletRequest)r;
                    //获取登录的方式
                    String loginType = request.getHeader("loginType");
                    if(SafeKeeperUtils.isEmpty(loginType)){
                        throw new SafeKeeperException("登录的loginType不能为空，请在header中加入此登录类型进行判断");
                    }
                    SafeKeeper.safeLogic(loginType).checkLogin();
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(Throwable::getMessage)
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SafeKeeperManager.getSaTokenContext().getResponse()
                            // 服务器名称
                            .setServer("safekeeper-server")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff")
                    ;
                });
    }
}
