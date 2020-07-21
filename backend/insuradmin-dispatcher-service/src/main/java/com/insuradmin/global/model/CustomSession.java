package com.insuradmin.global.model;

import javax.websocket.Session;

public class CustomSession{
	
	private Session session;
	
    private String currentCustomerID;
    
    public CustomSession(Session session) {
    	this(session, "0");
    }
    
    public CustomSession(Session session, String currentCustomerID) {
    	this.session = session;
    	this.currentCustomerID = currentCustomerID;
    }

	public String getCurrentCustomerID() {
		return currentCustomerID;
	}

	public void setCurrentCustomerID(String currentCustomerID) {
		this.currentCustomerID = currentCustomerID;
	}

	public Session getSession() {
		return session;
	}

    

}
