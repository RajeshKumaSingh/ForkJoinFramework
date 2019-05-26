package my.forkjoinpool.exception;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MainThread {

	public static void main(String[] args) {
		int array[] = new int[100];
		Task task = new Task(array, 0, 100);
		
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		pool.shutdown();
		
		/*
		Wait for the finalization of the task using the awaitTermination() method. As you
		want to wait for the finalization of the task however long it takes to complete, pass the
		values 1 and TimeUnit.DAYS as parameters to this method.
		*/
		
		try {
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//  Exception handling in main
		
		if(task.isCompletedAbnormally()) {
			System.out.println("Main: An exception has ocurred");
			System.out.println("Main: "+task.getException());
		}
		System.out.println("Main: Result: "+task.join());
		
	}

}
