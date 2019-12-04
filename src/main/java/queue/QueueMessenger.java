package queue;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

public class QueueMessenger {

    private static QueueMessenger queueMessenger;
    private static String BROKER_URL = PropertiesLoader.getBrokerURL();
    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .serializeNulls()
            .create();

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

    /**
     * @param object The object to be sent.
     * @param queue The name of the queue to send the object to.
     * @throws JMSException If any error occurs
     */
    public static void sendObject(Object object, String queue)throws JMSException{
        String toJson = gson.toJson(object);
        sendMessage(toJson, queue);
    }

    /**
     * @param typeOf This is the class of the object which you want to receive. Example User.class
     * @param queue This is the name of the queue from which you would receive the object.
     * @param <T> Generic type object. If User.class object is received, User.class object is returned
     * @return Returning the object specified.
     * @throws JMSException If any error occurs
     */
    public static <T> T receiveObject(Class<T> typeOf, String queue)throws JMSException{
        String jsonReceived = receiveMessageString(queue);
        return gson.fromJson(jsonReceived, typeOf);
    }
}
