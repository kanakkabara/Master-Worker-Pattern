package Main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterServer extends Remote {
	Object remoteHandleJob(Job job) throws RemoteException;
}
