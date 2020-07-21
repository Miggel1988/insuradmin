import { SessionService } from './session.service';
import { LIST_CUSTOMER, LIST_POLICE, DELETE_POLICE, SEPERATOR, CREATE_POLICE } from '../util/constants';
import { environment } from '../../environments/environment';

export class WebSocketAPI {
    webSocketEndPoint: string = environment.webSocketEndPoint;

    socket

    constructor(public sessionService: SessionService) {

    }

    _connect() {
        this.socket = new WebSocket(this.webSocketEndPoint);

        const _this = this;
        this.socket.onopen = function () {
            console.log("web_socket;connect=true");
            _this.socket.send(JSON.stringify({ messageType: LIST_CUSTOMER }));
        };
        this.socket.onmessage = function (m) {
            console.log("web_socket;received_message=", m.data);
            _this.sessionService.handleMessage(m.data)
        };
    };

    _disconnect() {
        this.socket.close()
        console.log("web_socket;disconnect");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error) {
        console.error("web_socket;errorCallBack",error);
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

    _requestCustomerList() {
        console.info("web_socket;requestCustomerList");
        this.socket.send(JSON.stringify({ messageType: LIST_CUSTOMER }));
    }

    _requestPoliceList(customerID) {        
        console.info("web_socket;requestPoliceList;customerID=",customerID);
        this.socket.send(JSON.stringify({ messageType: LIST_POLICE, content: customerID }));
    }

    _deletePolice(customerID, policeID) {
        console.info("web_socket;deletePolice;policeID=",policeID);
        this.socket.send(JSON.stringify({ messageType: DELETE_POLICE, content: customerID + SEPERATOR + policeID }));
    }

    _createPolice(customerID) {
        console.info("web_socket;createPolice;customerID=",customerID);
        this.socket.send(JSON.stringify({ messageType: CREATE_POLICE, content: customerID }));
    }
    

}