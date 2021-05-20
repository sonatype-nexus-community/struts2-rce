# Sonatype Nexus Ahab Evaluation
FROM alpine as ahab
LABEL stage=AHAB

RUN apk add --no-cache curl
WORKDIR /tmp/ahab
RUN curl -o ahab -O -L https://github.com/sonatype-nexus-community/ahab/releases/download/v0.2.5/ahab-linux.amd64-v0.2.5

FROM tomcat
LABEL nexus_scan="true"
RUN apt-get update && apt-get install -y ca-certificates && apt-get upgrade -y

# Sonatype Nexus Ahab Evaluation
ARG IQ_TOKEN
ARG IQ_USER
ARG IQ_SERVER
ARG IQ_STAGE

COPY --from=ahab /tmp/ahab /tmp/
#RUN chmod +x /tmp/ahab \
#	&& dpkg-query --show --showformat='${Package} ${Version}\n' | /tmp/ahab chase

RUN set -ex \
	&& rm -rf /usr/local/tomcat/webapps/* \
	&& chmod a+x /usr/local/tomcat/bin/*.sh

COPY target/struts2-rest-showcase.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
