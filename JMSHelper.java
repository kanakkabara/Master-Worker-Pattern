package Main;
import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSHelper {
	
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 3700;
	
	private static final String JMS_CONNECTION_FACTORY = "jms/TestConnectionFactory";
	private static final String MASTER_QUEUE = "jms/MasterQueue";
	private static final String WORKER_QUEUE = "jms/WorkerQueue";
	private static final String RESULT_QUEUE = "jms/ResultQueue";

	
	private Context jndiContext;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	
	private Session session;
	private Queue masterQueue;
	private Queue workerQueue;
	private Queue resultQueue;

	public JMSHelper() throws NamingException, JMSException {
		this(DEFAULT_HOST);
	}
	public JMSHelper(String host) throws NamingException, JMSException {
	    int port = DEFAULT_PORT;
	    
	    System.setProperty("org.omg.CORBA.ORBInitialHost", host);
	    System.setProperty("org.omg.CORBA.ORBInitialPort", ""+port);
	    try {
	        jndiContext = new InitialContext();
	        connectionFactory = (ConnectionFactory)jndiContext.lookup(JMS_CONNECTION_FACTORY);
	        masterQueue = (Queue)jndiContext.lookup(MASTER_QUEUE);
	        workerQueue = (Queue)jndiContext.lookup(WORKER_QUEUE);
	        resultQueue = (Queue)jndiContext.lookup(RESULT_QUEUE);
	    } catch (NamingException e) {
	        System.err.println("JNDI failed: " + e);
	        throw e;
	    }
	    
	    try {
	        connection = connectionFactory.createConnection();
	        connection.start();
	    } catch (JMSException e) {
	        System.err.println("Failed to create connection to JMS provider: " + e);
	        throw e;
	    }
	}
	public Session createSession() throws JMSException {
		if(session != null) {
	        return session;
	    } else {
	        try {
	            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        } catch (JMSException e) {
	            System.err.println("Failed creating session: " + e);
	            throw e;
	        }
	    }
	}
	public ObjectMessage createMessage(Serializable obj) throws JMSException {
	    try {
	        return createSession().createObjectMessage(obj);
	    } catch (JMSException e) {
	        System.err.println("Error preparing message: " + e);
	        throw e;
	    }
	}
	public MessageProducer createMasterQueueSender() throws JMSException {
		try {
	        return createSession().createProducer(masterQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed sending to queue: " + e);
	        throw e;
	    }
	}
	public MessageConsumer createMasterQueueReader() throws JMSException {
	    try {
	        return createSession().createConsumer(masterQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed reading from queue: " + e);
	        throw e;
	    }	
	}
	public MessageProducer createWorkerQueueSender() throws JMSException {
	    try {
	        return createSession().createProducer(workerQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed sending to queue: " + e);
	        throw e;
	    }
	}
	public MessageConsumer createWorkerQueueReader() throws JMSException {
	    try {
	        return createSession().createConsumer(workerQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed reading from queue: " + e);
	        throw e;
	    }
	}
	public MessageProducer createResultQueueSender() throws JMSException {
	    try {
	        return createSession().createProducer(resultQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed reading from queue: " + e);
	        throw e;
	    }
	}
	public MessageConsumer createResultQueueReader() throws JMSException {
	    try {
	        return createSession().createConsumer(resultQueue);
	    } catch (JMSException e) {
	        System.err.println("Failed reading from queue: " + e);
	        throw e;
	    }
	}
}

