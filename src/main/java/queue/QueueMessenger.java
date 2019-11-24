package queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import properties.PropertiesLoader;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.Serializable;

public class QueueMessenger {

    private static QueueMessenger queueMessenger;
    private static String BROKER_URL = PropertiesLoader.getBrokerURL();

    private QueueMessenger(){}


    public static void sendMessage(String message, String queue) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        MessageProducer producer = session.createProducer(destination);
        TextMessage toSend = session.createTextMessage(message);
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
        Message message = consumer.receive(1000);
        connection.close();
        return message;
    }

    public static String receiveMessageString(String queue)throws JMSException{
        Message message = receiveMessage(queue);
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            return textMessage.getText();
        }
        return "";
    }
}
