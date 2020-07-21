package com.insuradmin.global;

import javax.inject.Inject;

import com.insuradmin.global.service.DemoDataService;
import com.insuradmin.global.service.KafkaCustomerConsumerService;
import com.insuradmin.global.service.KafkaPoliceConsumerService;
import com.insuradmin.global.service.KafkaProducerService;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MainDispatcher {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

    	@Inject
    	KafkaPoliceConsumerService kafkaPoliceConsumerService;
    	
    	@Inject
    	KafkaCustomerConsumerService kafkaCustomerConsumerService;

    	@Inject
    	KafkaProducerService kafkaProducerService;
    	
    	@Inject
    	DemoDataService demoDataService;

        @Override
        public int run(String... args) throws Exception {
        	demoDataService.initDB();
    		new Thread(kafkaPoliceConsumerService).start();
    		new Thread(kafkaCustomerConsumerService).start();
            Quarkus.waitForExit();
            return 0;
        }
    }
}
