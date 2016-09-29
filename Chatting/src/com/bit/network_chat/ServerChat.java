package com.bit.network_chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat {
	private static final int PORT = 9999;
	private static List<PrintWriter> listPrintWriter;

	public static void main(String[] args) {
		// PrintWriter List »ý¼º
		listPrintWriter = new ArrayList<PrintWriter>();

		// 1. serversocket
		ServerSocket serversocket = null;
		try {
			serversocket = new ServerSocket();
			String localhost = InetAddress.getLocalHost().getHostAddress();
			serversocket.bind(new InetSocketAddress(localhost, PORT));
			consolelog("binding " + localhost + " : " + PORT);

			while (true) {
				Socket socket = serversocket.accept();

				Thread thread = new ChatServerThread(socket, listPrintWriter);
				thread.start();
			}
			// 2. InetAdress
		} catch (IOException e) {
			consoleerror("error :" + e);
		} finally {
			try {
				if (serversocket != null && serversocket.isClosed() == false) {
					serversocket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
