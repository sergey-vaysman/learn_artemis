package me.vaysman.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueTest {

    public static void main(String[] args) {

        InitialContext initialContext;
        Queue queue;
        ConnectionFactory cf;

        try {
            // Step 1. Create an initial context to perform JNDI lookup
            initialContext = new InitialContext();

            // Step 2. Perform a lookup on the queue
            queue = (Queue) initialContext.lookup("queue/exampleQueue");

            // Step 3. Perform a lookup on the Connection Factory
            cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

        } catch (NamingException exc) { throw new RuntimeException(exc); }

        Connection connection;
        try {
            // Step 4. Create a JMS Connection
            connection = cf.createConnection("admin", "admin");

            // Step 5. Create a JMS Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Step 6. Create a JMS Message Producer
            MessageProducer producer = session.createProducer(queue);

            // Step 7. Create a Text Message
            TextMessage message = session.createTextMessage("This is a text message");

            // Step 8. Send the message
            System.out.printf("Sent message %s%n", message.getText());
            producer.send(message);

            // Step 9. Create a JMS Message Consumer
            MessageConsumer consumer = session.createConsumer(queue);

            // Step 10. Start the connection
            connection.start();

            // Step 11. Receive the message
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.printf("Received message: %s%n", messageReceived.getText());

        } catch (JMSException exc) {
            throw new RuntimeException(exc);
        }

        // Step 12. Be sure to close our JMS resources!
        try {
            initialContext.close();
        } catch (NamingException exc) { exc.printStackTrace(); }
        try {
            connection.close();
        } catch (JMSException exc) { exc.printStackTrace(); }

    }

}
