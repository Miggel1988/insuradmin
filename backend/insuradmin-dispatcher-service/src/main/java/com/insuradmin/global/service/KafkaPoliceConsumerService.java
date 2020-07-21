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

import com.insuradmin.global.model.Message;
import com.insuradmin.global.model.MessageType;
import com.insuradmin.global.model.Police;
import com.insuradmin.global.util.Constants;
import com.insuradmin.global.websocket.GatewaySocket;

@ApplicationScoped
public class KafkaPoliceConsumerService implements Runnable {

	private static final Logger LOG = Logger.getLogger(KafkaPoliceConsumerService.class);

	@Inject
	GatewaySocket gatewaySocket;
	
	private Properties properties;

	public KafkaPoliceConsumerService() {
		properties = new Properties();
		String kafkaServer = System.getenv(Constants.ENV_VAR_BOOTSTRAP_SERVERS_CONFIG);
		kafkaServer = kafkaServer != null ? kafkaServer : Constants.BOOTSTRAP_SERVERS_CONFIG;
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		properties.put("key.deserializer", Constants.STRING_DESERIALIZER);
		properties.put("value.deserializer", Constants.POLICE_DESERIALIZER);
		properties.put("group.id", Constants.GROUP_ID);
	}
	
	@Override
	public void run() {
		KafkaConsumer<String, List<Police>> kafkaConsumer = new KafkaConsumer<String, List<Police>>(properties);
		kafkaConsumer.subscribe(
				Arrays.asList(Constants.TOPIC_POLICE_OUT, Constants.TOPIC_REFRESH));

		try (KafkaConsumer<String, List<Police>> consumer = new KafkaConsumer<>(properties)) {

			while (true) {
				ConsumerRecords<String, List<Police>> records = kafkaConsumer.poll(Duration.ofSeconds(10));

				final Iterator<ConsumerRecord<String, List<Police>>> recordIterator = records.iterator();
				while (recordIterator.hasNext()) {
					ConsumerRecord<String, List<Police>> obj = recordIterator.next();
					LOG.info(String.format("Topic - %s, Partition - %d, Value: %s", obj.topic(),
							obj.partition(), obj.value()));

					switch (obj.topic()) {
						case Constants.TOPIC_POLICE_OUT:
							gatewaySocket.broadcast(new Message(obj.value(), MessageType.LIST_POLICE), obj.key());
							break;
						case Constants.TOPIC_REFRESH:
							gatewaySocket.refreshAll();
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