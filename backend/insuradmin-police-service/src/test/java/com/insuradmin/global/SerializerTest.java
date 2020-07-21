package com.insuradmin.global;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.insuradmin.global.model.Police;
import com.insuradmin.global.model.serializer.PoliceDeserializer;
import com.insuradmin.global.model.serializer.PoliceSerializer;

import io.quarkus.test.junit.QuarkusTest;

@TestInstance(Lifecycle.PER_CLASS)
@QuarkusTest
public class SerializerTest {

	private PoliceSerializer policeSerializer;
	private PoliceDeserializer policeDeserializer;

	private List<Police> lstPoliceNormal_1 = new ArrayList<Police>();
	private List<Police> lstPoliceNormal_2 = new ArrayList<Police>();
	private List<Police> lstPoliceNormal_3 = new ArrayList<Police>();
	private List<Police> lstPoliceEmpty = new ArrayList<Police>();
	private List<Police> lstPoliceNull = null;
	
	private Police police_1,police_2,police_3,police_4,police_5,police_6,police_7;
	
    @BeforeAll
    public void setup() {
    	policeSerializer = new PoliceSerializer();
    	policeDeserializer = new PoliceDeserializer();

    	police_1 = new Police("Haus", "customer_1");
    	police_2 = new Police("Auto", "customer_1");
    	police_3 = new Police("Hund", "customer_1");
    	police_4 = new Police("Katze", "customer_1");
    	police_5 = new Police("Motorrad", "customer_1");
    	police_6 = new Police("Reise", "customer_1");
    	police_7 = new Police("Haus", "customer_2");
    	

    	lstPoliceNormal_1.add(police_1);
    	lstPoliceNormal_1.add(police_2);
    	lstPoliceNormal_1.add(police_3);
    	lstPoliceNormal_1.add(police_4);

    	lstPoliceNormal_2.add(police_1);
    	lstPoliceNormal_2.add(police_2);
    	lstPoliceNormal_2.add(police_3);
    	lstPoliceNormal_2.add(police_4);
    	
    	lstPoliceNormal_3.add(police_7);
    	lstPoliceNormal_3.add(police_2);
    	lstPoliceNormal_3.add(police_3);
    	lstPoliceNormal_3.add(police_4);
    }
	
    @Test
    public void testSerializationNormalList() throws Exception {
    	byte[] serialized_1 = policeSerializer.serialize("topic_1", lstPoliceNormal_1);
    	byte[] serialized_2 = policeSerializer.serialize("topic_1", lstPoliceNormal_2);
    	byte[] serialized_3 = policeSerializer.serialize("topic_1", lstPoliceNormal_3);
    	
    	for(int i=0; i<serialized_1.length; i++) {
    		Assertions.assertEquals(serialized_1[i], serialized_2[i], "Compare equal list after serialization");
    	}

    	List<Police> lstDeserialized_1 = policeDeserializer.deserialize("topic_1", serialized_1);
    	List<Police> lstDeserialized_2 = policeDeserializer.deserialize("topic_1", serialized_2);
    	List<Police> lstDeserialized_3 = policeDeserializer.deserialize("topic_1", serialized_3);
    	
    	Assertions.assertEquals(lstDeserialized_1, lstDeserialized_2, "Compare equal list after serialization/deserialization");
    	Assertions.assertNotEquals(lstDeserialized_1, lstDeserialized_3, "Compare unequal list after serialization/deserialization");
    }
    
    @Test
    public void testSerializationEmptyList() throws Exception {    	
    	byte[] serialized = policeSerializer.serialize("topic_1", lstPoliceEmpty);
    	List<Police> lstDeserialized = policeDeserializer.deserialize("topic_1", serialized);
    	Assertions.assertEquals(lstPoliceEmpty, lstDeserialized, "Compare empty list before and after serialization");
    	Assertions.assertNotEquals(lstPoliceNormal_1, lstDeserialized, "Normal list must be different to empty list after deserialization");
    }
    
    @Test
    public void testSerializationNullList() throws Exception {
    	byte[] serialized = policeSerializer.serialize("topic_1", lstPoliceNull);
    	List<Police> lstDeserialized = policeDeserializer.deserialize("topic_1", serialized);
    	Assertions.assertEquals(lstPoliceNull, lstDeserialized, "Compare null list before and after serialization");
    }
    
    @Test
    public void testSerializationDifferentTopic() throws Exception {
    	byte[] serialized_1 = policeSerializer.serialize("topic_1", lstPoliceNormal_1);
    	byte[] serialized_2 = policeSerializer.serialize("topic_999", lstPoliceNormal_2);
    	
    	for(int i=0; i<serialized_1.length; i++) {
    		Assertions.assertEquals(serialized_1[i], serialized_2[i], "Compare equal list after serialization with different topics; must not interfer results");
    	}

    	List<Police> lstDeserialized_1 = policeDeserializer.deserialize("topic_1", serialized_1);
    	List<Police> lstDeserialized_2 = policeDeserializer.deserialize("topic_1", serialized_2);
    	
    	Assertions.assertEquals(lstDeserialized_1, lstDeserialized_2, "Compare equal list after serialization/deserialization with different topics; must not interfer results");
    }
    
    
    


}
