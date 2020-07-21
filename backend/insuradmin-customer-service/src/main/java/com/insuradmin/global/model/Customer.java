package com.insuradmin.global.model;

import java.util.UUID;

public class Customer {

	private String customID;

	private String name;

	public Customer() {
		this("Unbekannt");
	}

	public Customer(String name) {
		UUID uuid = UUID.randomUUID();
		this.customID = uuid.toString();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCustomID() {
		return customID;
	}

	public void setCustomID(String customID) {
		this.customID = customID;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Customer [customID=" + customID + ", name=" + name + "]";
	}

}
