package Main;
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

public abstract class Worker implements MessageListener {
	private JMSHelper jmsHelper;
	private MessageConsumer workerQueueReader;
	private MessageProducer resultQueueWriter;
	
	public Worker() throws NamingException, JMSException {
		jmsHelper = new JMSHelper();
		workerQueueReader = jmsHelper.createWorkerQueueReader();
		workerQueueReader.setMessageListener(this);
		resultQueueWriter = jmsHelper.createResultQueueSender();
		System.out.println("All set up, starting Worker!");
		this.start();
	}
	
	public Worker(MessageConsumer consumer, MessageProducer producer) throws NamingException, JMSException {
		workerQueueReader = consumer;
		resultQueueWriter = producer;
	}
	
	public void onMessage(Message jmsMessage) {
		try {
	        Job newJob = (Job)((ObjectMessage) jmsMessage).getObject();
			System.out.println("New Job: "+ newJob);
	        Object result = handleResult(handleNewJob(newJob));
	        
	        if(result!=null){
	        	newJob.setResponse(result);
				this.addToResultQueue(newJob);
	        }
	    } catch (JMSException e) {
	        System.err.println("Failed to receive message");
	    }
	}

	public void start(){	
		while(true) {}
	}
	
	//TODO
	private void addToResultQueue(Object result) throws JMSException{
		this.resultQueueWriter.send(prepare(result));
	}
	
	public ObjectMessage prepare(Object result) throws JMSException {
		if(jmsHelper != null)
			return this.jmsHelper.createMessage((Serializable) result);
		else
			throw new JMSException("Unable to create ObjectMessage from Serializable object; Fix by adding a JMSHelper or forcefully use workerQueueWriter.send(job)");
	}
	
	public abstract Object handleNewJob(Job newJob);
	public abstract Object handleResult(Object result);
}
