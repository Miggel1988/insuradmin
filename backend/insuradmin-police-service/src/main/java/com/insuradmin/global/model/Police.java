package com.insuradmin.global.model;

import java.util.UUID;

public class Police {

	private String customID;

	private String name;

	private String customerID;

	public Police() {
	}

	public Police(String name, String customerID) {
		UUID uuid = UUID.randomUUID();
		this.customID = uuid.toString();
		this.name = name;
		this.customerID = customerID;
	}

	public String getName() {
		return name;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public String getCustomID() {
		return customID;
	}

	public void setCustomID(String customID) {
		this.customID = customID;
	}

	@Override
	public String toString() {
		return "Police [customID=" + customID + ", name=" + name + ", customerID=" + customerID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customID == null) ? 0 : customID.hashCode());
		result = prime * result + ((customerID == null) ? 0 : customerID.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Police other = (Police) obj;
		if (customID == null) {
			if (other.customID != null)
				return false;
		} else if (!customID.equals(other.customID))
			return false;
		if (customerID == null) {
			if (other.customerID != null)
				return false;
		} else if (!customerID.equals(other.customerID))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
