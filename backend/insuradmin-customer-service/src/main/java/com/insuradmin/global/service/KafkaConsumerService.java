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
import org.jboss.logging.Logger;

import com.insuradmin.global.model.Customer;
import com.insuradmin.global.util.Constants;

@ApplicationScoped
public class KafkaConsumerService implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(KafkaConsumerService.class);

	@Inject
	KafkaProducerService kafkaProducerService;

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
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		kafkaConsumer.subscribe(Collections.singletonList(Constants.TOPIC_CUSTOMER_IN));

		try (KafkaConsumer<String, List<Customer>> consumer = new KafkaConsumer<>(properties)) {

			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));

				final Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator();
				while (recordIterator.hasNext()) {
					ConsumerRecord<String, String> obj = recordIterator.next();
					LOG.info(String.format("Topic - %s, Partition - %d, Value: %s", obj.topic(),
							obj.partition(), obj.value()));

					if (obj.key().equals(Constants.KEY_CUSTOMER_REQUEST)) {
						kafkaProducerService.produceCustomer();
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