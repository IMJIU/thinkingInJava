package concurrency;

//: concurrency/AtomicityTest.java
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicityTest implements Runnable {
	// private Lock lock = new ReentrantLock();
	private int i = 0;

	public int getValue() {
		return i;
	}

	private synchronized void evenIncrement() {

		i++;
		i++;
		// Lock lock = new ReentrantLock();
		// try
		// {
		// lock.lock();
		// i++;
		// i++;
		// }
		// finally
		// {
		// lock.unlock();
		// }
	}

	public void run() {
		while (true)
			evenIncrement();
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		AtomicityTest at = new AtomicityTest();
		exec.execute(at);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (true) {
			int val = at.getValue();
			if (val % 2 != 0) {
				System.out.println(val);
				System.exit(0);
			}
		}
	}
} /*
 * Output: (Sample) 191583767
 */// :~
