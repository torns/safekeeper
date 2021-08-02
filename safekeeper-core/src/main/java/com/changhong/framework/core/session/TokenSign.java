package com.changhong.framework.core.session;

import java.io.Serializable;

/**
 * Token签名
 * 挂在Session上的token签名
 * @author skylark
 *
 */
public class TokenSign implements Serializable {
	/**
	 * token值
	 */
	private String value;
	/**
	 * 所在设备标识
	 */
	private String device;
	/** 构建一个 */
	public TokenSign() {
	}
	/**
	 * 构建一个
	 * @param value  token值
	 * @param device 所在设备标识
	 */
	public TokenSign(String value, String device) {
		this.value = value;
		this.device = device;
	}

	/**
	 * @return token value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @return token登录设备 
	 */
	public String getDevice() {
		return device;
	}

	@Override
	public String toString() {
		return "TokenSign [value=" + value + ", device=" + device + "]";
	}

}
