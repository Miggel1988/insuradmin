package com.insuradmin.global;

import javax.inject.Inject;

import com.insuradmin.global.service.KafkaConsumerService;
import com.insuradmin.global.service.KafkaProducerService;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MainPoliceService {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

    	@Inject
    	KafkaConsumerService kafkaConsumerService;
    	
    	@Inject
    	KafkaProducerService kafkaProducerService;
    	
        @Override
        public int run(String... args) throws Exception {
    		new Thread(kafkaConsumerService).start();
    		kafkaProducerService.produceConnected();
            Quarkus.waitForExit();
            return 0;
        }
    }
}
