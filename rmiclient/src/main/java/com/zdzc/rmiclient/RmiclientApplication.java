package com.zdzc.rmiclient;

import com.zdzc.api.service.SumService;
import com.zdzc.rmiclient.util.SpringContextUtil;
import com.zdzc.rmiclient.util.dynamicDataSource.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@SpringBootApplication
@Import({DynamicDataSourceRegister.class})
@ComponentScan("com.zdzc.rmiclient.**")
@MapperScan("com.zdzc.rmiclient.dao")
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
