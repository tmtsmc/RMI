package iad.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatParticipant extends Remote {

	public String getName() throws RemoteException;
	
	public String getCurrentConferenceDescription() throws RemoteException;
	
	public abstract boolean join(ChatConference conference) throws RemoteException;

	public abstract void leave(ChatConference conference) throws RemoteException;

	public abstract void send(String txt) throws RemoteException;

	public abstract void process(ChatMessage msg) throws RemoteException;

	public abstract boolean isConnected() throws RemoteException;

	public ChatMessage next() throws RemoteException;
}