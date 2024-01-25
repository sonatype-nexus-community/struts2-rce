# Sonatype Nexus Ahab Evaluation
FROM tomcat:7.0.84-jre8
LABEL nexus_scan="true"
RUN apt-get update && apt-get install -y ca-certificates && apt-get upgrade -y

# Sonatype Nexus Ahab Evaluation
ARG IQ_TOKEN
ARG IQ_USER

RUN set -ex \
	&& rm -rf /usr/local/tomcat/webapps/* \
	&& chmod a+x /usr/local/tomcat/bin/*.sh

COPY target/struts2-rest-showcase.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
