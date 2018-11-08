package com.zdzc.rmiclient;

import com.zdzc.api.SumService;
import com.zdzc.rmiclient.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@SpringBootApplication
public class RmiclientApplication {
    @Value("${rmi.url}")
    private String url;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RmiclientApplication.class, args);
        SpringContextUtil.setApplicationContext(context);
    }


    @Bean
    RmiProxyFactoryBean service(){
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl(url);
        rmiProxyFactoryBean.setServiceInterface(SumService.class);
        return rmiProxyFactoryBean;
    }
}
