package com.insuradmin.global.service;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.insuradmin.global.model.Customer;

@ApplicationScoped
public class CustomerService {
	
	@Inject
	MongoService mongoService;
	
	public CustomerService() {
	}

	public void create(Customer customer) {
		mongoService.addCustomer(customer);
	}

	public void delete(String id) {
		mongoService.removeDocuments(MongoService.COLLECTION_CUSTOMER, "_id", id);
	}

	public Collection<Customer> getCustomers() {
		return mongoService.loadCustomer();
	}

}