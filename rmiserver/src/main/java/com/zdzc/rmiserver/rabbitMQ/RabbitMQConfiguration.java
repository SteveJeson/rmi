package com.zdzc.rmiserver.rabbitMQ;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/7/23 0023.
 */
@Configuration
@Component
public class RabbitMQConfiguration {

    @Value("${mq_host}")
    private String host;

    @Value("${mq_username}")
    private String username;

    @Value("${mq_password}")
    private String password;

    @Value("${queue_name}")
    private String queueName;

    @Value("${channel_num}")
    private int channelNum;

    @Bean
    public Producer initMQ(){
        try {
            Producer producer = new Producer(host, username, password);
            producer.initProducer(queueName, channelNum);
            return producer;
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }

}
