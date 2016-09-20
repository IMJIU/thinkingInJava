package concurrency;

//: concurrency/TestBlockingQueues.java
// {RunByHand}
import java.util.concurrent.*;
import java.io.*;
import static net.mindview.util.Print.*;

class LiftOffRunner implements Runnable {

	private BlockingQueue<LiftOff> rockets;

	public LiftOffRunner(BlockingQueue<LiftOff> queue) {
		rockets = queue;
	}

	public void add(LiftOff lo) {
		try {
			rockets.put(lo);
		} catch (InterruptedException e) {
			print("Interrupted during put()");
		}
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				LiftOff rocket = rockets.take();
				rocket.run(); // Use this thread
			}
		} catch (InterruptedException e) {
			print("Waking from take()");
		}
		print("Exiting LiftOffRunner");
	}
}

public class TestBlockingQueues {

	public static void getkey() {
		try {
			// Compensate for Windows/Linux difference in the
			// length of the result produced by the Enter key:
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (java.io.IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void getkey(String message) {
		print(message);
		getkey();
	}

	public static void test(String msg, BlockingQueue<LiftOff> queue) {
		print(msg);
		LiftOffRunner runner = new LiftOffRunner(queue);
		Thread t = new Thread(runner);
		t.start();
		for (int i = 0; i < 5; i++)
			runner.add(new LiftOff(5));
		getkey("Press 'Enter' (" + msg + ")");
		t.interrupt();
		print("Finished " + msg + " test");
	}

	public static void main(String[] args) {
		test("LinkedBlockingQueue", // Unlimited size
				new LinkedBlockingQueue<LiftOff>());
		test("ArrayBlockingQueue", // Fixed size
				new ArrayBlockingQueue<LiftOff>(3));
		test("SynchronousQueue", // Size of 1
				new SynchronousQueue<LiftOff>());
	}
} /*
// /:~
LinkedBlockingQueue
Press 'Enter' (LinkedBlockingQueue)
#0(4), #0(3), #0(2), #0(1), #0(Liftoff!), #1(4), #1(3), #1(2), #1(1), #1(Liftoff!), #2(4), #2(3), #2(2), #2(1), #2(Liftoff!), #3(4), #3(3), #3(2), #3(1), #3(Liftoff!), #4(4), #4(3), #4(2), #4(1), #4(Liftoff!), 
Finished LinkedBlockingQueue test

Waking from take()
Exiting LiftOffRunner
ArrayBlockingQueue
#5(4), #5(3), #5(2), #5(1), #5(Liftoff!), Press 'Enter' (ArrayBlockingQueue)
#6(4), #6(3), #6(2), #6(1), #6(Liftoff!), #7(4), #7(3), #7(2), #7(1), #7(Liftoff!), #8(4), #8(3), #8(2), #8(1), #8(Liftoff!), #9(4), #9(3), #9(2), #9(1), #9(Liftoff!), 
Finished ArrayBlockingQueue test

Waking from take()
Exiting LiftOffRunner
SynchronousQueue
#10(4), #10(3), #10(2), #10(1), #10(Liftoff!), #11(4), #11(3), #11(2), #11(1), #11(Liftoff!), #12(4), #12(3), #12(2), #12(1), #12(Liftoff!), #13(4), #13(3), #13(2), #13(1), #13(Liftoff!), #14(4), Press 'Enter' (SynchronousQueue)
#14(3), #14(2), #14(1), #14(Liftoff!), 
Finished SynchronousQueue test

Waking from take()
Exiting LiftOffRunner
*/
