package com.bit.network_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ChatServerThread extends Thread {

	private List<PrintWriter> listPrintWriter;

	private Socket socket;
	private String name;

	public ChatServerThread(Socket socket, List<PrintWriter> listPrintWriter) {
		this.socket = socket;
		this.listPrintWriter = listPrintWriter;
	}

	@Override
	public void run() {
		try {
			// 1. remote inet
			InetSocketAddress remoteAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			ServerChat.consolelog("Connected by Clinet ["
					+ remoteAddress.getAddress().getHostAddress() + " : "
					+ remoteAddress.getPort() + "]");

			// 2. io
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(),"UTF-8"), true);

			// 3. while()
			String data = null;
			while (true) {
				data = br.readLine();
				if (data == null) {
					doQuit(pw);
					break;
				}
				if ("".equals(data)) {
					break;
				}
				System.out.println( data );
				String[] tokens = data.split(":");
				if ("JOIN".equalsIgnoreCase(tokens[0])) {
					doJoin(tokens[1], pw);
				} else if ("MESSAGE".equalsIgnoreCase(tokens[0])) {
					 doMesage(tokens[1]);
				} else if ("QUIT".equalsIgnoreCase(tokens[0])) {
					doQuit(pw);
				}
			}
		} catch (UnsupportedEncodingException e) {
			ServerChat.consoleerror("error" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ServerChat.consoleerror("error" + e);
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				ServerChat.consoleerror("error" + e);
			}
		}
	}

	private void doQuit( PrintWriter pw) {
		String message = name + "¥‘¿Ã ≈¿Â«ﬂΩ¿¥œ¥Ÿ.";
		deletePrintWriter(pw);
		broadcastingMessage(message);
	}

	private void doMesage(String data) {
		//1.message show to all
		String message = name+">>"+data;
		broadcastingMessage(message);
		
	}

	private void doJoin(String name, PrintWriter pw) {
		// 1. name setting.
		this.name = name;
		// 2. broadcasting
		String message = name + "¥‘¿Ã ¿‘¿Â«ﬂΩ¿¥œ¥Ÿ.";
		broadcastingMessage(message);

		// 3. add pw
		addPrintWriter(pw);
		// 4. ack
		pw.println("JOIN:OK");
	}

	private void addPrintWriter(PrintWriter pw) {
		synchronized (listPrintWriter) {
			listPrintWriter.add(pw);
		}
	}

	private void deletePrintWriter(PrintWriter pw) {
		synchronized (listPrintWriter) {
			listPrintWriter.remove(pw);
		}
	}

	private void broadcastingMessage(String message) {
		int count=0;
		synchronized (listPrintWriter) {
			for (PrintWriter pw : listPrintWriter) {
				pw.println(message);
				count++;
			}
		}
		System.out.println("[server] send message"+count);
	}

}
