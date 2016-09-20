package net.thread;

//: TIEJ:X1:MultiJabberServer2.java
//Same semantics as MultiJabberServer1 using thread pooling.
//{RunByHand}
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

import net.thread.pool.ThreadPool;

public class MultiJabberServer2 {
	public static final int PORT = 8080;
	private static String encoding = System.getProperty("file.encoding");
	public static final Charset CS = Charset.forName(encoding);
	// Make thread pool with 20 Worker threads.
	private static ThreadPool pool = new ThreadPool(20);

	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		Selector sel = Selector.open();
		try {
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress(PORT));
			SelectionKey key = ssc.register(sel, SelectionKey.OP_ACCEPT);
			System.out.println("Server on port: " + PORT);
			while (true) {
				sel.select(1000);
				Iterator it = sel.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey skey = (SelectionKey) it.next();
					it.remove();
					if (skey.isAcceptable()) {
						SocketChannel channel = ssc.accept();
						System.out.println("Accepted connection from:" + channel.socket());
						channel.configureBlocking(false);
						// Decouple event and associated action
						pool.addTask(new ServeOneJabber(channel));
					}
				}
			}
		} finally {
			ssc.close();
			sel.close();
		}
	}
} // /:~

