package Main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterServer extends Remote {
	Object remoteHandleOneJob(Job job) throws RemoteException;
	void remotePushJob(Job job) throws RemoteException;
	Object getResponse(Job job) throws RemoteException;
}
