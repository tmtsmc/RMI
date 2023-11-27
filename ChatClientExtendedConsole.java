package iad.rmi.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ChatClientExtendedConsole extends Thread {

	protected ChatParticipant participant;
	protected BufferedReader reader;
	protected Registry registry; 
	
	public ChatClientExtendedConsole(String name, Registry reg) throws RemoteException {
		participant = new ChatParticipantImplementation(name);
		registry = reg;
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run() {
		String line; 
		
	    while(true) {
	       try {
				printNextMessage();	
			    line = reader.readLine();
			    processCmd(line);
		   } catch (Exception e) {
				e.printStackTrace();
		   }
	    }
	}


	protected void printNextMessage() throws IOException {
		while (! reader.ready()) {
			   ChatMessage msg = null;
		        try {
					msg = participant.next();
		    	}
		    	catch(RemoteException e) {
		    		e.printStackTrace();
		    	}
				if (msg != null) {
					System.out.println(msg.getEmitter() + " -> "  + msg.getContent());
				}
		}
	}
	


	protected void processCmd(String line) throws RemoteException, IOException {
		ChatConference conf = null;
		String[] words = line.split(" ");
		try {
			conf = (ChatConference) registry.lookup(words[1]);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		if (line.equals("!stop")) {
			participant.leave(conf);
			reader.close(); 
			System.exit(0);
		} else if (line.equals("!status")) {
			if (participant.isConnected()) {
				System.out.println("Connected to : " + participant.getCurrentConferenceDescription());
			}
			else {
				System.out.println("Not connected");
			}
		} else if (line.equals("!leave")) {
				if (participant.isConnected()) {
					participant.leave(conf);
				} else {
					System.out.println("Not connected");
				}
		} else if (line.startsWith("!join")) {
			if (words[0].equals("!join")) {
				try {
					participant.join(conf);
				} catch(Exception re) {
					re.printStackTrace();
				}
			}
			else {
				System.out.println("Incorrect join commands");
			}
		} else if (line.equals("!list")) {
			String[] names = registry.list();
			for (String n: names) {
				System.out.println(n);
			} 			
		} else if (line.equals("!info")) {
			System.out.println(participant.getCurrentConferenceDescription()); 
		} else {
			participant.send(line);
		}
	}
	
}
