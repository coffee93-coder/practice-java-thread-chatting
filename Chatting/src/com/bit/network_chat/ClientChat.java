package com.bit.network_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientChat {

	private static final int SERVER_PORT = 9999;
	private static final String SERVER_IP = "192.168.1.9";

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;
		try {
			// 1, keyboard
			scanner = new Scanner(System.in);
			// 2. socket 생성
			socket = new Socket();
			// 3. connect
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 4. reader/writer
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(),"UTF-8"), true);
			
			// 5. join protocol
			System.out.print("닉네임 >>");
			String nickname = scanner.nextLine();
			pw.println("JOIN:" + nickname);
			pw.flush();
			// 6. chatclientRecieveThread 시작
			Thread thread = new ReceiveThread(socket,br);
			thread.start();

			while (true) {
				System.out.print(">>");
				String data = scanner.nextLine();
				if( data != null && data.equals("") == false ){
					if ("QUIT".equalsIgnoreCase(data)) {
						
						break;
					} else {
						pw.println("MESSAGE:"+data);
					} 
				}
			}
		} catch(SocketException se){
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scanner.close();
			if (socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void consolelog(String message) {
		System.out.println("[chat server]" + message);
	}

	public static void consoleerror(String message) {
		System.out.println("[char server error]" + message);
	}
}
