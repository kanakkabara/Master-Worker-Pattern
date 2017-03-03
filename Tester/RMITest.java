package Tester;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import Main.MasterServer;

public class RMITest {
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		MasterServer x = (MasterServer)Naming.lookup("RMITest");
		
		Random rand = new Random();
		TestJob newTest = new TestJob(rand.nextInt(10),rand.nextInt(10));
		System.out.println(newTest);
		System.out.println((int) x.remoteHandleJob(newTest));
	}
}
