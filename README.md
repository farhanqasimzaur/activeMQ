README

<-- If activeMQ is not configured -->

1. Download activeMQ from: https://activemq.apache.org/components/classic/download/

2. Extract the zip file in your desired directory.

3. Traverse to the directory by CMD eg: C:\Users\USERNAME\Downloads\apache-activemq-5.15.10-bin\apache-activemq-5.15.10\bin

4. Type the command activemq.bat start

5. This would start your activeMQ. Keep the command prompt active.


<-- Importing library to your project -->

1. Traverse to directory completeActiveMQ/target

2. You would see the library reuseBrokerQueue-1.0-SNAPSHOT.jar

3. Copy the library to your project and import it. 


<-- Using the library -->

1. Create a properties file in your project directory named "brokerconfig.properties"

2. Include a single property in the project named as "brokerURL=failover://tcp://localhost:61616"

3. Default url is mentioned above. Change it to your own will as to where the activeMQ is deployed.

4. In your code, import the class QueueMessenger

5. To send a message, call the static method QueueMessenger.sendMessage(String, String);
5a. 1st parameter is the string to send while 2nd parameter is the queue to send to.

6. To receive the message, call the static method QueueMessenger.receiveMessageString(String);
6a. The string is the queue name from which the message you want to read.


<-- Code changes -->
1. Currently the version only supports sending and receiving string messages.

2. Code is compiled through maven. Import project to your IDE, make sure mvn is configured.

3. Open cmd, go to main project directory and type "mvn clean install"

4. Any changes you bring would be compiled in the .jar file present in the target. Importing this in your project can be used.

