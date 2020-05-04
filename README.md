# Currency Converter

The Travel Calculator application is built using Spring Boot. It uses an in-memory graph to traverse a certain map of countries and return the number the visits and the number of countries, as well as a currency per country calculated per country if rate is provided. 

## Running the application

The application can be started up by entering the following command from within the TravelCalculator directory

`mvn spring-boot:run`

Alternately, the following command may be executed to build and execute an executable JAR

`mvn clean package`

`java -jar target/TravelManager-1.0.0-SNAPSHOT.jar`

Once running, the API information and details can be accessed on:
`http://localhost:18081/swagger-ui.html#/travel-controller`

## Application Details

The Travel calculator application has a pre-defined map for Bulgaria, Northern Macedonia, Greece and Romania. 
It includes data for their neighbours, which if queried separately - their travel routes will be incomplete. 

It can be used in the following flow:
- If desired, a get request can be sent, to load the currency exchange rates from ECB API, save them locally and return them in the body of the response.
- Upon a decision to overwrite them,  a request with the desired exchange rate should be send.
 If no rates are updated the calculation will come only in base currency.
- A request with the reqired data for the calculation should be send.
- Countries can be added in the graph, on a separate request their connections should be updated in order the graph to traverse properly. 

## Configuration

In the Spring setup properties, the src/main/resources/application.properties file are specified the following:

`system.travelconfig.exchangeRateUrl=https://api.exchangeratesapi.io/latest?base=`
`system.travelconfig.defaultCurrency=EUR`
`system.travelconfig.trasversalDepthLevel=1`


## Known Limitation

- As the added countries, their connections and the currency exchange rates are in memory, the information will be lost upon restart or shutdown. 
- It is possible to load the currency exchange rates for the availble currencies from the ECB API. 
- It is possible to do so manually providing the rates manually. 
The latter will replace the autmatically gotten rates(or the previously configured ones) and vice versa. 
