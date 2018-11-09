package com.zdzc.rmiserver.rabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class RabbitMQFactory {

    protected Connection connection;

    protected Channel channel;

    protected ConnectionFactory factory;

    protected String queueName;

    public RabbitMQFactory(){}

    public RabbitMQFactory(String host, String username, String password) throws IOException,TimeoutException {
        //Create a connection factory
        this.factory = new ConnectionFactory();
        //hostname of your rabbitmq server
        this.factory.setHost(host);
        this.factory.setUsername(username);
        this.factory.setPassword(password);
        this.factory.setPort(5672);
    }

    public void initProducer(String queueName, int channelNum) throws Exception {
        this.queueName = queueName;
        //getting a connection
        connection = this.factory.newConnection();
        //creating a channel
//        for (int i = 0; i < channelNum; i++) {
        channel = connection.createChannel();
            //declaring a queue for this channel. If queue does not exist,
            //it will be created on the server.
        channel.queueDeclare(queueName,true,false,false,null);
//        }
    }

}
