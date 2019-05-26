package my.forkjoinpool.asynchronous;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderProcessor extends RecursiveTask<List<String>> {

	/*
	 * the asynchronous methods (for example, the fork() method), the task continues
	 * with its execution, so the ForkJoinPool class can't use the work-stealing
	 * algorithm to increase the performance of the application. In this case, only
	 * when you call the join() or get() methods to wait for the finalization of a
	 * task, the ForkJoinPool class can use that algorithm
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 138298788796858950L;
	private String path;
	private String extension;

	public FolderProcessor(String path, String extension) {
		super();
		this.path = path;
		this.extension = extension;
	}

	@Override
	protected List<String> compute() {
		List<String> list = new ArrayList<String>();
		List<FolderProcessor> tasks = new ArrayList();
		File file = new File(path);
		File content[] = file.listFiles();
		if(content!=null) {
			for(int i=0;i<content.length;i++) {
				if(content[i].isDirectory()) {
					FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				}else {
					if(checkFile(content[i].getName())){
						list.add(content[i].getAbsolutePath());
					}
				}
			}
		}
		if(tasks.size()>50) {
			System.out.println(file.getAbsolutePath()+" "+tasks.size()+" tasks ran.");
		}
		addResultsFromTasks(list,tasks);
		return list;
	}
	
	
	
	private boolean checkFile(String name) {
		return name.endsWith(extension);
	}

	/*
	Implement the addResultsFromTasks() method. For each task stored in the list of
	tasks, call the join() method that will wait for its finalization and then will return the
	result of the task. Add that result to the list of strings using the addAll() method.
	*/

	private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
		for(FolderProcessor item: tasks) {
			list.addAll(item.join());
		}
	}

}
