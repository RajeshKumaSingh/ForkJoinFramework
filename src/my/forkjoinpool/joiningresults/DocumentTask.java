package my.forkjoinpool.joiningresults;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class DocumentTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7499500641740306193L;
	private String document[][];
	private int start, end;
	private String word;

	public DocumentTask(String[][] document, int start, int end, String word) {
		super();
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		if (end - start < 10) {
			result = processLines(document, start, end, word);
		} else {
			int mid = (start + end) / 2;
			DocumentTask t1 = new DocumentTask(document, start, mid, word);
			DocumentTask t2 = new DocumentTask(document, mid, end, word);
			invokeAll(t1, t2);

			try {
				result = groupResults(t1.get(), t2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private int groupResults(Integer integer, Integer integer2) {
		Integer result;
		result = integer + integer2;
		return result;
	}

	private int processLines(String[][] document, int start, int end, String word) {
		List<LineTask> tasks = new ArrayList<LineTask>();
		for (int i = start; i < end; i++) {
			LineTask task = new LineTask(document[i], 0, document[i].length, word);
			tasks.add(task);
		}
		invokeAll(tasks);
		int result = 0;
		for (int i = 0; i < tasks.size(); i++) {
			LineTask task = tasks.get(i);
			try {
				result = result + task.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
