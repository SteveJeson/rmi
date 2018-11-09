package com.zdzc.rmiserver;

import com.zdzc.api.service.SumService;
import com.zdzc.rmiserver.util.dynamicDataSource.DynamicDataSourceRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;

@SpringBootApplication
@Import({DynamicDataSourceRegister.class})
public class RmiserverApplication {
    @Value("${rmi.port}")
    private int port;

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
        exporter.setRegistryPort(port);
        return exporter;
    }
}
