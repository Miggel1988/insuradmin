package com.insuradmin.global.model.serializer;

import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insuradmin.global.model.Police;

//https://blog.knoldus.com/kafka-sending-object-as-a-message/
public class PoliceDeserializer implements Deserializer<List<Police>> {

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {

	}

	@Override
	public List<Police> deserialize(String arg0, byte[] arg1) {
		ObjectMapper mapper = new ObjectMapper();
		List<Police> lstPolice = null;
		try {
			lstPolice = mapper.readValue(arg1, List.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return lstPolice;
	}

}