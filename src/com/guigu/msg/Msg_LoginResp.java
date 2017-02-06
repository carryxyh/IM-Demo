package com.guigu.msg;

/**
 * 登陆响应    消息协议类
 * 包含 响应状态字段   如果为1表示成功    2表示失败
 */
public class Msg_LoginResp extends MsgHead{
	public Msg_LoginResp(){
		this.setType(IMsgType.MSG_LOGINRESP);
	}
	private byte state;
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	
}
