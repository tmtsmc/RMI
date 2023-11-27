package iad.rmi.chat;

import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ChatJMSConferenceImplementation extends ChatConferenceImplementation implements ChatJMSConference {
	protected String password;
	protected Boolean logged;
	protected QueueConnection connection;
	protected QueueSession session;
	protected QueueSender sender;
	protected Queue queue;
	
	protected ChatJMSConferenceImplementation(String name, String description, String password) throws RemoteException {
		super(name, description);
		this.password = password;
		this.logged = false;
	}
	
	@Override
	public void addParticipant(ChatParticipant participant) throws RemoteException {
		try {
			String name = participant.getName();
			super.addParticipant(participant);
			if (logged) {
				sendJMSMessage(name, "join"); // TODO important pour le QROC.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeParticipant(ChatParticipant participant) throws RemoteException {
		try {
			String name = participant.getName();
			super.removeParticipant(participant);
			if (logged) {
				sendJMSMessage(name, "join");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Envoie des messages JMS (commentaire très util) */
	protected void sendJMSMessage(String pName, String action) throws RemoteException {
		MapMessage message;
		try {
			message = session.createMapMessage();
			message.setString("name", pName);
			message.setString("action", action);
			message.setString("conf", name);
			sender.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void activateLog(String password) throws RemoteException {
		if (!logged && this.password.equals(password)) {
			try {
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
				connection = factory.createQueueConnection();
				session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				queue = session.createQueue("chatqueue");
				sender = session.createSender(queue);
				logged = true;
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deactivateLog(String password) throws RemoteException {
		if (!logged && this.password.equals(password)) {
			try {
				session.close();
				connection.close();
				logged = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
