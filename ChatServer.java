package iad.rmi.chat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer {

	public static void main(String[] args) throws RemoteException {
		//ChatConference chatConference = new ChatConferenceImplementation("conf1", "Une conference.");
		ChatJMSConference chatConference = new ChatJMSConferenceImplementation("conf1", "Une conference.", "obviouslyNotMyPassword");
		chatConference.activateLog("obviouslyNotMyPassword");
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("conf1", chatConference);
		chatConference.start();
	}
}
