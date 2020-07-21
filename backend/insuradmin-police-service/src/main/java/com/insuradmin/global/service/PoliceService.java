package com.insuradmin.global.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.insuradmin.global.model.Police;

@ApplicationScoped
public class PoliceService {
	
	@Inject
	MongoService mongoService;
	
	private Random rand = new Random(); 
	
	private List<String> lstInsuranceNames = Arrays.asList("Haus", "Auto", "Motorrad", "PKV", "Brille", "Glas", "Reise", "Haustier", "Velo", "Feuer", "Elementar");
	
	public PoliceService() {
	}

	public void create(Police police) {
		mongoService.addPolice(police);
	}

	public void delete(String id) {
		mongoService.removeDocuments(MongoService.COLLECTION_POLICE, "customID", id);
	}
	
	//Random for Walking Skeleton
	public void create(String customerID) {
		Police newPolice = new Police(lstInsuranceNames.get(rand.nextInt(lstInsuranceNames.size())), customerID);
		mongoService.addPolice(newPolice);
	}
	
	public Collection<Police> getPolices(String customerID) {
		return mongoService.loadPolicies(customerID);
	}

}