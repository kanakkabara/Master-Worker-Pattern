package Tester;

import Main.Job;

public class TestJob extends Job{
	private static final long serialVersionUID = 1L;
	int x,y;
	
	public TestJob(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Object doWork() {
		return x+y;
	}

	@Override
	public String toString() {
		return "TestJob [x=" + x + ", y=" + y + "]";
	}
}
