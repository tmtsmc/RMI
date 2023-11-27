package iad.rmi.chat;

import java.rmi.RemoteException;

public interface ChatJMSConference extends ChatConference {
	void activateLog(String password) throws RemoteException;
	
	void deactivateLog(String password) throws RemoteException;
}
