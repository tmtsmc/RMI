package iad.rmi.chat;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ChatJoinLeaveLogger {
	public static void main(String[] args) {
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
			QueueConnection connection = factory.createQueueConnection();
			QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("chatqueue");
			QueueReceiver receiver = session.createReceiver(queue);
			connection.start();
			// TODO pour le QROC (méthode pull)
			/*while (true) {
				MapMessage message = (MapMessage) receiver.receive(); // PULL
				String action = message.getString("action");
				String conf = message.getString("conf");
				String name = message.getString("name");
				if (action.equals("join")) {
					System.out.println(name + " joined " + conf + ".");
				} else if (action.equals("leave")) {
					System.out.println(name + " left " + conf + ".");
				}
			}*/
			// PUSH
			receiver.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					if (message instanceof MapMessage) {
						try {
							MapMessage msg = (MapMessage) message;
							String action = msg.getString("action");
							String conf = msg.getString("conf");
							String name = msg.getString("name");
							if (action.equals("join")) {
								System.out.println(name + " joined " + conf + ".");
							} else if (action.equals("leave")) {
								System.out.println(name + " left " + conf + ".");
							}
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
