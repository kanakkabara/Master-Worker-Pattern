package Main;
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

public abstract class Master implements MessageListener{
	protected JMSHelper jmsHelper;
	protected MessageConsumer masterQueueReader;
	protected MessageProducer workerQueueWriter; 
	private MessageConsumer resultQueueReader;
	
	public Master() throws NamingException, JMSException {
		jmsHelper = new JMSHelper();
		masterQueueReader = jmsHelper.createMasterQueueReader();
		workerQueueWriter = jmsHelper.createWorkerQueueSender();
		resultQueueReader = jmsHelper.createResultQueueReader();
		resultQueueReader.setMessageListener(this);
	}
	
	public Master(MessageConsumer consumer, MessageProducer producer) throws NamingException, JMSException {
		masterQueueReader = consumer;
		workerQueueWriter = producer;
	}
	
	public ObjectMessage prepare(Serializable obj) throws JMSException {
		if(jmsHelper != null)
			return this.jmsHelper.createMessage(obj);
		else
			throw new JMSException("Unable to create ObjectMessage from Serializable object; Fix by adding a JMSHelper or forcefully use workerQueueWriter.send(job)");
	}

	public void addToWorkerQueue(Job job) throws JMSException{
		this.workerQueueWriter.send(prepare(job));
	}
	
	public abstract void handleNewJob(Job job);

	public void onMessage(Message jmsMessage) {
		try {			
	        Object result = ((ObjectMessage) jmsMessage).getObject();
	        handleResult(result);
	    } catch (JMSException e) {
	        System.err.println("Failed to receive message");
	    }
	}

	public abstract void handleResult(Object obj);

	public void start(){	
		while(true) {
	        Message jmsMessage;
			try {
				jmsMessage = masterQueueReader.receive();
				Object obj = ((ObjectMessage)jmsMessage).getObject();
				if(obj instanceof Job)
					handleNewJob((Job) obj);
				else
					System.out.println((String) obj);
			} catch (JMSException e) {
				e.printStackTrace();
			}
	    }
	}
}
