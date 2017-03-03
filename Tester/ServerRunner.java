package Tester;
import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import Main.ServerImplementation;

public class ServerRunner {
	public static void main(String[] args) throws RemoteException {
		TestMaster tm = null;
		try {
			tm = new TestMaster();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
		ServerImplementation SI = new ServerImplementation(tm, "RMITest");
		SI.runServer();
	}
}
