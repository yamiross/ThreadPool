package com.epam.threadpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrii Seliverstov on 04.09.2015.
 */
public class ThreadPool {

    private final Queue<Runnable> workerQueue;
    private final Thread[] threads;

    public ThreadPool(int threadCapacity) {
    	checkIfProperThreadCapacity(threadCapacity);
    	threads = new Thread[threadCapacity];
        workerQueue = new LinkedList<>();
    }

    private void checkIfProperThreadCapacity(int threadCapacity) {
        if (threadCapacity <= 0) {
            throw new IllegalArgumentException("Thread pool minimum capacity is 1. Given capacity: " + threadCapacity);
        }
    }

    public synchronized void addTask(Runnable runnable) {
    	if (threads[0] == null) {
    		init();
    	}
        workerQueue.add(runnable);
        notify();
    }
    
    private void init() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Worker("Pool Thread " + i);
            threads[i].start();
        }
    }

    public void shutdown() {
        while (!workerQueue.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
        for (Thread workerThread : threads) {
            workerThread.interrupt();
        }
    }

    private class Worker
            extends Thread {
        public Worker(String name) {
            super(name);
        }

        public void run() {
            while (!isInterrupted()) {
            	Runnable runnable = null;
                synchronized (ThreadPool.this) {
                    while (workerQueue.isEmpty()) {
                    	try {
                    		ThreadPool.this.wait();
						} catch (InterruptedException e) {
							return;
						}
                    }
                    runnable = workerQueue.poll();
                }
                if (runnable != null) {
                	runnable.run();
                	runnable = null;
                }
            }
        }
    }
}
