package com.insuradmin.global.service;

import java.time.Duration;
import java.util.Arrays;
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
import com.insuradmin.global.model.Message;
import com.insuradmin.global.model.MessageType;
import com.insuradmin.global.util.Constants;
import com.insuradmin.global.websocket.GatewaySocket;

@ApplicationScoped
public class KafkaCustomerConsumerService implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(KafkaCustomerConsumerService.class);

	@Inject
	GatewaySocket gatewaySocket;
	
	private Properties properties;
	
	public KafkaCustomerConsumerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put("key.deserializer", Constants.STRING_DESERIALIZER);
		properties.put("value.deserializer", Constants.CUSTOMER_DESERIALIZER);
		properties.put("group.id", Constants.GROUP_ID);
	}

	@Override
	public void run() {
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
		kafkaConsumer.subscribe(
				Arrays.asList(Constants.TOPIC_CUSTOMER_OUT));

		try (KafkaConsumer<String, List<Customer>> consumer = new KafkaConsumer<>(properties)) {

			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));

				final Iterator<ConsumerRecord<String, String>> recordIterator = records.iterator();
				while (recordIterator.hasNext()) {
					ConsumerRecord<String, String> obj = recordIterator.next();
					LOG.info(String.format("Topic - %s, Partition - %d, Value: %s", obj.topic(),
							obj.partition(), obj.value()));

					switch (obj.topic()) {
						case Constants.TOPIC_CUSTOMER_OUT:
							gatewaySocket.broadcast(new Message(obj.value(), MessageType.LIST_CUSTOMER));
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