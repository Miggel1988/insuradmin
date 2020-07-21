
# InsurAdmin
InsurAdmin steht für ein Administrationsprogramm für Versicherungen um Kunden und Policen zu verwalten.
Die aktuelle Version ist ein *Walking Skeleton* mit End-to-End Durchstich.

**Projekt Wiki & Architekturdokumentation:**
[https://insuradmin.atlassian.net/wiki/spaces/INSURADMIN/pages/196610/](https://insuradmin.atlassian.net/wiki/spaces/INSURADMIN/pages/196610/)

**Präsentationsseite:**
[https://insuradmin.atlassian.net/wiki/spaces/INSURADMIN/pages/9830401/Pr+sentation](https://insuradmin.atlassian.net/wiki/spaces/INSURADMIN/pages/9830401/Pr+sentation)

**Test Version auf AWS:**
Die aktuelle Version ist auf AWS deployed. Den Link könnt ihr durch einen request bei dem Repository Owner erhalten.

## Bestandteile des Systems und lokales Deployment
	                             +-----+----+   +-----+----+
	                             | Customer |   | MongoDB  |
	                          +--+ Service  +--+|          |
	                          |  +----------+   +----------+
	                        Kafka
	                          |
	+--------+             +--+---------+
	| Angular|  Websockets | Dispatcher |
	| Client +-------------+ Service    |
	+--------+             +--+---------+
	                          |
	                        Kafka
	                          |  +----------+   +-----+----+
	                          +--+ Police   +--+| MongoDB  |
	                             | Service  |   |          |
	                             +-----+----+   +----------+

Das System ist als **dockerized Microservice Architektur** aufgebaut. 
Mit dem Befehl
```
docker-compose.exe --project-name insuradmin up
```
werden alle benötigten Ressourcen gestartet (inklusive MongoDB und Kafka). Anschliessend ist die Anwendung unter [http://localhost:4200/](http://localhost:4200/)erreichbar.

### Entwicklung
**Voraussetzungen**
1. [Maven](https://maven.apache.org/) muss installiert sein
2. [NodeJS](https://nodejs.org/en/) muss installiert sein
3. Eine lokale MongoDB Instanz muss verfügbar sein
4. Docker muss verfügbar sein

**Backend**
Microservice Architektur mit folgenden Bestandteilen:
* Folder *backend/insuradmin-dispatcher-service*: Quarkus Applikation
* Folder *backend/insuradmin-customer-service*: Quarkus Applikation
* Folder *backend/insuradmin-police-service*: Quarkus Applikation

```
cd backend/<SERVICE_TO_START>
mvnw compile quarkus:dev:
```
**Frontend**
Folder *frontend*: Angular 8 Applikation
```
cd frontend
npm i
ng serve
```
Unter [http://localhost:4200/](http://localhost:4200/) ist die Anwendung erreichbar.

**Extene benötigte Systeme**
* MongoDB
* Kafka


## Tests
### Frontend

### Backend
```
cd backend/<SERVICE_TO_START_TESTS>
mvnw test quarkus:dev
```

## Offene Punkte
Da es sich aktuell um ein Walking Skeleton handelt, sind einige Punkte noch offen.
### Frontend
 - [ ] Protractor und Jasmine Tests
 - [ ] CRUD Operations
 - [ ] Login-Funktionalität

### Backend
 - [ ] Duplizierter Code (Konstanten, POJOs, etc.) in eigene Lib (jar) auslagern und diese in den Services einbinden
 - [ ] KAFKA Tests
 - [ ] Security Maßnahmen (z.B. Verwendung des *wss* Protokolls anstatt *ws*)
 - [ ] IAM (z.B. mittels AWS API Gateway)
 - [ ] Deployment via AWS ECS
 - [ ] Zielarchitektur mit [MongoDB Kafka Connector](https://docs.mongodb.com/kafka-connector/master/)
