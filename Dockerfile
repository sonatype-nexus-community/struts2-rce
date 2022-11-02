# Sonatype Nexus Ahab Evaluation
FROM tomcat:7.0.84-jre8
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

EXPOSE 8080
