package me.vaysman.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TopicTest {

    public static void main(String[] args) {

        InitialContext initialContext = null;
        Topic topic;
        ConnectionFactory cf;
        try {
            initialContext = new InitialContext();
            topic = (Topic) initialContext.lookup("topic/exampleTopic");
            cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        } catch (NamingException exc) {
            closeResource(initialContext);
            throw new RuntimeException(exc);
        }

        Connection connection = null;
        try {
            connection = cf.createConnection("admin", "admin");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(topic);
            MessageConsumer consumer1 = session.createConsumer(topic);
            MessageConsumer consumer2 = session.createConsumer(topic);

            TextMessage message = session.createTextMessage("This is a text message");
            producer.send(message);
            System.out.printf("Sent message: %s%n", message.getText());

            connection.start();

            TextMessage messageReceived = (TextMessage) consumer1.receive();
            System.out.printf("Consumer 1 received message: %s%n", messageReceived.getText());

            messageReceived = (TextMessage) consumer2.receive();
            System.out.printf("Consumer 2 received message: %s%n", messageReceived.getText());

            connection.stop();

        } catch (JMSException exc) {
            throw new RuntimeException(exc);
        } finally {
            closeResource(connection);
            closeResource(initialContext);
        }

    }

    private static void closeResource(InitialContext initialContext) {
        if (initialContext == null) return;
        try {
            initialContext.close();
        } catch (NamingException exc) {
            exc.printStackTrace();
        }
    }

    private static void closeResource(Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }

}
