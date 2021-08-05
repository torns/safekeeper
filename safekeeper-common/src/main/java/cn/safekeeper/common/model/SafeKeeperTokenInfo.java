package cn.safekeeper.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SafeKeeper实体
 * @author skylark
 *
 */
@Setter
@Getter
@ToString
public class SafeKeeperTokenInfo {

	/** token名称 */
	public String tokenName;

	/** token值 */
	public String tokenValue;

	/** 此token是否已经登录 */
	public Boolean isLogin;

	/** 此token对应的LoginId，未登录时为null */
	public Object loginId;

	/** 账号类型 */
	public String loginType;

	/** token剩余有效期 (单位: 秒) */
	public long tokenTimeout;

	/** User-Session剩余有效时间 (单位: 秒) */
	public long sessionTimeout;

	/** Token-Session剩余有效时间 (单位: 秒) */
	public long tokenSessionTimeout;

	/** token剩余无操作有效时间 (单位: 秒) */
	public long tokenActivityTimeout;

	/** 登录设备标识 */
	public String loginDevice;

	/** 自定义数据 */
	public String tag;

}
