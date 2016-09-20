package net.thread;

//: TIEJ:X1:JabberServer.java
//Very simple server that just
//echoes whatever the client sends.
//{RunByHand}
import java.io.*;
import java.net.*;

public class JabberServer {
	// Choose a port outside of the range 1-1024:
	public static final int PORT = 8080;

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Started: " + s);
		try {
			// Blocks until a connection occurs:
			Socket socket = s.accept();
			try {
				System.out.println("Connection accepted: " + socket);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Output is automatically flushed
				// by PrintWriter:
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				while (true) {
					String str = in.readLine();
					System.out.println("Echoing: " + str);
					out.write(str, 0, str.length());
					out.newLine();
					out.flush();
					if (str.equals("END"))
						break;
				}
				// Always close the two sockets...
			} finally {
				System.out.println("closing...");
				socket.close();
			}
		} finally {
			s.close();
		}
	}
} // /:~
