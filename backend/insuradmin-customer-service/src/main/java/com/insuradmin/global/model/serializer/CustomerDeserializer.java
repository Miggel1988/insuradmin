package com.insuradmin.global.model.serializer;

import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insuradmin.global.model.Customer;

//https://blog.knoldus.com/kafka-sending-object-as-a-message/
public class CustomerDeserializer implements Deserializer<List<Customer>> {

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {

	}

	@Override
	public List<Customer> deserialize(String arg0, byte[] arg1) {
		ObjectMapper mapper = new ObjectMapper();
		List<Customer> customer = null;
		try {
			customer = mapper.readValue(arg1, List.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return customer;
	}

}