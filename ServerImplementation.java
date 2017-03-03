package Main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImplementation extends UnicastRemoteObject implements MasterServer{
	private static final long serialVersionUID = 1L;
	Master master;
	String bindName;
	
	public ServerImplementation(Master myMaster, String bindName) throws RemoteException {
		super();
		master = myMaster;
		this.bindName = bindName;
	}
	
	public void runServer(){
		try {
			System.setSecurityManager(new SecurityManager());
			Naming.rebind(bindName, this);
			System.out.println("Service registered");
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public Object remoteHandleOneJob(Job job) throws RemoteException{
		return master.handleOneJob(job);
	}

	public void remotePushJob(Job job) throws RemoteException {
		master.handleNewJob(job);
	}

	public Object getResponse(Job job) throws RemoteException {
		return master.getResponse(job.getId());
	}
}