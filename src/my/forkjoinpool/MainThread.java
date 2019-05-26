package my.forkjoinpool;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MainThread {

	public static void main(String[] args) {
		ProductListGenerator generator = new ProductListGenerator();
		List<Product> products = generator.generate(10000);
		Task task = new Task(products,0,products.size(),0.20);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		do {
			System.out.println("Main: Thread Count: "+pool.getActiveThreadCount());
			
			System.out.println("Main: Thread steal: "+pool.getStealCount());
			
			System.out.println("Main: Parallelism: "+pool.getParallelism()+"\n");
			
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(!task.isDone());
		
		pool.shutdown();
		
		if(task.isCompletedNormally()) {
			System.out.println("Main: The process has completed mormally\n");
		}
		
		for(int i=0;i<products.size();i++) {
			Product product= products.get(i);
			if(product.getPrice()!=12) {
				System.out.println("Product: "+product.getName()+" "+product.getPrice());
			}
		}
		System.out.println("\nMain: End of the program");
	}

}
