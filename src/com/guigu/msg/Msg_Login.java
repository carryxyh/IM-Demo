package com.guigu.msg;

/**
 * 登陆    消息协议类
 * 包含    用户名、密码两个字段
 * 使用定长字符串的方式进行
 */
public class Msg_Login  extends MsgHead{
	
	
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Msg_Login(){
		this.setType(IMsgType.MSG_LOGIN);
	}
	
}
