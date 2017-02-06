package com.guigu.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.guigu.client.Client;
import com.guigu.client.ClientThread;

/***
 * 登陆界面
 * 不要在这个里面直接来写一些客户端的代码，要让UI界面和我们的客户端等代码分离
 * @author DELL
 */
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	//创建出Client对象
	private Client client = new Client();
	//创建了一个username的静态变量
	public static String username;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(77, 55, 54, 15);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setBounds(77, 128, 54, 15);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(187, 55, 147, 18);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(187, 125, 147, 18);
		contentPane.add(passwordField);
		
		JButton button = new JButton("\u767B\u9646");
		button.setBounds(142, 204, 93, 23);
		contentPane.add(button); 
		//加上监听事件
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//获得到username和password
				String username = textField.getText();
				String password = new String(passwordField.getPassword());
				//这里去调用登陆方法
				boolean result = client.login(username, password);
				if(result){
					//通过构造方法将client传入到不同的界面中去
					Chat frame = new Chat(client);
					ClientThread ct = new ClientThread(client.getSocket());
					//将frame添加到观察者的队列中去
					ct.addObserver(frame);
					//让异步接受消息的线程启动
					new Thread(ct).start();
					frame.setVisible(true);
					setVisible(false);
					//这里赋值一下当前名字
					Login.username = username;
					//使当前标题为username
					frame.setTitle(username);
				}else{
					JOptionPane.showMessageDialog(null, "登录失败，请重新登录", "登陆失败", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
