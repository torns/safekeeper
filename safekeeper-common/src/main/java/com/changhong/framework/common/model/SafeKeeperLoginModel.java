package com.changhong.framework.common.model;
import com.changhong.framework.common.configuration.SafeKeeperConfiguration;
import com.changhong.framework.common.constant.SafeKeeperConstant;

/**
 * 调用登录时的 [配置参数 Model ]
 * @author skylark
 *
 */
public class SafeKeeperLoginModel {

	/**
	 * 永久不过期
	 */
	private static final int NEVER_EXPIRE=-1;
	
	/**
	 * 此次登录的客户端设备标识 
	 */
	public String device;
	
	/**
	 * 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
	 */
	public Boolean isLastingCookie;

	/**
	 * 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的timeout值）
	 */
	public Long timeout;

	
	/**
	 * @return 参考 {@link #device}
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device 参考 {@link #device}
	 * @return 对象自身
	 */
	public SafeKeeperLoginModel setDevice(String device) {
		this.device = device;
		return this;
	}

	/**
	 * @return 参考 {@link #isLastingCookie}
	 */
	public Boolean getIsLastingCookie() {
		return isLastingCookie;
	}

	/**
	 * @param isLastingCookie 参考 {@link #isLastingCookie}
	 * @return 对象自身
	 */
	public SafeKeeperLoginModel setIsLastingCookie(Boolean isLastingCookie) {
		this.isLastingCookie = isLastingCookie;
		return this;
	}

	/**
	 * @return 参考 {@link #timeout}
	 */
	public Long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout 参考 {@link #timeout}
	 * @return 对象自身
	 */
	public SafeKeeperLoginModel setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}


	/**
	 * @return Cookie时长
	 */
	public int getCookieTimeout() {
		if(isLastingCookie == false) {
			return -1;
		}
		if(timeout == SafeKeeperLoginModel.NEVER_EXPIRE) {
			return Integer.MAX_VALUE;
		}
		return (int)(long)timeout;
	}
	
	/**
	 * 构建对象，初始化默认值 
	 * @param config 配置对象 
	 * @return 对象自身
	 */
	public SafeKeeperLoginModel build(SafeKeeperConfiguration config) {
		if(device == null) {
			device = SafeKeeperConstant.DEFAULT_LOGIN_DEVICE;
		}
		if(isLastingCookie == null) {
			isLastingCookie = true;
		}
		if(timeout == null) {
			timeout = config.getTimeout();
		}
		return this;
	}
	
	/**
	 * 静态方法获取一个 SaLoginModel 对象
	 * @return SaLoginModel 对象 
	 */
	public static SafeKeeperLoginModel create() {
		return new SafeKeeperLoginModel();
	}

	/**
	 * toString
	 */
	@Override
	public String toString() {
		return "SaLoginModel [device=" + device + ", isLastingCookie=" + isLastingCookie + ", timeout=" + timeout + "]";
	}

}
