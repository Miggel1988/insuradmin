package com.insuradmin.global;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.insuradmin.global.model.Customer;
import com.insuradmin.global.model.serializer.CustomerDeserializer;
import com.insuradmin.global.model.serializer.CustomerSerializer;

import io.quarkus.test.junit.QuarkusTest;

@TestInstance(Lifecycle.PER_CLASS)
@QuarkusTest
public class SerializerTest {

	private CustomerSerializer customerSerializer;
	private CustomerDeserializer customerDeserializer;

	private List<Customer> lstCustomerNormal_1 = new ArrayList<Customer>();
	private List<Customer> lstCustomerNormal_2 = new ArrayList<Customer>();
	private List<Customer> lstCustomerNormal_3 = new ArrayList<Customer>();
	private List<Customer> lstCustomerEmpty = new ArrayList<Customer>();
	private List<Customer> lstCustomerNull = null;
	
	private Customer customer_1,customer_2,customer_3,customer_4,customer_5;
	
    @BeforeAll
    public void setup() {
    	customerSerializer = new CustomerSerializer();
    	customerDeserializer = new CustomerDeserializer();

    	customer_1 = new Customer("Michael");
    	customer_2 = new Customer("Anna");
    	customer_3 = new Customer("Evi");
    	customer_4 = new Customer("Jana");
    	customer_5 = new Customer("Herbert");
    	

    	lstCustomerNormal_1.add(customer_1);
    	lstCustomerNormal_1.add(customer_2);
    	lstCustomerNormal_1.add(customer_3);
    	lstCustomerNormal_1.add(customer_4);

    	lstCustomerNormal_2.add(customer_1);
    	lstCustomerNormal_2.add(customer_2);
    	lstCustomerNormal_2.add(customer_3);
    	lstCustomerNormal_2.add(customer_4);
    	
    	lstCustomerNormal_3.add(customer_5);
    	lstCustomerNormal_3.add(customer_2);
    	lstCustomerNormal_3.add(customer_3);
    	lstCustomerNormal_3.add(customer_4);
    }
	
    @Test
    public void testSerializationNormalList() throws Exception {
    	byte[] serialized_1 = customerSerializer.serialize("topic_1", lstCustomerNormal_1);
    	byte[] serialized_2 = customerSerializer.serialize("topic_1", lstCustomerNormal_2);
    	byte[] serialized_3 = customerSerializer.serialize("topic_1", lstCustomerNormal_3);
    	
    	for(int i=0; i<serialized_1.length; i++) {
    		Assertions.assertEquals(serialized_1[i], serialized_2[i], "Compare equal list after serialization");
    	}

    	List<Customer> lstDeserialized_1 = customerDeserializer.deserialize("topic_1", serialized_1);
    	List<Customer> lstDeserialized_2 = customerDeserializer.deserialize("topic_1", serialized_2);
    	List<Customer> lstDeserialized_3 = customerDeserializer.deserialize("topic_1", serialized_3);
    	
    	Assertions.assertEquals(lstDeserialized_1, lstDeserialized_2, "Compare equal list after serialization/deserialization");
    	Assertions.assertNotEquals(lstDeserialized_1, lstDeserialized_3, "Compare unequal list after serialization/deserialization");
    }
    
    @Test
    public void testSerializationEmptyList() throws Exception {    	
    	byte[] serialized = customerSerializer.serialize("topic_1", lstCustomerEmpty);
    	List<Customer> lstDeserialized = customerDeserializer.deserialize("topic_1", serialized);
    	Assertions.assertEquals(lstCustomerEmpty, lstDeserialized, "Compare empty list before and after serialization");
    	Assertions.assertNotEquals(lstCustomerNormal_1, lstDeserialized, "Normal list must be different to empty list after deserialization");
    }
    
    @Test
    public void testSerializationNullList() throws Exception {
    	byte[] serialized = customerSerializer.serialize("topic_1", lstCustomerNull);
    	List<Customer> lstDeserialized = customerDeserializer.deserialize("topic_1", serialized);
    	Assertions.assertEquals(lstCustomerNull, lstDeserialized, "Compare null list before and after serialization");
    }
    
    @Test
    public void testSerializationDifferentTopic() throws Exception {
    	byte[] serialized_1 = customerSerializer.serialize("topic_1", lstCustomerNormal_1);
    	byte[] serialized_2 = customerSerializer.serialize("topic_999", lstCustomerNormal_2);
    	
    	for(int i=0; i<serialized_1.length; i++) {
    		Assertions.assertEquals(serialized_1[i], serialized_2[i], "Compare equal list after serialization with different topics; must not interfer results");
    	}

    	List<Customer> lstDeserialized_1 = customerDeserializer.deserialize("topic_1", serialized_1);
    	List<Customer> lstDeserialized_2 = customerDeserializer.deserialize("topic_1", serialized_2);
    	
    	Assertions.assertEquals(lstDeserialized_1, lstDeserialized_2, "Compare equal list after serialization/deserialization with different topics; must not interfer results");
    }
    
    
    


}
