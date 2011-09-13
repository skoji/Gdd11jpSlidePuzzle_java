package solver;

public interface AbstractSolver extends Runnable {

	public abstract void run();
	public String getResult();
	public void nofityEnd();
}