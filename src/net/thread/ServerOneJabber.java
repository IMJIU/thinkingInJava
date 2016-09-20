package net.thread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

class ServeOneJabber implements Runnable {

	private SocketChannel channel;

	private Selector sel;

	public ServeOneJabber(SocketChannel ch) throws IOException {
		channel = ch;
		sel = Selector.open();
	}

	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		boolean read = false, done = false;
		String response = null;
		int sleep = 0;
		try {
			channel.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			while (!done) {
				sel.select(1000);
				Iterator it = sel.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					it.remove();
					System.out.println("coming..");
					if(sleep++>7){
						try {
							sleep=0;
							System.out.println("【sleep】..");
	                        Thread.currentThread().wait(100);
                        } catch (InterruptedException e) {
	                        e.printStackTrace();
                        }
					}
					
					if (key.isReadable() && !read) {
						int len = 0;
						StringBuilder s = new StringBuilder();
						while ((len = channel.read(buffer)) > 0) {
							s.append(new String(buffer.array()));
							// System.out.println(new String(buffer.array()));
							buffer.clear();
						}
						if (s.length() > 0) {
//							System.out.println(s.toString());
							read = true;
						}
						// CharBuffer cb =
						// MultiJabberServer2.CS.decode((ByteBuffer)
						// buffer.flip());
						// response = cb.toString();
						response = s.toString();
					}
					if (key.isWritable() && read) {
						System.out.print("服务端收到 : " + response);
						int len = channel.write( ByteBuffer.wrap(response.getBytes()));
						System.out.println("\n服务端写入多少字节:" + len);
						if (response.indexOf("END") != -1) {
							done = true;
						}
						buffer.clear();
						read = false;
					}
//					System.out.println("acceptable:" + key.isAcceptable());
//					System.out.println("conn:" + key.isConnectable());
//					System.out.println("valid:" + key.isValid());
//					System.out.println("writable::" + key.isWritable());
					if (key.isAcceptable() && !key.isConnectable()) {
						key.cancel();
					}

				}
			}
		} catch (IOException e) {
			// will be caught by Worker.java and logged.
			// Need to throw runtime exception since we cannot
			// keep it as IOException
			throw new RuntimeException(e);
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				System.out.println("Channel not closed.");
				// Throw it so that worker thread can log it.
				throw new RuntimeException(e);
			}
		}
	}
}
