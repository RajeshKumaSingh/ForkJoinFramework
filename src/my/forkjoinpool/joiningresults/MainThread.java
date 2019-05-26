package my.forkjoinpool.joiningresults;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MainThread {

	public static void main(String[] args) {
		Document mock = new Document();
		String[][] document = mock.genrateDocument(100, 1000, "the");
		DocumentTask task = new DocumentTask(document, 0, 100, "the");

		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

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

		} while (!task.isDone());
		
		pool.shutdown();
		
		// Wait for the finalization of the tasks using the awaitTermination() method.
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Main: The word appears "+task.get()+" times in the document");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
