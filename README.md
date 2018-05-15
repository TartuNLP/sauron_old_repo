# Sauron

Sauron is a reverse proxy for servers that provide a machine translation service such as [Nazgul](https://github.com/TartuNLP/nazgul). Sauron offers authentication, load balancing between translation servers and a simple REST API.
 
 
## API

API documentation can be found [here](https://app.swaggerhub.com/apis/kspar/sauron/v1.0). 

 
## Configuration
 
Translation servers (so-called providers) are specified in the configuration file ```/src/main/resources/providers.xml```.

All the following properties must be specified for each provider:

* Name (```name```) - used only for identification in logs.
* Language pair (```languagePair```) - string representing a translation language pair; there is no enforced format but the same string must be used as the value for the API request parameter ```langpair```. 
* Translation domain (```translationDomain```) - string representing a translation domain; this is similarly mapped to the API request parameter ```domain```. 
* GPU/CPU preference (```fast```) - boolean indicating whether the server is using a GPU for translation (whether it is fast); this is mapped to the API request parameter ```fast```.
* IP address (```ipAddress```) - IP address of the translation server.
* Port (```port```) - listening port of the translation server.
 

Sauron uses arbitrary variable-length string tokens for authentication. An environmental variable ```SAURON_ALLOWED_TOKENS``` must be set that contains semicolon-delimited allowed tokens. Each API request's authentication token (parameter ```auth```) is checked against this set of allowed tokens.  

## OpenNMT interface

The OpenNMT interface can be enabled by setting the environment variable ```SAURON_OPENNMT_INTERFACE=TRUE```.  
When using SDL Trados OpenNMT plugin to connect to the sauron server, use ```<ip-address>:<port>/v1.0/<langpair>``` as the server address and use the 'Client' feature field for your authentication token.
 
## Deployment

Sauron runs on Java Spring Boot.

The preferred method is to use Gradle to build a war:

    ./gradlew war
    
and deploy it into a Java web container such as Tomcat.
 
You can also run the server without a web container but this is not recommended:
    
    ./gradlew bootRun
    
    
## Dependencies

A [fork of JSocket](https://github.com/kspar/jsocket) is used for socket communication. This is already bundled with Sauron. No manual configuration is needed.
    
