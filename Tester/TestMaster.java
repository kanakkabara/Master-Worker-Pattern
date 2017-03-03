package Tester;

import javax.jms.JMSException;
import javax.naming.NamingException;

import Main.Job;
import Main.Master;

public class TestMaster extends Master{
	public TestMaster() throws NamingException, JMSException {
		super();
	}
	
	public static void main(String[] args) {
		try {
			new TestMaster().start();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public void handleNewJob(Job job) {
		System.out.println(job.getId()+" - "+job);
		try {
			addToWorkerQueue(job);			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Job handleResult(Job obj) {
		return obj;
	}
}
