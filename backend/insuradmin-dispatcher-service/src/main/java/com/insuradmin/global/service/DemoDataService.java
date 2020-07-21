package com.insuradmin.global.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.insuradmin.global.model.Customer;
import com.insuradmin.global.model.Police;

@ApplicationScoped
public class DemoDataService {
	
	@Inject
	MongoService mongoService;
	
	@ConfigProperty(name = "mongo.db.customer")
	String customerDatabase;
	
	@ConfigProperty(name = "mongo.db.police")
	String policeDatabase;
	
	public void initDB() {
		mongoService.removeAllDocuments(customerDatabase, MongoService.COLLECTION_CUSTOMER);
		mongoService.removeAllDocuments(policeDatabase, MongoService.COLLECTION_POLICE);
		List<Customer> lstCustomer = new ArrayList<Customer>();
		List<Police> lstPolices = new ArrayList<Police>();

		lstCustomer.add(new Customer("Michael Hassel"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Auto","Motorrad","Haus","Reise","Haftpflicht","Hausrat","Hunde"));
		lstCustomer.add(new Customer("Jana Hassel"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Auto","Haftpflicht","Hausrat","Hunde"));
		lstCustomer.add(new Customer("Evi Miessmer"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Reise","Haftpflicht","Hausrat","Hunde"));
		lstCustomer.add(new Customer("Paul Panzer"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Fahrrad","Haus","Reise","Haftpflicht"));
		lstCustomer.add(new Customer("Stefan Eckert"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Auto","Hausrat"));
		lstCustomer.add(new Customer("Tanja Heizer"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Haus","Haftpflicht","Hausrat"));
		lstCustomer.add(new Customer("Mike Hansen"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Katze", "Auto"));
		lstCustomer.add(new Customer("Sabi Meike"));
		lstPolices.addAll(addPolice(lstCustomer.get(lstCustomer.size()-1), "Auto","Motorrad","Fahrrad","Haus","Reise","Haftpflicht","Hausrat","Hunde"));

		for (Customer c : lstCustomer) {
			mongoService.addCustomer(c);
		}

		for (Police p : lstPolices) {
			mongoService.addPolice(p);
		}
	}
	
	private List<Police> addPolice(Customer customer, String... polices) {
		List<Police> lstPolice = new ArrayList<Police>();
		
		for(String police: polices) {
			lstPolice.add(new Police(police, customer.getCustomID()));
		}
	
		return lstPolice;
	}
}