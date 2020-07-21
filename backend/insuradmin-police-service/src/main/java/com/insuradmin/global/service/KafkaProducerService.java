package com.insuradmin.global.service;

import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

import com.insuradmin.global.model.Police;
import com.insuradmin.global.util.Constants;

@ApplicationScoped
public class KafkaProducerService {

	@Inject
	PoliceService policeService;

	private static final Logger LOG = Logger.getLogger(KafkaProducerService.class);

	private Properties properties;
	
	public KafkaProducerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Constants.STRING_SERIALIZER);
	}
	
	public void producePolicies(String customerID) {
		List<Police> lstPolicies = (List<Police>) policeService.getPolices(customerID);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Constants.POLICE_SERIALIZER);

		LOG.info("producePolicies; count="+lstPolicies.size());
		
		try (KafkaProducer<String, List<Police>> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, List<Police>>(Constants.TOPIC_POLICE_OUT,
					customerID, lstPolicies));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void produceConnected() {
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,Constants.STRING_SERIALIZER);

		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, String>(Constants.TOPIC_REFRESH, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}