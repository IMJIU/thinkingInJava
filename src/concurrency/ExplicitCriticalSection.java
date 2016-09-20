//: concurrency/ExplicitCriticalSection.java
// Using explicit Lock objects to create critical sections.
package concurrency;

import java.util.concurrent.locks.*;

// Synchronize the entire method:
class ExplicitPairManager1 extends PairManager {
	private Lock lock = new ReentrantLock();

	public  void increment() {
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			store(getPair());
		} finally {
			lock.unlock();
		}
	}
	/**
	 * зЂвт
	 */
	public Pair getPair() {
		lock.lock();
		try {
			return new Pair(p.getX(), p.getY());
		} finally {
			lock.unlock();
		}
		// Make a copy to keep the original safe:
	}
}

// Use a critical section:
class ExplicitPairManager2 extends PairManager {
	private Lock lock = new ReentrantLock();

	public void increment() {
		Pair temp;
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		} finally {
			lock.unlock();
		}
		store(temp);
	}
	/**
	 * зЂвт
	 */
	public Pair getPair() {
		lock.lock();
		try {
			return new Pair(p.getX(), p.getY());
		} finally {
			lock.unlock();
		}
		// Make a copy to keep the original safe:
	}
}

public class ExplicitCriticalSection {
	public static void main(String[] args) throws Exception {
		PairManager pman1 = new ExplicitPairManager1(), pman2 = new ExplicitPairManager2();
		CriticalSection.testApproaches(pman1, pman2);
	}
} /*
 * Output: (Sample) 
 * pm1: Pair: x: 15, y: 15 checkCounter =  174 035 
 * pm2: Pair: x:16, y: 16 checkCounter = 2 608 588
 * 
 * pm1: Pair: x: 96, y: 96 checkCounter =   1 413 303
   pm2: Pair: x: 99, y: 99 checkCounter = 167 666 496
 */// :~

