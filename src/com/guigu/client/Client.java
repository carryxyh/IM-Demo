package com.guigu.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.guigu.msg.Msg_Login;
import com.guigu.msg.Msg_LoginResp;
import com.guigu.msg.Msg_Talk;
import com.guigu.utils.ClientMsgUtil;

public class Client {
	private InputStream is;
	private OutputStream os;
	private DataInputStream dins;
	private Socket socket;
	public Client(){
			init();
	}
	
	//用于属性初始化
	public void init(){
		try {
			socket = new Socket("localhost", 9999);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			dins = new DataInputStream(is);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 登陆的时候使用，内部采用了同步消息
	 * @param username 用户名
	 * @param password 密码
	 * @return true代表登陆成功，false代表登陆失败
	 */
	public boolean login(String username,String password){
		//将UI界面传递过来的username和password连到一起，然后发送过去。
//		String msg = username+","+password;
		//这种思路为同步消息，我必须等到从服务器接收到消息才可以继续向下运行
//		sendMsg(msg);
		msgLogin(username, password);
		//这里必须得接受到你的消息，我才可以继续向下运行
		byte result = readFromServer();
		if(result == 1){
			return true;
		}else{
			return false;
		}
	}
	

	
	
	/**
	 * @param msg  要发送的数据
	 */
	public void sendMsg(byte[]  bs){
		try {
			os.write(bs);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 接收服务器发送来是否登陆成功的数据
	 * @return 1成功  2失败
	 */
	public byte readFromServer(){
		Msg_LoginResp loginResp = null;
		try {
			int len = dins.readInt();
			byte[] data = new byte[len-4];
			dins.readFully(data);
			loginResp = (Msg_LoginResp) ClientMsgUtil.parseMsg(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loginResp.getState();
	}

	public Socket getSocket() {
		return socket;
	}

	/**
	 * 关闭输入流和输出流
	 */
	public void close(){
		try {
			socket.shutdownOutput();
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//-----------------------------以下写的代码是相应的发送消息的功能-----------------------------
	/**
	 * 将用户登录的用户名、密码发送出去
	 * @param username  长度不能大于多少
	 * @param password  长度不能大于多少
	 */
	private void msgLogin(String username,String password){
		Msg_Login msgLog = new Msg_Login();
		msgLog.setUsername(username);
		msgLog.setPassword(password);
		//4字节的int   totalLen    1字节的type   20字节的username  20字节的password
		msgLog.setTotalLen(4+1+20+20);
		byte[] bs = ClientMsgUtil.creatMsg(msgLog);
		sendMsg(bs);
	}
	
	public void sendTalk(String content){
		Msg_Talk msgTalk = new Msg_Talk();
		msgTalk.setTotalLen(4+1+120);
		msgTalk.setContent(content);
		byte[] bs = ClientMsgUtil.creatMsg(msgTalk);
		sendMsg(bs);
	}
}
