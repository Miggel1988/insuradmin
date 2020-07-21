package com.insuradmin.global.service;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.insuradmin.global.util.Constants;

@ApplicationScoped
public class KafkaProducerService {

	private Properties properties;

	public KafkaProducerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Constants.STRING_SERIALIZER);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Constants.STRING_SERIALIZER);
		properties.put("group.id", Constants.GROUP_ID);
	}
	
	public void requestPolices(String customerID) {
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, String>(Constants.TOPIC_POLICE_IN, Constants.KEY_POLICE_REQUEST, customerID));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deletePolice(String comboID) {
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, String>(Constants.TOPIC_POLICE_IN, Constants.KEY_POLICE_DELETION, comboID));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createPolice(String customerID) {
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, String>(Constants.TOPIC_POLICE_IN, Constants.KEY_POLICE_CREATION, customerID));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void requestCustomers() {
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
			producer.send(new ProducerRecord<String, String>(Constants.TOPIC_CUSTOMER_IN, Constants.KEY_CUSTOMER_REQUEST, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}