package iad.rmi.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatClient {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		String ip = "localhost"; //"172.27.161.107"; // Marine' server.
		String port = "1099"; // Marine's port.
		ChatConference chatConference = (ChatConference) Naming.lookup("rmi://" + ip + ":" + port + "/conf1");
		System.out.println(chatConference.getName());
		System.out.println(chatConference.getDescription());
		ChatParticipant participant = new ChatParticipantImplementation(args[0]);
		participant.join(chatConference);
		new ChatClientConsole(chatConference, participant).start();
	}
}
