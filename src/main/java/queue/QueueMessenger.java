package queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

public class QueueMessenger {

    private static QueueMessenger queueMessenger;
    private static String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;;

    private QueueMessenger(){}

    public static void sendMessage(Serializable message, String queue) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //Creating a non transactional session to send JMS message.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageProducer producer = session.createProducer(destination);
        ObjectMessage toSend = session.createObjectMessage(message);
        producer.send(toSend);
        connection.close();
    }

    public static Message receiveMessage(String queue) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageConsumer consumer = session.createConsumer(destination);
        Message message = consumer.receive();
        connection.close();
        return message;
    }



}
