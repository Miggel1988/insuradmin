package com.insuradmin.global;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.insuradmin.global.model.Message;
import com.insuradmin.global.model.MessageDecoder;
import com.insuradmin.global.model.MessageEncoder;
import com.insuradmin.global.model.MessageType;

import io.quarkus.test.junit.QuarkusTest;

@TestInstance(Lifecycle.PER_CLASS)
@QuarkusTest
public class DecoderTest {

	private MessageDecoder messageDecoder;
	private MessageEncoder messageEncoder;
	
	private Message message_1, message_2,message_3,message_4;
	
    @BeforeAll
    public void setup() {
    	messageDecoder = new MessageDecoder();
    	messageEncoder = new MessageEncoder();

    	message_1 = new Message();
    	message_2 = new Message("a",MessageType.CONFIRMATION);
    	message_3 = new Message("a",MessageType.CONFIRMATION);
    	message_4 = new Message("a",MessageType.LIST_CUSTOMER);
    }
	
    @Test
    public void testEqual() throws Exception {
    	String encodedMsg_1 = messageEncoder.encode(message_1);
    	String encodedMsg_2 = messageEncoder.encode(message_2);
    	String encodedMsg_3 = messageEncoder.encode(message_3);
    	String encodedMsg_4 = messageEncoder.encode(message_4);
    	
    	Assertions.assertEquals(encodedMsg_2, encodedMsg_3, "Compare equal messages after encoding");
    	Assertions.assertNotEquals(encodedMsg_2, encodedMsg_4, "Compare unequal messages after encoding");

    	Message decodedMsg_1 = messageDecoder.decode(encodedMsg_1);
    	Message decodedMsg_2 = messageDecoder.decode(encodedMsg_2);
    	Message decodedMsg_3 = messageDecoder.decode(encodedMsg_3);
    	Message decodedMsg_4 = messageDecoder.decode(encodedMsg_4);
    	
    	Assertions.assertEquals(decodedMsg_2, decodedMsg_3, "Compare equal messages after decoding");
    	Assertions.assertEquals(decodedMsg_2, message_2, "Compare equal messages after decoding");
    	Assertions.assertNotEquals(decodedMsg_1, decodedMsg_2, "Compare unequal messages after decoding");
    	Assertions.assertNotEquals(decodedMsg_4, decodedMsg_3, "Compare unequal messages after decoding");
    	Assertions.assertNotEquals(decodedMsg_4, encodedMsg_4, "Compare unequal messages after decoding, since encoded and decoded is different");
    }
    
    


}
