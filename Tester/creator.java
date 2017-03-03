package Tester;

import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

import Main.JMSHelper;

public class creator {
	private JMSHelper jmsHelper;
	private MessageProducer queueSender;
	
	public creator() throws NamingException, JMSException {
		jmsHelper = new JMSHelper();
		queueSender = jmsHelper.createMasterQueueSender();
	}
	
	public void sendJob() {
		Random rand = new Random();
		TestJob newTest = new TestJob(rand.nextInt(10),rand.nextInt(10));
		System.out.println("Trying to send job: "+newTest);
		try {
			Message message = jmsHelper.createMessage(newTest);
			if(message != null) {
				queueSender.send(message);
			}
		} catch (JMSException e) {
			System.err.println("Failed to send message");
		}
	}

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		creator x = new creator();
		while(true){
			x.sendJob();
			Thread.sleep(3000);
		}
	}
	
}
