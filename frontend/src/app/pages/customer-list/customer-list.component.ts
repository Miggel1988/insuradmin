import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { STORE_KEY_CUSTOMER_ID } from '../../util/constants';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  name: string;

  constructor(private sessionService: SessionService) { 
    this.sessionService.connect();
  }

  ngOnInit() {

  }

  connect(){
    this.sessionService.connect();
  }

  disconnect(){
    this.sessionService.disconnect();
  }

  selectCustomer(customerID){
    this.sessionService.selectedCustomerID = customerID
    this.sessionService.store(STORE_KEY_CUSTOMER_ID, customerID)
    this.requestPolicyList()
  }

  requestPolicyList(){
    this.sessionService.requestPoliceList(this.sessionService.selectedCustomerID);
  }

  deletePolice(id){
    this.sessionService.deletePolice(this.sessionService.selectedCustomerID, id)
    this.requestPolicyList()
  }

  createPolice(){
    this.sessionService.createPolice(this.sessionService.selectedCustomerID)
    this.requestPolicyList()
  }

}
