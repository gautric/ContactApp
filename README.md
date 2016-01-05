# ContactApp
Hibernate search, JPA, Hibernate ORM, JTA, JBossForge


## Setup 

`mvn clean package`

## Deploy

You can deploy the application on WLFY 10CR2 

## Inject Data 

`sh data/contact_inject.sh`

## Hibernate search engine

Open <http://localhost:8080/ContactsApp/app.html#/Contacts/engine>

and enter some query like 

 * `nom:xa* -ville:ra*`
 * `prenom:ro*`
 


