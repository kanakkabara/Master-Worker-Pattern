package Tester;

import javax.jms.JMSException;
import javax.naming.NamingException;

import Main.Job;
import Main.Worker;

public class TestWorker extends Worker{
	public TestWorker() throws NamingException, JMSException {
		super();
	}

	@Override
	public Object handleNewJob(Job newJob) {
		return newJob.doWork();
	}
	
	public static void main(String[] args) {
		try {
			new TestWorker();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleResult(Object result) {
		try {
			this.addToResultQueue(result);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
