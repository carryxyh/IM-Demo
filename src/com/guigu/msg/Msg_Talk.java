package com.guigu.msg;

/**
 * 聊天的消息体
 */
public class Msg_Talk extends MsgHead {
	
	public Msg_Talk(){
		this.setType(IMsgType.MSG_TALK);
	}
	
	//指定大小为120字节
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
