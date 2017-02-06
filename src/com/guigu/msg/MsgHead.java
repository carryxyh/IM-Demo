package com.guigu.msg;

//消息头
public class MsgHead {
	
	private int totalLen;	// 消息总长度
	private byte type;		// 消息类型
	
	
	public int getTotalLen() {
		return totalLen;
	}
	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	
}
