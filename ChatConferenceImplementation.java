package iad.rmi.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatConferenceImplementation extends UnicastRemoteObject implements ChatConference {
	/** Serial ID. */
	private static final long serialVersionUID = 1L;
	/** Chat conference name. */
	protected String name;
	/** Chat conference description. */
	protected String description;
	/** Map nom<->chatParticipant. */
	protected Map<String, ChatParticipant> participants = new HashMap<String, ChatParticipant>();
	/** Define the status of the chat conference. */
	protected Boolean isStarted;
	
	/** Constructeur. */
	protected ChatConferenceImplementation(String name, String description) throws RemoteException {
		super();
		this.name = name;
		this.description = description;
		isStarted = false;
	}
	
	@Override
	public String getName() throws RemoteException {
		return name;
	}
	
	@Override
	public String getDescription() throws RemoteException {
		return description;
	}
	
	@Override
	public String[] participants() throws RemoteException {
		return (String[]) participants.keySet().toArray();
	}
	
	@Override
	public boolean isStarted() throws RemoteException {
		return isStarted;
	}
	
	@Override
	public void addParticipant(ChatParticipant p) throws RemoteException {
		String nameParticipant = p.getName();
		if (!participants.containsKey(nameParticipant)) {
			participants.put(nameParticipant, p);
			broadcast(new ChatMessage(name, "Participant " + nameParticipant + " joined conference."));
		}
	}
	
	@Override
	public void removeParticipant(ChatParticipant p) throws RemoteException {
		String nameParticipant = p.getName();
		if (participants.containsKey(nameParticipant)) {
			participants.remove(nameParticipant);
			broadcast(new ChatMessage(name, "Participant " + nameParticipant + " left conference."));
		}
	}
	
	@Override
	public void broadcast(ChatMessage message) throws RemoteException {
		if (isStarted) {
			String emitter = message.getEmitter();
			for (String nameParticipants : participants.keySet()) {
				if (!nameParticipants.equals(emitter)) {
					try {
						participants.get(nameParticipants).process(message);
					} catch (Exception e) {
						System.err.println("Caught Exception : " + e.getMessage());
					}
				}
			}
		}
	}
	
	@Override
	public void start() throws RemoteException {
		if (!isStarted) {
			broadcast(new ChatMessage(name, "Conference started."));
			isStarted = true;
		}
	}
	
	@Override
	public void stop() throws RemoteException {
		if (isStarted) {
			broadcast(new ChatMessage(name, "Conference stoped."));
			isStarted = false;
		}
	}
}
