package com.zdzc.rmiserver.rabbitMQ;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer extends RabbitMQFactory {
    public Producer(String host, String username, String password) throws IOException, TimeoutException {
        super(host, username, password);
    }

    public void sendMessage(String message) throws IOException {
        channel.basicPublish("", queueName, null, message.getBytes());
    }

}
