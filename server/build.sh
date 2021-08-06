#export EZYFOX_SERVER_HOME=
mvn -pl . clean install
mvn -pl EzyRoulette-common -Pexport clean install
mvn -pl EzyRoulette-app-api -Pexport clean install
mvn -pl EzyRoulette-app-entry -Pexport clean install
mvn -pl EzyRoulette-plugin -Pexport clean install
cp EzyRoulette-zone-settings.xml $EZYFOX_SERVER_HOME/settings/zones/
