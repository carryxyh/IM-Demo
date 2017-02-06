package com.guigu.ui;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.guigu.client.Client;

/**
 * 让这个类实现Observer接口，实现update方法，就可以更方便的来接受ClientThread的数据
 * @author DELL
 *
 */
public class Chat extends JFrame implements Observer {

	private JPanel contentPane;
	//这个界面同样需要Client对象，我们可以使用单例模式，也可以通过其他界面传过来，我这里从Login界面传递过来的
	private Client client;
	
	public Chat(Client client) throws HeadlessException {
		this();
		this.client = client;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JTextArea textArea = new JTextArea();
	private JTextField textField;
	/**
	 * Create the frame.
	 */
	public Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		textArea.setBounds(10, 0, 414, 225);
		//设置textArea为不能修改
		textArea.setEditable(false);
		contentPane.add(textArea);
		
		JButton button = new JButton("\u53D1\u9001");
		button.setBounds(324, 228, 100, 23);
		contentPane.add(button);
		
		
		textField = new JTextField();
		textField.setBounds(10, 228, 300, 23);
		contentPane.add(textField);
		textField.setColumns(10);
		
		//发送按钮的点击事件，主要是把内容发送出去，然后让TextField清空
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//对textField中的数据进行了一下简单的修改
				String msg = Login.username+":"+textField.getText();
				//使用client来发送数据
				client.sendTalk(msg);
				//清空textField中的数据
				textField.setText("");
			}
		});
		
		//添加窗口关闭事件
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				client.sendMsg("quit");
				//关闭流
				client.close();
				//程序退出
				System.exit(0);
			}
		});
	}

	
	public void update(Observable o, Object arg) {
		//添加数据到textArea
		textArea.append((String)arg);
	}
}
