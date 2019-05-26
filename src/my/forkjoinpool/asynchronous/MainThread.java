package my.forkjoinpool.asynchronous;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MainThread {
	
	/*
	 * the asynchronous methods (for example, the fork() method), the task continues
	 * with its execution, so the ForkJoinPool class can't use the work-stealing
	 * algorithm to increase the performance of the application. In this case, only
	 * when you call the join() or get() methods to wait for the finalization of a
	 * task, the ForkJoinPool class can use that algorithm
	 */

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
		FolderProcessor apps = new FolderProcessor("C:\\Program Files", "log");
		FolderProcessor documents = new FolderProcessor("C:\\Users\\Rajesh", "log");
		
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		
		do {
			System.out.println("Main: Thread Count: " + pool.getActiveThreadCount());
			
			System.out.println("Main: Task Count: " + pool.getQueuedTaskCount());

			System.out.println("Main: Thread steal: " + pool.getStealCount());

			System.out.println("Main: Parallelism: " + pool.getParallelism() + "\n");

			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}while(!system.isDone() && !apps.isDone() && !documents.isDone());
		
		pool.shutdown();
		
		List<String> results;
		results=  system.join();
		System.out.println("System files found: "+results.size());
		results=  apps.join();
		System.out.println("Apps files found: "+results.size());
		results=  documents.join();
		System.out.println("Document files found: "+results.size());
		
	}

}
