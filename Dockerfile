# Sonatype Nexus Ahab Evaluation
FROM tomcat:7

RUN apt-get update && apt-get install -y ca-certificates && apt-get upgrade -y

RUN set -ex \
	&& rm -rf /usr/local/tomcat/webapps/* \
	&& chmod a+x /usr/local/tomcat/bin/*.sh

COPY target/struts2-rest-showcase.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
