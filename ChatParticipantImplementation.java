package iad.rmi.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatParticipantImplementation extends UnicastRemoteObject implements ChatParticipant {
	/** participant's name. */
	protected String name;
	/** the current conference. */
	protected ChatConference currentConference;
	/** */
	protected BlockingQueue<ChatMessage> msgQueue;
	
	public ChatParticipantImplementation(String name) throws RemoteException {
		this.name = name;
		msgQueue = new LinkedBlockingQueue<ChatMessage>();
		currentConference = null;
	}
	
	@Override
	public String getName() throws RemoteException {
		return name;
	}
	
	@Override
	public boolean join(ChatConference conference) throws RemoteException {
		Boolean result = false;
		if (!isConnected()) {
			currentConference = conference;
			try {
				currentConference.addParticipant(this);
				result = true;
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
		return result;
	}

	@Override
	public void leave(ChatConference conference) throws RemoteException {
		if (conference != null) {
			if (isConnected() && currentConference.equals(conference)) {
				try {
					currentConference.removeParticipant(this);
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		}
	}

	@Override
	public void send(String txt) throws RemoteException {
		if (isConnected()) {
			ChatMessage msg = new ChatMessage(name, txt);
			try {
				currentConference.broadcast(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void process(ChatMessage msg) throws RemoteException {
		if (isConnected()) {
			try {
				msgQueue.put(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isConnected() throws RemoteException {
		return currentConference != null;
	}

	@Override
	public ChatMessage next() throws RemoteException {
		return msgQueue.poll();
	}

	@Override
	public String getCurrentConferenceDescription() throws RemoteException {
		return currentConference.getDescription();
	}
}