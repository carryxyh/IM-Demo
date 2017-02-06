package com.guigu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServer {
	public static List<ServerThread> list = new CopyOnWriteArrayList<ServerThread>();
	
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(9999);
			System.out.println("服务器启动成功，正在监听9999端口......");
			while(true){
				Socket client = server.accept();
				ServerThread st = new ServerThread(client);
				list.add(st);
				st.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
