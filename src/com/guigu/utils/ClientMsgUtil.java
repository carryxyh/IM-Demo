package com.guigu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.guigu.msg.IMsgType;
import com.guigu.msg.MsgHead;
import com.guigu.msg.Msg_Login;
import com.guigu.msg.Msg_LoginResp;
import com.guigu.msg.Msg_Talk;

public class ClientMsgUtil {
	/**
	 * 打包数据
	 * 
	 * @param head
	 *            要打包的数据
	 * @return 打包后的数据
	 */
	public static byte[] creatMsg(MsgHead head) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(baos);
		int totalLength = head.getTotalLen(); // 消息的总长度是预先定义好的
		byte type = head.getType();
		try {
			dous.writeInt(totalLength);
			dous.writeByte(type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (type == IMsgType.MSG_LOGIN) {
			Msg_Login msgLogin = (Msg_Login) head;
			writeString(dous, msgLogin.getUsername(), 20);
			writeString(dous, msgLogin.getPassword(), 20);
		}else if(type == IMsgType.MSG_TALK){
			Msg_Talk msgTalk = (Msg_Talk) head;
			writeString(dous, msgTalk.getContent(),120);
		}
		return baos.toByteArray();
	}
	
	public static void writeString(DataOutputStream dous, String s,int size) {
		byte[] bs = s.getBytes();
		int len = bs.length;
		if(len>size){
			throw new RuntimeException("字符串处理错误：长度大于指定字节数");
		}
		try {
			dous.write(bs);
			while(len<size){
				dous.writeByte((byte)' ');
				len++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param data 此时读取到的数据是除了totalLen之外的数据了
	 * @return Msg对象
	 */
	public static MsgHead parseMsg(byte[] data){
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
			try {
				byte type = dis.readByte();

				MsgHead head = new MsgHead();
				head.setTotalLen(data.length+4);
				head.setType(type);
				
				if (type == IMsgType.MSG_LOGINRESP) {
					Msg_LoginResp msgLoginResp = new Msg_LoginResp();
					copyMsgHead(head, msgLoginResp);
					msgLoginResp.setState(dis.readByte());
					return msgLoginResp;
				}else if(type == IMsgType.MSG_TALK){
					Msg_Talk msgTalk = new Msg_Talk();
					copyMsgHead(head, msgTalk);
					byte[] bs = new byte[120];
					dis.read(bs);
					msgTalk.setContent(new String(bs));
					return msgTalk;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	private  static void copyMsgHead(MsgHead src,MsgHead dest){
		dest.setTotalLen(src.getTotalLen());
		dest.setType(src.getType());
	} 
}
