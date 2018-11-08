package com.zdzc.rmiserver;

import com.zdzc.api.service.SumService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;

@SpringBootApplication
public class RmiserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmiserverApplication.class, args);
    }

    @Bean
    SumService sumService(){
        return new SumServiceImpl();
    }

    @Bean
    RmiServiceExporter exporter(SumService sumService){
        Class<SumService> serviceInterface = SumService.class;
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(sumService);
        exporter.setServiceName(serviceInterface.getSimpleName());
        exporter.setRegistryPort(1099);
        return exporter;
    }
}
