package iad.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatConference extends Remote {
	
	public String getName() throws RemoteException;
	
	public String getDescription() throws RemoteException;
	
	public String[] participants() throws RemoteException;
	
	public abstract boolean isStarted() throws RemoteException;
	
	public abstract void addParticipant(ChatParticipant p) throws RemoteException;

	public abstract void removeParticipant(ChatParticipant p) throws RemoteException;

	public abstract void broadcast(ChatMessage message) throws RemoteException;

	public abstract void start() throws RemoteException;

	public abstract void stop() throws RemoteException;
}