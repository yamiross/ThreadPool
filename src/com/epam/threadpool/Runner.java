package com.epam.threadpool;

/**
 * Created by Andrii Seliverstov on 04.09.2015.
 */
public class Runner {
    public static void main(String[] args)
    {
        Runner runner = new Runner();
        runner.init();
    }

    private void init()
    {
        ThreadPool pool = new ThreadPool(4);
        for (int i = 0; i < 100; i++) {
            final int number = i;
            pool.addTask(() -> {
            	System.out.println(Thread.currentThread().getName() + " Before:" + number);
            	try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	System.out.println(Thread.currentThread().getName() + " After:" + number);
            	});
        }
        pool.shutdown();
    }
}
