package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static String getBrokerURL(){
        String toReturn = "";
        try (InputStream input = new FileInputStream("brokerconfig.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            toReturn =  prop.getProperty("brokerURL");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return toReturn;
    }

}
