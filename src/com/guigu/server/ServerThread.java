package com.guigu.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.guigu.msg.IMsgType;
import com.guigu.msg.MsgHead;
import com.guigu.msg.Msg_Login;
import com.guigu.msg.Msg_LoginResp;
import com.guigu.msg.Msg_Talk;
import com.guigu.utils.ServerMsgUtil;

public class ServerThread extends Thread {

	private Socket client;
	private InputStream is;
	private DataInputStream dins;
	private OutputStream os;

	public ServerThread(Socket client) {
		this.client = client;
		init();
	}

	// 不要把抛异常的话写在构造方法中，可能会造成this逃逸
	private void init() {
		try {
			is = client.getInputStream();
			os = client.getOutputStream();
			dins = new DataInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			MsgHead msgHead = null;
			try {
				msgHead = readMsgFromClient();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (msgHead.getType() == IMsgType.MSG_LOGIN) {
				Msg_Login msgLogin = (Msg_Login) msgHead;
				sendLoginResp(msgLogin);
			}else if(msgHead.getType() == IMsgType.MSG_TALK){
				System.out.println("收到客户端的消息");
				Msg_Talk msgTalk = (Msg_Talk) msgHead;
				sendTalk(msgTalk);
			}

		}
	}

	/*
	 * //读服务器发送过来的消息,进行消息的广播 String msg = null; try { while ((msg =
	 * br.readLine()) != null) { if ("quit".equals(msg)) { close(); removeMe();
	 * } else { broadMsg(msg); } } } catch (IOException e) {
	 * e.printStackTrace(); }
	 */

	
	/**
	 * 处理用户发送过来的消息
	 * 
	 * @return
	 */
	private MsgHead readMsgFromClient() throws IOException {
		int totalLen = dins.readInt();
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data);
		MsgHead msg = ServerMsgUtil.parseMsg(data);
		return msg;
	}

	/**
	 * 调用数据库查找的方法去找数据库中的数据，处理用户登陆
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 1成功，2失败
	 */
	private int login(String username, String password) {
		if (("zhangsan".equals(username)) && "123".equals(password)
				|| ("lisi".equals(username) && "123".equals(password))) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 关闭流
	 */
	private void close() {
		try {
			client.shutdownInput();
			client.shutdownOutput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除本ServerThread
	 */
	private void removeMe() {
		for (ServerThread st : MainServer.list) {
			if (this == st) {
				MainServer.list.remove(st);
			}
		}
	}


	/**
	 * 发送数据
	 * @param msg          数据
	 * @throws IOException
	 */
	private void sendMsg(byte[] bs) {
		try {
			os.write(bs);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//-------------------------------以下方法为服务器发送到客户端的方法-------------------------------------
	/**
	 * 获取用户登陆的信息，并验证，最后发给客户端
	 * @param result
	 */
	private void sendLoginResp(Msg_Login msgLogin){
		int result = login(msgLogin.getUsername(),msgLogin.getPassword());
		//发送回去
		Msg_LoginResp msgLoginResp = new Msg_LoginResp();
		msgLoginResp.setState((byte)result);
		msgLoginResp.setTotalLen(4+1+1);
		try {
			sendMsg(ServerMsgUtil.creatMsg(msgLoginResp));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 *  将收到的消息广播
	 * @param msgTalk
	 */
	private void sendTalk(Msg_Talk msgTalk) {
		for (ServerThread st : MainServer.list) {
			try {
				st.sendMsg(ServerMsgUtil.creatMsg(msgTalk));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
