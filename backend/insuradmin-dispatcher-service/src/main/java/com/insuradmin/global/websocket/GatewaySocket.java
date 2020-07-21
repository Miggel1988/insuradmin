package com.insuradmin.global.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

import com.insuradmin.global.model.CustomSession;
import com.insuradmin.global.model.Message;
import com.insuradmin.global.model.MessageDecoder;
import com.insuradmin.global.model.MessageEncoder;
import com.insuradmin.global.model.MessageType;
import com.insuradmin.global.service.KafkaProducerService;

@ServerEndpoint(value = "/gw", decoders = MessageDecoder.class, encoders = MessageEncoder.class)

@ApplicationScoped
public class GatewaySocket {

	private static final Logger LOG = Logger.getLogger(GatewaySocket.class);

	@Inject
	KafkaProducerService kafkaProducerService;

	Map<String, CustomSession> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		LOG.info("onOpen: " + session.getId());
		sessions.put(session.getId(), new CustomSession(session));
		broadcast(new Message("connected"), sessions.get(session.getId()).getCurrentCustomerID());
	}

	@OnClose
	public void onClose(Session session) {
		LOG.info("onClose: " + session.getId());
		sessions.remove(session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		sessions.remove(session.getId());
		LOG.error("onError", throwable);
	}

	@OnMessage
	public void onMessage(Session session, Message message) {
		LOG.info("onMessage: " + message);
		String customerID;

		broadcast(new Message(String.format("message received; %s", message.getMessageType().toString()),
				MessageType.CONFIRMATION), sessions.get(session.getId()).getCurrentCustomerID());

		if (message.getContent() != null && message.getContent().equals("test"))
			return; // disable kafka for tests

		switch (message.getMessageType()) {
		case LIST_CUSTOMER:
			kafkaProducerService.requestCustomers();
			return;
		case LIST_POLICE:
			customerID = message.getContent().toString();
			sessions.get(session.getId()).setCurrentCustomerID(customerID);
			kafkaProducerService.requestPolices(customerID);
			return;
		case DELETE_POLICE:
			String combo = message.getContent().toString();
			kafkaProducerService.deletePolice(combo);
			return;
		case CREATE_POLICE:
			customerID = message.getContent().toString();
			kafkaProducerService.createPolice(customerID);
			return;
		default:
			return;
		}
	}

	public void refreshAll() {
		sessions.values().forEach(s -> {
			kafkaProducerService.requestPolices(s.getCurrentCustomerID());
		});
	}

	public void broadcast(Object message) {
		broadcast(message, null);
	}

	public void broadcast(Object message, String customerID) {
		LOG.info("broadcast: " + message);
		sessions.values().forEach(s -> {
			if (customerID == null || s.getCurrentCustomerID().equals(customerID)) {
				s.getSession().getAsyncRemote().sendObject(message, result -> {
					if (result.getException() != null) {
						System.out.println("Unable to send message: " + result.getException());
					}
				});
			}
		});
	}

}