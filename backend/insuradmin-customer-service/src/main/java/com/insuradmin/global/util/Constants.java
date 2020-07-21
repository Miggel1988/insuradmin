package com.insuradmin.global.util;

public class Constants {

	public final static String ENV_VAR_BOOTSTRAP_SERVERS_CONFIG = "KAFKA_CONNECT";
	public final static String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092"; //necessary, if KAFKA_CONNECT is not set
	
	public final static String TOPIC_POLICE_IN = "police.in";
	public final static String TOPIC_CUSTOMER_IN = "customer.in";
	public final static String TOPIC_POLICE_OUT = "police.out";
	public final static String TOPIC_CUSTOMER_OUT = "customer.out";
	public final static String TOPIC_REFRESH = "refresh";

	public final static String KEY_POLICE_REQUEST = "request";
	public final static String KEY_CUSTOMER_REQUEST = "request";
	
	public final static String GROUP_ID = "insuradmin-backend";			//TODO For each instance individual group, in order that every instance will consume message

	public final static String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
	public final static String POLICE_SERIALIZER = "com.insuradmin.global.model.serializer.PoliceSerializer";
	public final static String CUSTOMER_SERIALIZER = "com.insuradmin.global.model.serializer.CustomerSerializer";
	public final static String STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public final static String POLICE_DESERIALIZER = "com.insuradmin.global.model.serializer.PoliceDeserializer";
	public final static String CUSTOMER_DESERIALIZER = "com.insuradmin.global.model.serializer.CustomerDeserializer";
	
	
	
	
	
	
}
