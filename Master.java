package Main;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import Main.Job.Status;

public abstract class Master implements MessageListener{
	protected JMSHelper jmsHelper;
	protected MessageConsumer masterQueueReader;
	protected MessageProducer workerQueueWriter; 
	private MessageConsumer resultQueueReader;
	
	private Map<UUID, Job> responses = new HashMap<UUID, Job>();
	
	public Object getResponse(UUID id){
		if(responses.containsKey(id)){
			Job obj = responses.remove(id);
			return obj.getResponse();
		}
		else
			return null;
	}
	
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

	public abstract Job handleResult(Job job);
	public abstract void handleNewJob(Job job);

	public void onMessage(Message jmsMessage) {
		try {			
	        Job result = (Job) ((ObjectMessage) jmsMessage).getObject();
	        result = handleResult(result);
	        result.setStatus(Status.FINISHED);
	        
	        responses.put(result.getId(), result);
	    } catch (JMSException e) {
	        System.err.println("Failed to receive message");
	    }
	}

	public void start(){	
		while(true) {
	        Message jmsMessage;
			try {
				jmsMessage = masterQueueReader.receive();
				Object obj = ((ObjectMessage)jmsMessage).getObject();
				if(obj instanceof Job){
					Job newJob = (Job) obj;
					newJob.setStatus(Status.PROCESSING);
					handleNewJob(newJob);
				}
				else
					System.out.println((String) obj);
			} catch (JMSException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void addToWorkerQueue(Job job) throws JMSException{
		job.setStatus(Status.QUEUED);
		this.workerQueueWriter.send(prepare(job));
	}
	
	public ObjectMessage prepare(Serializable obj) throws JMSException {
		if(jmsHelper != null)
			return this.jmsHelper.createMessage(obj);
		else
			throw new JMSException("Unable to create ObjectMessage from Serializable object; Fix by adding a JMSHelper or forcefully use workerQueueWriter.send(job)");
	}
	
	public Object waitFor(Job job){
		Object obj = null; 
		while(obj == null){
			obj = getResponse(job.getId()); 
		}
		return obj;
	}
	
	public Object handleOneJob(Job job) throws RemoteException{
		handleNewJob(job);
		return waitFor(job);
	}
}
