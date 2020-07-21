package com.insuradmin.global.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.insuradmin.global.model.Customer;
import com.insuradmin.global.model.Police;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ApplicationScoped
public class MongoService {

	public final static String COLLECTION_CUSTOMER = "customer";
	public final static String COLLECTION_POLICE = "police";

	@Inject
	MongoClient mongoClient;

	@ConfigProperty(name = "mongo.db.customer")
	String customerDatabase;
	
	@ConfigProperty(name = "mongo.db.police")
	String policeDatabase;

	CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
			CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

	public MongoService() {
	}

	private MongoDatabase getDatabase(String database) {
		return this.mongoClient.getDatabase(database).withCodecRegistry(this.codecRegistry);
	}

	private MongoCollection<Document> getCollection(String database, String collection) {
		return this.getDatabase(database).getCollection(collection);
	}


	public void removeAllDocuments(String database, String collection) {
		BasicDBObject document = new BasicDBObject();
		this.getCollection(database, collection).deleteMany(document);
	}
	
	public void addPolice(Police police) {
		getDatabase(policeDatabase).getCollection(COLLECTION_POLICE, Police.class).insertOne(police);
	}
	
	public void addCustomer(Customer customer) {
		getDatabase(customerDatabase).getCollection(COLLECTION_CUSTOMER, Customer.class).insertOne(customer);
	}
	
}