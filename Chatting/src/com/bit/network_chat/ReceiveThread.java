package com.bit.network_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveThread extends Thread {
	BufferedReader br;
	Socket socket;
	public ReceiveThread(Socket socket,BufferedReader br) {
		this.br = br;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String message = br.readLine();
				if (message == null) {
					break;
				}
				if (" ".equals(message)) {
					break;
				}
				System.out.println(message);
			}
		}catch (SocketException se){
			System.exit(0);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
