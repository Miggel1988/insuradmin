import { Injectable } from '@angular/core';
import { WebSocketAPI } from './web-socket-api';
import { LIST_CUSTOMER, LIST_POLICE, STORE_KEY_CUSTOMER_ID, STORE_KEY_CUSTOMERS, STORE_KEY_POLICES } from '../util/constants';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  webSocketAPI: WebSocketAPI;

  selectedCustomerID: string
  customers: string[] = []
  polices: { [id: string]: any; } = {} // TODO: Class for police instead of 'any'

  constructor() {
    this.webSocketAPI = new WebSocketAPI(this);
    this.selectedCustomerID = this.load(STORE_KEY_CUSTOMER_ID)
    this.customers = this.load(STORE_KEY_CUSTOMERS)? this.load(STORE_KEY_CUSTOMERS):[]
    this.polices = this.load(STORE_KEY_POLICES)? this.load(STORE_KEY_POLICES):{}
  }

  store(key, value){
    if(key)
      localStorage.setItem(key, JSON.stringify(value));
  }

  load(key){
    return JSON.parse(localStorage.getItem(key));
  }

  connect() {
    this.webSocketAPI._connect();
  }

  disconnect() {
    this.webSocketAPI._disconnect();
  }

  requestCustomerList() {
    this.webSocketAPI._requestCustomerList();
  }

  deletePolice(customerID, policeID){
    this.webSocketAPI._deletePolice(customerID, policeID);
  }

  createPolice(customerID){
    this.webSocketAPI._createPolice(customerID);
  }

  requestPoliceList(customerID: string) {
    this.webSocketAPI._requestPoliceList(customerID);
  }

  handleMessage(message) {
    var messageObj = JSON.parse(message);

    console.debug("session_service;handleMessage", messageObj);

    switch (messageObj.messageType) {
      case LIST_CUSTOMER:
        this.customers = messageObj.content
        this.store(STORE_KEY_CUSTOMERS, this.customers)
        break;
      case LIST_POLICE:
        this.polices[this.selectedCustomerID] = messageObj.content
        this.store(STORE_KEY_POLICES, this.polices)
        break;
    }

  }

}
