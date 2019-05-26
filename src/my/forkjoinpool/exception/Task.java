package my.forkjoinpool.exception;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class Task extends RecursiveTask<Integer> {

	private int array[];
	private int start, end;

	public Task(int[] array, int start, int end) {
		super();
		this.array = array;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: Start from "+start+" to "+end);
		if(end-start<10) {
			if((3>start)&&(3<end)) {
				throw new RuntimeException("This task throws an Exception: Task form "+start+" to "+end);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			int mid = (start+end)/2;
			Task t1 = new Task(array, start, mid);
			Task t2 = new Task(array, mid, end);
			invokeAll(t1,t2);
		}
		System.out.println("Task: End from "+start+" to "+end);
		return 0;
	}

}
