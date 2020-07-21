package com.insuradmin.global.websockets;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.insuradmin.global.model.Message;
import com.insuradmin.global.model.MessageType;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class WebsocketTest {

	private static final LinkedBlockingDeque<Object> MESSAGES = new LinkedBlockingDeque<>();

	@TestHTTPResource("/gw")
	URI uri;



    @ClientEndpoint
    public static class Client {

        @OnOpen
        public void open(Session session) {
            MESSAGES.add("connect");
        }
        
        @OnMessage
        void message(String message) {
            MESSAGES.add(message);
        }

 

    }
}
