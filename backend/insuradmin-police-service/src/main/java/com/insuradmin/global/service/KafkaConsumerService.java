package com.insuradmin.global.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import com.insuradmin.global.model.Police;
import com.insuradmin.global.util.Constants;

@ApplicationScoped
public class KafkaConsumerService implements Runnable {

	@Inject
	KafkaProducerService kafkaProducerService;

	@Inject
	PoliceService policeService;
	
	private Properties properties;

	public KafkaConsumerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put("key.deserializer", Constants.STRING_DESERIALIZER);
		properties.put("value.deserializer", Constants.STRING_DESERIALIZER);
		properties.put("group.id", Constants.GROUP_ID);
	}

	@Override
	public void run() {
		String policeID, customerID;
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		kafkaConsumer.subscribe(Collections.singletonList(Constants.TOPIC_POLICE_IN));

		try (KafkaConsumer<String, List<Police>> consumer = new KafkaConsumer<>(properties)) {

			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));

				final Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator();
				while (recordIterator.hasNext()) {
					ConsumerRecord<String, String> obj = recordIterator.next();
					System.out.println(String.format("Topic - %s, Partition - %d, Value: %s, Key: %s", obj.topic(),
							obj.partition(), obj.value(), obj.key()));

					switch (obj.key()) {
					case Constants.KEY_POLICE_REQUEST:
						customerID = obj.value();
						kafkaProducerService.producePolicies(customerID);
						break;
					case Constants.KEY_POLICE_DELETION:
						String combo = obj.value();
						policeID = combo.split("/")[1];
						customerID=combo.split("/")[0];
						
						policeService.delete(policeID);
						kafkaProducerService.producePolicies(customerID);
						break;
					case Constants.KEY_POLICE_CREATION:
						customerID = obj.value();
						policeService.create(customerID);
						kafkaProducerService.producePolicies(customerID);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			kafkaConsumer.close();
		}
	}

	

}