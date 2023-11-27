package iad.rmi.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

public class ChatClientConsole extends Thread {
	
	protected ChatConference conference;
	protected ChatParticipant participant;
	protected BufferedReader reader; 
	
	public ChatClientConsole(ChatConference c, ChatParticipant p) {
		conference = c;
		participant = p;
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run() {
		String line; 
		
	    while(true) {
	       try {
				printNextMessage();	
			    line = reader.readLine();
			    if (line.equals("stop")) {
			    	participant.leave(conference);
					reader.close(); 
					System.exit(0);
			    } else {
			    	participant.send(line);
			    }
		   } catch (Exception e) {
				e.printStackTrace();
		   }
	    }
	}

	private void printNextMessage() throws IOException {
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
}

