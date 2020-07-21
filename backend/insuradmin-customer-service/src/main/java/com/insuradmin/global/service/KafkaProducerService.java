package com.insuradmin.global.service;

import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

import com.insuradmin.global.model.Customer;
import com.insuradmin.global.util.Constants;

@ApplicationScoped
public class KafkaProducerService {
	
	private static final Logger LOG = Logger.getLogger(KafkaProducerService.class);

	@Inject
	CustomerService customerService;

	private Properties properties;

	public KafkaProducerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Constants.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Constants.CUSTOMER_SERIALIZER);
	}

	public void produceCustomer() {
		List<Customer> lstCustomer = (List<Customer>) customerService.getCustomers();

		LOG.info("produceCustomer; count="+lstCustomer.size());
		
		try (KafkaProducer<String, List<Customer>> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, List<Customer>>(Constants.TOPIC_CUSTOMER_OUT,
					Constants.KEY_CUSTOMER_REQUEST, lstCustomer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}