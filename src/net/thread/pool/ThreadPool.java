package net.thread.pool;

//: TIEJ:X1:ThreadPool.java
//Thread that polls and executes tasks in pool.
//{RunByHand}
import java.util.*;

public class ThreadPool extends Thread {
	
	private static final int DEFAULT_NUM_WORKERS = 5;
	private int workers = 0;
	private LinkedList workerList = new LinkedList();
	
	private LinkedList taskList = new LinkedList();
	
	private boolean stopped = false;

	public ThreadPool() {
		this(DEFAULT_NUM_WORKERS);
	}

	public ThreadPool(int numOfWorkers) {
		workers = numOfWorkers;
		for (int i = 0; i < numOfWorkers; i++){
			workerList.add(new Worker("" + i, this));
		}
		start();
	}
	public int getWorkers(){
		return workers;
	}
	public void run() {
		try {
			while (!stopped) {
				if (taskList.isEmpty()) {
					synchronized (taskList) {
						// If queue is empty, wait for tasks to be added
						taskList.wait();
					}
				} else if (workerList.isEmpty()) {
					synchronized (workerList) {
						// If no worker threads available, wait till
						// one is available
						workerList.wait();
					}
				}
				// Run the next task from the tasks scheduled
				getWorker().setTask((Runnable) taskList.removeLast());
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void addTask(Runnable task) {
		taskList.addFirst(task);
		synchronized (taskList) {
			taskList.notify(); // If new task added, notify
		}
	}

	public void putWorker(Worker worker) {
		workerList.addFirst(worker);
		// There may be cases when you have a pool of 5 threads
		// and the requirement exceeds this. That is when a Worker is required
		// but none is available (or free), it just blocks on threadpool.
		// This is the event that there is now a free Worker thread in
		// threadpool. Hence this thread does a notification that unblocks
		// the ThreadPool thread waiting on threadpool
		synchronized (workerList) {
			workerList.notify();
		}
	}

	private Worker getWorker() {
		return (Worker) workerList.removeLast();
	}

	public boolean isStopped() {
		return stopped;
	}

	public void stopThreads() {
		System.out.println("Stop!!!!!!!!!!!");
		stopped = true;
		Iterator it = workerList.iterator();
		while (it.hasNext()) {
			Worker w = (Worker) it.next();
			synchronized (w) {
				w.notify();
			}
		}
	} // Junit test

	public static void main(String[] args) {
		ThreadPool tp = new ThreadPool(100);
		for (int i = 0; i < tp.getWorkers()*1000; i++) {
			tp.addTask(new Runnable() {
				public void run() {
					System.out.println("jjj");
				}
			});
		}
		try {
			Thread.currentThread().sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tp.stopThreads();
	}
} // /:~
