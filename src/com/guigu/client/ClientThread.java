package com.guigu.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Observable;

import com.guigu.msg.IMsgType;
import com.guigu.msg.MsgHead;
import com.guigu.msg.Msg_Talk;
import com.guigu.utils.ClientMsgUtil;

/**
 * 这个类继承了Observable类，然后实现了Runnable接口。 在run方法中不停的接受服务器端传来的消息，然后发送到图形化界面去。
 * 使用Observable的update方法做到
 * 
 * @author DELL
 *
 */
public class ClientThread extends Observable implements Runnable {

	private Socket socket;
	private InputStream is;
	private DataInputStream dins;

	public ClientThread(Socket socket) {
		this.socket = socket;
		init();
	}

	/**
	 * 构造方法初始化
	 */
	private void init() {
		try {
			is = socket.getInputStream();
			dins = new DataInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			MsgHead msgHead = null;
			try {
				msgHead = readMsgFromServer();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (msgHead.getType() == IMsgType.MSG_TALK) {
				Msg_Talk msgTalk = (Msg_Talk) msgHead;
				// 在通知观察者之前必须使用setChanged()方式
				setChanged();
				// 通知观察者
				notifyObservers(msgTalk.getContent().trim()+"\r\n");
			}
		}
	}

	private MsgHead readMsgFromServer()  throws IOException{
		int totalLen = dins.readInt();
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data);
		MsgHead msg = ClientMsgUtil.parseMsg(data);
		return msg;
	}

}
