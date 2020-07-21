package com.insuradmin.global.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.insuradmin.global.model.Customer;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@ApplicationScoped
public class MongoService {

	public final static String COLLECTION_CUSTOMER = "customer";

	@Inject
	MongoClient mongoClient;

	@ConfigProperty(name = "mongo.db")
	String database;

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

	public void add(String collection, Map<String, Object> propMap2Store) {
		Document document = new Document();
		document.putAll(propMap2Store);
		this.getCollection(this.database, collection).insertOne(document);
	}

	public void update(String collection, String fieldname2Compare, String value2Compare, Object object2Update) {
		this.getCollection(this.database, collection).updateOne(Filters.eq(fieldname2Compare, value2Compare),
				new Document("$set", object2Update));
	}

	public void delete(String collection, String fieldname2Compare, String value2Compare) {
		this.getCollection(this.database, collection).deleteMany(Filters.eq(fieldname2Compare, value2Compare));
	}

	public void removeDocuments(String collection, String fieldname2Compare, String value2Compare) {
		this.getCollection(this.database, collection).deleteMany(Filters.eq(fieldname2Compare, value2Compare));
	}

	public void removeAllDocuments(String collection) {
		BasicDBObject document = new BasicDBObject();
		this.getCollection(this.database, collection).deleteMany(document);
	}

	public boolean hasEntry(String collection, String fieldname2Compare, String value2Compare) {
		MongoCursor<Document> cursor = this.getCollection(this.database, collection)
				.find(Filters.eq(fieldname2Compare, value2Compare)).limit(1).iterator();
		return cursor.hasNext();
	}

	public void addCustomer(Customer customer) {
		getDatabase(database).getCollection(COLLECTION_CUSTOMER, Customer.class).insertOne(customer);
	}

	public List<Customer> loadCustomer() {
		List<Customer> lstCustomer = new ArrayList<>();
		MongoCursor<Customer> cursor = getDatabase(database).getCollection(COLLECTION_CUSTOMER, Customer.class).find()
				.iterator();

		try {
			while (cursor.hasNext()) {
				lstCustomer.add(cursor.next());
			}
		} finally {
			cursor.close();
		}
		return lstCustomer;
	}

}